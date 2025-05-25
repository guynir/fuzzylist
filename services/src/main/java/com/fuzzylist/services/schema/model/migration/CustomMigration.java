package com.fuzzylist.services.schema.model.migration;

import com.fuzzylist.services.schema.model.SchemaDefinition;

import java.util.function.Consumer;

/**
 * A custom migration step which is not directly related to a specific field.
 *
 * @author Guy Raz Nir
 * @since 2025/05/20
 */
public final class CustomMigration extends Migration {

    private final Runnable validator;
    private final Consumer<SchemaDefinition> migrator;

    /**
     * Class constructor.
     *
     * @param validator An optional callback to be issued during the validation stage. Nay be {@code null}.
     * @param migrator  An optional callback to be issued during the migration stage. May be {@code null}.
     */
    public CustomMigration(Runnable validator, Consumer<SchemaDefinition> migrator) {
        this.validator = validator;
        this.migrator = migrator;
    }

    /**
     * Creates a custom migration with a migration callback.
     *
     * @param migrator A callback to be issued during the migration stage. May be {@code null}.
     * @return A new instance of {@link CustomMigration}.
     */
    public static CustomMigration of(Consumer<SchemaDefinition> migrator) {
        return new CustomMigration(null, migrator);
    }

    @Override
    public void validate() throws MigrationValidationException {
        if (validator != null) {
            validator.run();
        }
    }

    @Override
    public void applyMigration(SchemaDefinition definition) throws MigrationException {
        if (migrator != null) {
            migrator.accept(definition);
        }
    }
}
