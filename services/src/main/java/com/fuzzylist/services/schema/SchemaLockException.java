package com.fuzzylist.services.schema;

/**
 * Indicates that a schema lock operation failed due to prior acquisition of the lock.
 *
 * @author Guy Raz Nir
 * @since 2025/05/07
 */
public class SchemaLockException extends SchemaManagementException {

    public SchemaLockException(String message) {
        super(message);
    }

    public SchemaLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
