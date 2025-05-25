package com.fuzzylist.services.domain;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.models.schema.DomainEntity;
import com.fuzzylist.repositories.schema.DomainRepository;
import com.fuzzylist.services.schema.UnknownDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DomainManagementServiceImpl implements DomainManagementService {

    /**
     * Provide access to the domain repository.
     */
    private final DomainRepository domainRepository;

    /**
     * Generate hash-based unit identifiers.
     */
    private final IdGenerator idGenerator;

    /**
     * Definition of a valid domain name.
     */
    private static final Pattern DOMAIN_NAME_REGEX = Pattern.compile("[\\w0-9a-zA-Z_-].*");

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DomainEntity registerDomain(String domainName) throws IllegalArgumentException, DomainAlreadyExistException {
        validateDomainName(domainName);

        DomainEntity domainEntity = new DomainEntity(null, domainName, domainName.toUpperCase());
        try {
            return domainRepository.save(domainEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DomainAlreadyExistException("Domain '" + domainName + "' already exists.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public boolean domainExists(String domainName) throws IllegalArgumentException {
        Assert.hasText(domainName, "Domain name cannot be null or empty.");
        return domainRepository.domainExist(domainName);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public DomainEntity findDomainByName(String domainName) throws IllegalArgumentException, UnknownDomainException {
        validateDomainName(domainName);

        DomainEntity entity = domainRepository.findByDomainName(domainName);
        if (entity == null) {
            throw new UnknownDomainException("Unknown domain name: '" + domainName + "'.");
        }

        return entity;
    }

    /**
     * Validate that a given domain name is valid. A valid domain name is one that:
     * <ul>
     *     <li>Non {@code null}.</li>
     *     <li>Contains only the following characters: 0-9, A-Z, a-z, underscore, hyphen or white space.</li>
     *     <li>Must not start or end with a white space.</li>
     * </ul>
     *
     * @param domainName Domain name to validate.
     * @throws IllegalArgumentException If <i>domainName</i> is non-valid name of domain.
     */
    protected void validateDomainName(String domainName) throws IllegalArgumentException {
        Assert.hasText(domainName, "Domain name cannot be null or empty.");

        if (!domainName.equals(domainName.trim())) {
            throw new IllegalArgumentException("Domain name cannot contain leading or trailing spaces.");
        }

        if (!DOMAIN_NAME_REGEX.matcher(domainName).matches()) {
            throw new IllegalArgumentException("Invalid domain name (can only contain letters, numbers, dashes, underscores and spaces).");
        }
    }


}

