package com.fuzzylist.services.schema;

/**
 * This exception indicates that a given schema name cannot be reused when creating a new schema.
 *
 * @author Guy Raz Nir
 * @since 2025/04/25
 */
public class SchemaAlreadyExistException extends SchemaManagementException {

    public SchemaAlreadyExistException(String message) {
        super(message);
    }
}
