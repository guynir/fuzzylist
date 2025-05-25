package com.fuzzylist.services.schema;

import com.fuzzylist.models.schema.SchemaDefinitionEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import com.fuzzylist.services.schema.model.migration.Migrations;

/**
 * <p>Interface responsible for managing schema operations for a database or similar storage systems.
 * This service typically provides methods to handle schema creation, updating, validation,
 * and migration tasks.
 * </p>
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
public interface SchemaManagementService {


    /**
     * Register a new schema.
     *
     * @param domain     Domain name to register the schema under. This field is case-insensitive.
     * @param schemaName Name of schema (case-sensitive).
     * @return The newly created schema.
     * @throws IllegalArgumentException    If either arguments are {@code null} or empty.
     * @throws UnknownDomainException      If given <i>domain</i> does not exist.
     * @throws SchemaAlreadyExistException If given schema name is already in use.
     */
    SchemaEntity createSchema(String domain, String schemaName)
            throws IllegalArgumentException, UnknownDomainException, SchemaAlreadyExistException;

    /**
     * Update an existing schema with a new schema definition.
     *
     * @param key        Schema's key to look by.
     * @param migrations Migration steps to apply on schema to achieve a new definition.
     * @return The updated schema.
     * @throws IllegalArgumentException If either arguments are {@code null} or empty.
     * @throws UnknownDomainException   If given <i>domain</i> does not exist.
     * @throws UnknownSchemaException   If the given schema does not exist.
     */
    SchemaDefinitionEntity addSchemaDefinition(String key, Migrations migrations)
            throws IllegalArgumentException, UnknownDomainException, UnknownSchemaException;

    /**
     * Look up a schema by public key.
     *
     * @param key Schema's key to look by.
     * @return Schema definition.
     * @throws IllegalArgumentException If <i>key</i> is {@code null} or empty.
     * @throws UnknownSchemaException   If the given schema does not exist.
     */
    SchemaEntity findSchema(String key)
            throws IllegalArgumentException, UnknownDomainException, UnknownSchemaException;

    /**
     * Look up a schema via its domain and name.
     *
     * @param domain     Domain name.
     * @param schemaName Schema name.
     * @return Schema definition.
     * @throws IllegalArgumentException If either arguments are {@code null} or empty.
     * @throws UnknownDomainException   If given <i>domain</i> does not exist.
     * @throws UnknownSchemaException   If the given schema does not exist.
     */
    SchemaEntity findSchema(String domain, String schemaName)
            throws IllegalArgumentException, UnknownDomainException, UnknownSchemaException;

}
