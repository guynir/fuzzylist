package com.fuzzylist.services.schema;

/**
 * This exception indicates an attempt to reference a non-existing domain.
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
public class UnknownDomainException extends SchemaManagementException {

    public UnknownDomainException(String message) {
        super(message);
    }
}
