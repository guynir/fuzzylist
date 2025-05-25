package com.fuzzylist.repositories.schema;

import com.fuzzylist.models.schema.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * JPA repository for accessing {@link DomainEntity}s.
 *
 * @author Guy Raz Nir
 * @since 2025/04/09
 */
public interface DomainRepository extends JpaRepository<DomainEntity, Integer> {

    /**
     * Lookup a domain name by its name (case-insensitive form).
     *
     * @param domainName Domain name to look by.
     * @return Domain matching given name or {@code null} if not such domain exist.
     */
    @Query("SELECT d FROM DomainEntity d WHERE d.unifiedName = UPPER(:domainName)")
    DomainEntity findByDomainName(String domainName);

    /**
     * Test if a given domain exists or not.
     *
     * @param domainName Domain name.
     * @return {@code true} if domain name exists, {@code false} if not.
     */
    @Query("SELECT CASE WHEN (COUNT(d) > 0) THEN true ELSE false END FROM DomainEntity d WHERE d.unifiedName = UPPER(:domainName)")
    boolean domainExist(String domainName);
}
