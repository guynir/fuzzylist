package com.fuzzylist.services.schema;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.common.id.SecureRandomIdGenerator;
import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import com.fuzzylist.repositories.UserRepository;
import com.fuzzylist.repositories.schema.DomainRepository;
import com.fuzzylist.repositories.schema.SchemaRepository;
import com.fuzzylist.services.domain.DomainManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * A unit test for service {@link SchemaManagementServiceImpl}.
 *
 * @author Guy Raz Nir
 * @since 2025/04/29
 */
@ExtendWith(MockitoExtension.class)
public class SchemaManagementServiceImplTest {

    // Mocked dependencies
    @Mock
    private IdGenerator idGenerator;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DomainRepository domainRepository;

    @Mock
    private SchemaRepository schemaRepository;

    @Spy
    private TransactionTemplate transactionTemplate = new TransactionTemplate();

    @Mock
    private DomainManagementService domainManagementService;

    @InjectMocks
    private SchemaManagementServiceImpl service;

    /**
     * Sample domain entity for testing.
     */
    private DomainEntity sampleDomain;

    /**
     * Sample schema entity for testing.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private SchemaEntity sampleSchema;

    /**
     * Sample domain id.
     */
    private static final Long DOMAIN_ID = 1L;

    /**
     * Sample schema id.
     */
    private static final Long SCHEMA_ID = 2L;

    /**
     * Sample domain name.
     */
    private static final String DOMAIN_NAME = "test-domain";

    /**
     * Sample schema name.
     */
    private static final String SCHEMA_NAME = "test-schema";

    /**
     * Sample key.
     */
    private static final String KEY = "000-0000-0000-0000";

    /**
     * Generates unique identifiers. Calls from {@link #idGenerator} are redirected to this implementation by default.
     */
    private final IdGenerator ID_GENERATOR = new SecureRandomIdGenerator();

    /**
     * Test fixture -- program mocks for default behavior for all tests.
     */
    @BeforeEach
    public void setUp() {
        transactionTemplate.setTransactionManager(transactionManager);

        sampleDomain = new DomainEntity(DOMAIN_ID, DOMAIN_NAME, DOMAIN_NAME.toLowerCase());
        sampleSchema = new SchemaEntity(SCHEMA_ID, KEY, sampleDomain, SCHEMA_NAME, Instant.now(), -1);

        lenient().when(domainManagementService.findDomainByName(eq(DOMAIN_NAME))).thenReturn(sampleDomain);

        // By default, idGenerator should generate unique identifiers.
        lenient().when(idGenerator.generate()).then(invocation -> ID_GENERATOR.generate());

        // By default, when looking up a sample domain, return 'sampleDomain'.
        lenient().when(domainRepository.findByDomainName(eq(DOMAIN_NAME))).thenReturn(sampleDomain);

        // By default, when lookup by our key, always return the existing schema.
        lenient().when(schemaRepository.findByKey(eq(KEY))).thenReturn(sampleSchema);
        lenient().when(schemaRepository.schemaExist(eq(sampleDomain), eq(SCHEMA_NAME))).thenReturn(true);
        lenient().when(schemaRepository.save(any())).thenAnswer(invocation -> {
            SchemaEntity entity = invocation.getArgument(0);
            if (entity.getDomain().equals(sampleDomain) && entity.getName().equals(SCHEMA_NAME)) {
                throw new DataIntegrityViolationException("Constraint violation on 'schema name' and 'domain name'.");
            } else if (entity.getKey().equals(KEY)) {
                throw new DataIntegrityViolationException("Constraint violation on 'key'.");
            } else {
                entity.setId(5L);
                return entity;
            }
        });
    }

    /**
     * Test should fail on schema name conflict (schema name already in use).
     */
    @Test
    @DisplayName("Test should fail reregistration of schema due to name conflict")
    public void testShouldFailSchemaCreationOnNameConflict() {
        assertThatThrownBy(() -> service.createSchema(DOMAIN_NAME, SCHEMA_NAME))
                .isInstanceOf(SchemaAlreadyExistException.class);
    }

    /**
     * Test should fail after trying to allocate a unique key for the schema and failing at least 10 times.
     */
    @Test
    @DisplayName("Test should fail due to unique key allocation failure")
    public void testShouldFailOnKeyAllocationFailure() {
        // IdGenerator should always return the same key for this test.
        // This should cause 'createSchema' to fail.
        when(idGenerator.generate()).thenReturn(KEY);

        // Test that 'createSchema' generates an exception after failing to allocate a unique key.
        assertThatThrownBy(() -> service.createSchema(DOMAIN_NAME, SCHEMA_NAME))
                .isInstanceOf(SchemaManagementException.class);

        // Make sure we tried to save the schema definition at least 10 retries.
        verify(schemaRepository, atLeast(10)).findByKey(eq(KEY));
    }
}
