package com.fuzzylist.services.schema.model.migration;

/**
 * An exception thrown during the validation phase of a migration.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public class MigrationValidationException extends MigrationException {

    public MigrationValidationException(String message) {
        super(message);
    }
}
