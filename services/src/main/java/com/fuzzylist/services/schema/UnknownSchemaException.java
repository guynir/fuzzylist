package com.fuzzylist.services.schema;

/**
 * This exception indicates a reference to a non-existing schema.
 *
 * @author Guy Raz Nir
 * @since 2025/04/25
 */
public class UnknownSchemaException extends SchemaManagementException {

    public UnknownSchemaException(String message) {
        super(message);
    }
}
