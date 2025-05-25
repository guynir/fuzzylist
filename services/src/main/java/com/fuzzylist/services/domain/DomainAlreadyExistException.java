package com.fuzzylist.services.domain;

import com.fuzzylist.services.schema.SchemaManagementException;

/**
 * This exception indicates an attempt to register a domain with a name already in use.
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
public class DomainAlreadyExistException extends SchemaManagementException {

    public DomainAlreadyExistException(String message) {
        super(message);
    }
}
