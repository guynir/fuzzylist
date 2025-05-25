package com.fuzzylist.services.domain;

import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.services.schema.UnknownDomainException;

/**
 * Definition of business service that handles registration and lookup of domains.
 *
 * @author Guy Raz Nir
 * @since 2025/05/17
 */
public interface DomainManagementService {

    /**
     * Register a new domain.
     *
     * @param domainName Domain name to register.
     * @return Domain entity representing the domain.
     * @throws IllegalArgumentException    If <i>domainName</i> is either {@code null} or an empty string.
     * @throws DomainAlreadyExistException If the given domain name is already in use.
     */
    DomainEntity registerDomain(String domainName) throws IllegalArgumentException, DomainAlreadyExistException;

    /**
     * Test if a given domain name exists or not. This call does not test for domain name validity, it only tests for
     * existence in data store.
     *
     * @param domainName Domain name to test.
     * @return {@code true} if domain exists, {@code false} if not.
     * @throws IllegalArgumentException If <i>domainName</i> is {@code null} or empty.
     */
    boolean domainExists(String domainName) throws IllegalArgumentException;

    /**
     * Look up a domain by its name.
     *
     * @param domainName Can insensitive of the domain.
     * @return Domain entity.
     * @throws IllegalArgumentException If <i>domainName</i> is {@code null} or empty.
     * @throws UnknownDomainException   If such a domain does not exist.
     */
    DomainEntity findDomainByName(String domainName) throws IllegalArgumentException, UnknownDomainException;

}
