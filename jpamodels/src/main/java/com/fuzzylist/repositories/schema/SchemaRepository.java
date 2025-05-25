package com.fuzzylist.repositories.schema;

import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.models.schema.SchemaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for accessing {@link SchemaEntity}s.
 *
 * @author Guy Raz Nir
 * @since 2025/04/09
 */
@Repository
public interface SchemaRepository extends JpaRepository<SchemaEntity, Long> {

    /**
     * Look up a schema definition by its name.
     *
     * @param domainName Domain name to search schema under (case-insensitive).
     * @param schemaName Name of schema to look for (case-sensitive).
     * @return Schema entity matching query or {@code null} if no such query exists.
     */
    @Query("SELECT s FROM SchemaEntity s " +
            "WHERE s.domain.unifiedName = UPPER(:domainName) AND s.name = :schemaName")
    SchemaEntity findByName(String domainName, String schemaName);

    /**
     * Look up a schema by a given domain and a schema name.
     *
     * @param domain     Domain entity to look by.
     * @param schemaName Schema name to search by (case-sensitive).
     * @return Schema definition matching query or {@code null} if not such an entity exists.
     */
    @Query("SELECT s FROM SchemaEntity s WHERE s.domain = :domain AND s.name = :schemaName")
    SchemaEntity findByName(DomainEntity domain, String schemaName);

    /**
     * Look up a schema by its public key.
     *
     * @param key Key to look schema by.
     * @return Schema definition denoted by <i>key</i> or {@code null} if no such schema exists.
     */
    @Query("SELECT s FROM SchemaEntity s WHERE s.key = :key")
    SchemaEntity findByKey(String key);

    /**
     * Test if a given schema exists, denoted by domain and schema name.
     *
     * @param domainName Domain name to look under (case-insensitive).
     * @param schemaName Schema name to look for (case-sensitive).
     * @return {@code true} if schema exists, {@code false} if not.
     */
    @Query("SELECT CASE WHEN (COUNT(s) > 0) THEN true ELSE false END FROM SchemaEntity s " +
            "WHERE s.domain.unifiedName = UPPER(:domainName) AND s.name = :schemaName")
    boolean schemaExist(String domainName, String schemaName);

    /**
     * Test if a given schema exists, denoted by domain and schema name.
     *
     * @param domain     Domain to look by.
     * @param schemaName Schema name to look for (case-sensitive).
     * @return {@code true} if schema exists, {@code false} if not.
     */
    @Query("SELECT CASE WHEN (COUNT(s) > 0) THEN true ELSE false END FROM SchemaEntity s " +
            "WHERE s.domain = :domain AND s.name = :schemaName")
    boolean schemaExist(DomainEntity domain, String schemaName);

    /**
     * Fetch a schema definition by its public key, applying a pessimistic write lock on the record.
     *
     * @param key Key to look schema by.
     * @return Schema definition denoted by <i>key</i> or {@code null} if no such schema exists.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM SchemaEntity WHERE key = :key")
    SchemaEntity findByKeyWithPessimisticWriteLock(String key);

}
