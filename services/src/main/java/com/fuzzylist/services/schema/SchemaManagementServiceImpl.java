package com.fuzzylist.services.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.models.schema.SchemaDefinitionEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import com.fuzzylist.models.schema.SchemaTransactionEntity;
import com.fuzzylist.repositories.schema.DomainRepository;
import com.fuzzylist.repositories.schema.SchemaDefinitionRepository;
import com.fuzzylist.repositories.schema.SchemaRepository;
import com.fuzzylist.repositories.schema.SchemaTransactionRepository;
import com.fuzzylist.services.domain.DomainManagementService;
import com.fuzzylist.services.schema.model.FieldDefinition;
import com.fuzzylist.services.schema.model.SchemaDefinition;
import com.fuzzylist.services.schema.model.migration.MigrationException;
import com.fuzzylist.services.schema.model.migration.Migrations;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.time.Instant;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * A concrete schema management service based on JPA repositories.
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
@Service
@RequiredArgsConstructor
public class SchemaManagementServiceImpl implements SchemaManagementService {

    private final IdGenerator idGenerator;
    private final TransactionTemplate transactionTemplate;
    private final DomainRepository domainRepository;
    private final SchemaRepository schemaRepository;
    private final SchemaTransactionRepository schemaTransactionRepository;
    private final DomainManagementService domainManagementService;
    private final SchemaDefinitionRepository schemaDefinitionRepository;

    /**
     * Serializer and deserialize schema definition.
     */
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    /**
     * Maximum number of retries to create a new schema.
     */
    private static final int MAX_COMMIT_RETRY_COUNT = 10;

    /**
     * Class logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SchemaManagementServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = REQUIRED)
    public SchemaEntity createSchema(String domain, String schemaName)
            throws IllegalArgumentException, UnknownDomainException, SchemaAlreadyExistException {
        Assert.hasText(schemaName, "Schema name cannot be null or empty.");

        logger.info("Request to create a new schema '{}/{}'.", schemaName, domain);
        DomainEntity domainEntity = domainManagementService.findDomainByName(domain);

        //
        // Create the schema entity ahead of time, so we can use it in the transaction.
        //
        SchemaEntity newSchema = new SchemaEntity(domainEntity, schemaName);

        //
        // Try to create a new schema within a new dedicated transaction.
        // We need to do this because we need to retry the transaction if the schema key already exists.
        //
        boolean commited = false;
        int commitRetryCount = 0;
        SchemaEntity schemaEntity = null;
        transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
        while (!commited && commitRetryCount < MAX_COMMIT_RETRY_COUNT) {
            String schemaKey = idGenerator.generate();
            try {
                //
                // Try to create a new schema within a new dedicated transaction.
                //
                schemaEntity = transactionTemplate.execute(tx -> {
                    newSchema.setCreatedAt(Instant.now());
                    newSchema.setKey(schemaKey);
                    schemaRepository.save(newSchema);
                    return newSchema;
                });
                logger.info("Successfully created a new schema '{}/{}' (key: '{}').", schemaName, domain, schemaKey);
                commited = true;
            } catch (DataIntegrityViolationException ex) {
                // There are two cases of data integrity failure -- either Key or the Schema name are not unique.
                // We need to find out who caused the failure.

                if (schemaRepository.findByKey(schemaKey) != null) {
                    // Our generated key already exists. We need to retry persisting our schema again.
                    commitRetryCount++;
                    logger.warn("Schema key collision. Retrying to create schema '{}/{}' (attempt: {}).",
                            domain,
                            schemaName,
                            commitRetryCount);
                } else if (schemaRepository.schemaExist(domainEntity, schemaName)) {
                    logger.error("Schema name collision. Schema '{}/{}' already exists.", domain, schemaName);

                    // If someone managed to persist a new schema with the same name -- we need to abort.
                    throw new SchemaAlreadyExistException("Schema '" + schemaName + "' already exists in domain '" + domain + "'.");
                } else {
                    logger.error("Unexpected error while trying to create schema.", ex);
                    throw new SchemaManagementException("Unexpected error while trying to create schema.", ex);
                }
            }
        }

        //
        // If our above loop reached the maximum number of retries, we failed to persist a new schema.
        //
        if (schemaEntity == null) {
            logger.error("Failed to create schema '{}/{}' (too many schema key collisions -- {} attempts).",
                    schemaName,
                    domain,
                    commitRetryCount);

            throw new SchemaManagementException("Failed to create schema for unexpected reason.");
        }

        return schemaEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = SERIALIZABLE)
    public SchemaDefinitionEntity addSchemaDefinition(String key, Migrations migrations) throws IllegalArgumentException, UnknownDomainException, UnknownSchemaException {
        Assert.notNull(migrations, "Migrations cannot be null.");

        try {
            //
            // Look up our schema and create a new transaction for it.
            //
            SchemaEntity schema = findSchema(key, true);
            logger.info("Adding new definition to schema '{}' (key: {}, current revision: {}).",
                    schema.getQualifiedName(),
                    key,
                    schema.getLatestRevision());

            SchemaDefinition schemaDefinition;
            if (schema.getLatestRevision() < 0) {
                logger.info("Creating a new empty schema definition for schema '{}' ({}).",
                        key,
                        schema.getQualifiedName());
                schemaDefinition = new SchemaDefinition();
            } else {
                logger.info("Fetching schema {} (key {}) revision {}.",
                        schema.getQualifiedName(),
                        key,
                        schema.getLatestRevision());

                // Fetch the existing baseline definition to migrate.
                SchemaDefinitionEntity baselineDefinition = schemaDefinitionRepository.findBySchemaAndRevision(schema,
                        schema.getLatestRevision());
                if (baselineDefinition == null) {
                    throw new MigrationException("Unexpected: Missing existing schema definition to migrate (revision %d)."
                            .formatted(schema.getLatestRevision()));
                }
                schemaDefinition = deserializeDefinition(baselineDefinition.getDefinition());
            }

            runMigrations(schemaDefinition, migrations);

            schema.incrementRevision();
            logger.info("Persisting a new schema revision no. {} (schema: {}, key {}).",
                    schema.getLatestRevision(),
                    schema.getQualifiedName(),
                    key);
            SchemaDefinitionEntity newDefinition = new SchemaDefinitionEntity(
                    null,
                    Instant.now(),
                    schema.getLatestRevision(),
                    schema,
                    serializeDefinition(schemaDefinition));

            schemaDefinitionRepository.save(newDefinition);
            schemaRepository.save(schema);

            return newDefinition;
        } catch (DataIntegrityViolationException | ConcurrencyFailureException ex) {
            logger.error("Failed to add schema definition to schema '{}'.", key, ex);
            throw new SchemaLockException("Schema already in use by another caller.", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SchemaEntity findSchema(String key)
            throws IllegalArgumentException, UnknownSchemaException, SchemaLockException {
        return findSchema(key, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SchemaEntity findSchema(String domainName, String schemaName)
            throws IllegalArgumentException, UnknownDomainException, UnknownSchemaException {
        Assert.notNull(schemaName, "Schema name cannot be null or empty.");
        DomainEntity domain = domainManagementService.findDomainByName(domainName);
        SchemaEntity entity = schemaRepository.findByName(domain, schemaName);
        if (entity == null) {
            throw new UnknownSchemaException("Unknown schema: '" + schemaName + "' (domain name: '" + domainName + "').");
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    protected SchemaEntity findSchema(String key, boolean lock)
            throws IllegalArgumentException, UnknownSchemaException, SchemaLockException {
        Assert.hasText(key, "Schema key cannot be null or empty.");

        try {
            SchemaEntity entity;

            if (lock) {
                entity = schemaRepository.findByKeyWithPessimisticWriteLock(key);
            } else {
                entity = schemaRepository.findByKey(key);
            }

            if (entity == null) {
                throw new UnknownSchemaException("Unknown schema key: '" + key + "'.");
            }
            return entity;
        } catch (ConcurrencyFailureException ex) {
            String message = "Schema (key #%s) could not acquire access.".formatted(key);

            logger.error(message, ex);
            throw new SchemaLockException(message, ex);
        }
    }

    /**
     * Runs a migration process on a given schema definition. The <i>baseline</i> definition may be {@code null} if
     * there are no previous definitions. In which case, the migrations simply define a new schema definition.
     *
     * @param definition Base schema to apply migration on.
     * @param migrations Migrations to apply.
     */
    protected void runMigrations(SchemaDefinition definition, Migrations migrations) {
        Assert.notNull(definition, "Definition cannot be null.");
        Assert.notNull(migrations, "Migrations cannot be null.");

        migrations.forEach(migration -> migration.applyMigration(definition));
    }

    /**
     * Validate that a given schema contains valid field definitions.
     *
     * @param schema Schema to validate.
     * @throws IllegalArgumentException If either <i>schema</i> is {@code null} or any of its fields are missing or
     *                                  invalid.
     */
    protected void validateSchemaDefinition(SchemaDefinition schema) throws IllegalArgumentException {
        Assert.notNull(schema, "Schema cannot be null.");
        for (FieldDefinition field : schema.getFields().values()) {
            Assert.hasText(field.getFieldName(), "Field name cannot be null or empty.");
            if (field.getFieldType() == null) {
                throw new IllegalArgumentException("Field '" + field.getFieldName() + "' type cannot be null.");
            }
        }
    }

    /**
     * <p>Register a new transaction for a given schema.
     * </p>
     * <p>The registration occurs in a dedicated transaction, so the registration is committed immediately.
     * </p>
     * If a transaction is already in place, a {@link SchemaLockException} is thrown.
     *
     * @param schemaEntity Schema to create transaction for.
     * @return Newly created transaction.
     * @throws SchemaLockException If a transaction is already in place for the given schema.
     */
    protected SchemaTransactionEntity createTransaction(SchemaEntity schemaEntity) throws SchemaLockException {
        final SchemaTransactionEntity transaction = new SchemaTransactionEntity();
        transaction.setSchema(schemaEntity);
        transaction.setCreatedAt(Instant.now());

        //
        // Create a new transaction entity and persist it (perform it in a dedicated transaction to take effect
        // immediately).
        //
        try {
            logger.info("Creating a new transaction for schema '{}'.", schemaEntity.getQualifiedName());
            SchemaTransactionEntity t = transactionTemplate.execute(tx -> schemaTransactionRepository.save(transaction));
            logger.info("Successfully created transaction for schema '{}'.", schemaEntity.getQualifiedName());
            return t;
        } catch (DataIntegrityViolationException ex) {
            // If we got a constraint violation exception, it means that there is already an existing transaction in
            // place.
            logger.error("Schema '{}' already locked in existing transaction.", schemaEntity.getQualifiedName(), ex);
            throw new SchemaLockException("Schema already locked in existing transaction.", ex);
        }
    }

    /**
     * Serialize a schema definition into JSON string.
     *
     * @param schemaDefinition Schema definition to serialize.
     * @return Serialized schema definition as JSON string.
     * @throws SchemaManagementException If, for some reason, the serialization failed (should not happen).
     */
    protected String serializeDefinition(SchemaDefinition schemaDefinition) throws SchemaManagementException {
        try {
            return OBJECT_MAPPER.writeValueAsString(schemaDefinition);
        } catch (JsonProcessingException ex) {
            throw new SchemaManagementException("Unexpected: Failed to serialize schema definition.", ex);
        }
    }

    /**
     * Deserialize a JSON into a schema definition.
     *
     * @param json JSON input to deserialize.
     * @return {@code SchemaDefinition} object.
     * @throws SchemaManagementException If, for some reason, the deserialization failed (should not happen).
     */
    protected SchemaDefinition deserializeDefinition(String json) throws SchemaManagementException {
        try {
            return OBJECT_MAPPER.readValue(json, SchemaDefinition.class);
        } catch (JsonProcessingException ex) {
            throw new SchemaManagementException("Unexpected: Failed to deserialize schema definition.", ex);
        }
    }

    /**
     * Creates a new ObjectMapper instance that serializes and deserialize schema definitions.
     *
     * @return A new mapper.
     */
    protected static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
