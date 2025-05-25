package com.fuzzylist.services.schema;

import com.fuzzylist.common.ApplicationException;

/**
 * A top-level exception for all schema-related operations exceptions.
 *
 * @author Guy Raz Nir
 * @since 2025/04/24
 */
public class SchemaManagementException extends ApplicationException {

    public SchemaManagementException() {
    }

    public SchemaManagementException(String message) {
        super(message);
    }

    public SchemaManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaManagementException(Throwable cause) {
        super(cause);
    }

    public SchemaManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
