package com.fuzzylist.services.schema.model.migration;

import org.hibernate.tool.schema.spi.SchemaManagementException;

/**
 * A common exception for all migration-related errors.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public class MigrationException extends SchemaManagementException {

    public MigrationException(String message) {
        super(message);
    }

    public MigrationException(String message, Throwable root) {
        super(message, root);
    }
}
