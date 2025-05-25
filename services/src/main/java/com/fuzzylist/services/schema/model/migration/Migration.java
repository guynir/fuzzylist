package com.fuzzylist.services.schema.model.migration;

import com.fuzzylist.services.schema.model.SchemaDefinition;

/**
 * Base class for all migrations.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public abstract sealed class Migration permits AddFieldMigration, DropFieldMigration, CustomMigration {

    /**
     * Validates the migration. Typically, validates validation data/state.
     *
     * @throws MigrationValidationException If migration validation failed.
     */
    public abstract void validate() throws MigrationValidationException;

    /**
     * Apply a migration on a given schema definition.
     *
     * @param definition Definition of the schema to apply the migration on.
     * @throws MigrationException If migration fails.
     */
    public abstract void applyMigration(SchemaDefinition definition) throws MigrationException;
}