package com.fuzzylist.services.schema;

import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import com.fuzzylist.repositories.schema.DomainRepository;
import com.fuzzylist.repositories.schema.SchemaDefinitionRepository;
import com.fuzzylist.repositories.schema.SchemaRepository;
import com.fuzzylist.services.AsyncExecutor;
import com.fuzzylist.services.domain.DomainManagementService;
import com.fuzzylist.services.domain.DomainManagementServiceImpl;
import com.fuzzylist.services.schema.model.migration.CustomMigration;
import com.fuzzylist.services.schema.model.migration.Migrations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

/**
 * <p>An integration test for service {@link SchemaManagementServiceImpl}. Works on top of an embedded database.
 * </p>
 * This intergation tests allows near-production level testing on {@code SchemaManagementService}.
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
@SuppressWarnings("ALL")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {JpaTestConfiguration.class})
@ComponentScan(basePackageClasses = {SchemaManagementServiceImpl.class, DomainManagementServiceImpl.class})
public class SchemaManagementServiceImplITest {

    @Autowired
    private SchemaManagementService service;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private SchemaDefinitionRepository schemaDefinitionRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DomainManagementService domainService;

    /**
     * Sample domain entity for testing.
     */
    private DomainEntity domain;

    /**
     * Async executor to execute concurrent database access tests.
     */
    private final AsyncExecutor executor = AsyncExecutor.newDefaultExecutor();

    /**
     * Class logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SchemaManagementServiceImplITest.class);

    /**
     * Sample domain name for testing.
     */
    private static final String DOMAIN_NAME = "MY_DOMAIN";

    /**
     * Sample schema name for testing.
     */
    private static final String SCHEMA_NAME = "MY_SCHEMA";

    /**
     * Test fixture -- perform database cleanup before each test and register a domain for testing.
     */
    @BeforeEach
    public void setUp() {
        // Before each test -- clean up the database.
        // Using @Rollback is ineffective in this case, since we are using a custom transaction manager.
        transactionTemplate.executeWithoutResult(status -> {
            schemaDefinitionRepository.deleteAll();
            schemaRepository.deleteAll();
            domainRepository.deleteAll();
        });

        // Register a sample domain for testing.
        domain = domainService.registerDomain(DOMAIN_NAME);
    }

    /**
     * Test creation of an empty schema.
     */
    @Test
    @DisplayName("Test should create an empty schema")
    public void testShouldCreateEmptySchema() {
        SchemaEntity schema = service.createSchema(DOMAIN_NAME, SCHEMA_NAME);

        // Assert that all key fields are well defined.
        assertThat(schema).isNotNull();
        assertThat(schema.getDomain()).isEqualTo(domain);
        assertThat(schema.getKey()).isNotNull();
        assertThat(schema.getCreatedAt()).isNotNull();
        assertThat(schema.getLatestRevision()).isEqualTo(-1);
        assertThat(schema.getName()).isEqualTo(SCHEMA_NAME);
    }

    /**
     * Test should create multiple revisions for a schema definition.
     */
    @Test
    @DisplayName("Test should create multiple revisions for schema definitions")
    public void testShouldCreateIncrementalSchemaDefinitions() {
        SchemaEntity schema = service.createSchema(DOMAIN_NAME, SCHEMA_NAME);
        final Migrations emptyMigration = new Migrations();

        // Register the first schema definition.
        service.addSchemaDefinition(schema.getKey(), emptyMigration);
        schema = service.findSchema(schema.getKey());
        assertThat(schema.getLatestRevision()).isEqualTo(0);

        // Migrate an existing schema definition.
        service.addSchemaDefinition(schema.getKey(), emptyMigration);
        schema = service.findSchema(schema.getKey());
        assertThat(schema.getLatestRevision()).isEqualTo(1);

        // Make sure we created 2 revisions for the schema definition.
        int numberOfRevisions = schemaDefinitionRepository.findByParent(schema).size();
        assertThat(numberOfRevisions).isEqualTo(2);
    }

    /**
     * Test should fail on concurrent schema definition update.
     */
    @Test
    @DisplayName("Test should fail on concurrent definition updates")
    public void testShouldFailOnConcurrentDefinitionUpdates() throws InterruptedException, ExecutionException {
        final SchemaEntity schema = service.createSchema(DOMAIN_NAME, SCHEMA_NAME);

        //
        // Execute a schema definition update in a separate thread, blocking it enough for main thread to fail.
        //
        final Object lock = new Object();
        Future<?> f = executor.executeAsync(() -> {
            // Run a custom migration that causes teh current thread to wait for the main thread to complete.
            // This way, we have a lock over the schema definition. The main thread should fail updating it.
            service.addSchemaDefinition(schema.getKey(), new Migrations(CustomMigration.of(d -> {
                // Notify the main thread that we are ready to run.
                synchronized (lock) {
                    lock.notifyAll();
                }

                // Wait for the main thread to complete its work and exit.
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // When interrupted -- runnable should complete its work and exit!
                        return;
                    }
                }
            })));
        });

        synchronized (lock) {
            // Wait for the other threa to complete setup and start updating the schema.
            lock.wait();

            // Try to run another migration in the main thread. It should fail.
            // We should get 'schema lock' error.
            assertThatExceptionOfType(SchemaLockException.class)
                    .isThrownBy(() -> service.addSchemaDefinition(schema.getKey(), new Migrations()));

            // Notify the other thread to exit.
            lock.notifyAll();
        }

        // Just to be safe, wait for the task to exit completely.
        f.get();
    }
}
