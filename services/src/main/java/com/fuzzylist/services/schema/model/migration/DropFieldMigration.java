package com.fuzzylist.services.schema.model.migration;

import com.fuzzylist.services.schema.model.SchemaDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import static com.fuzzylist.services.schema.model.migration.MigrationValidators.validateFieldName;

/**
 * A migration that describes a removal of a field from a schema.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public final class DropFieldMigration extends Migration {

    /**
     * The name of the field to be dropped.
     */
    private final String fieldName;


    /**
     * Validate the definition of this migration.
     *
     * @throws MigrationException If validation fails.
     */
    @Override
    public void validate() throws MigrationException {
        validateFieldName(fieldName);
    }

    /**
     * <p>Remove a field from a given schema definition.
     * </p>
     * Does nothing if the field does not exist.
     *
     * @param definition Definition of the schema to apply the migration on.
     */
    @Override
    public void applyMigration(SchemaDefinition definition) {
        Assert.notNull(definition, "Schema definition cannot be null.");
        definition.removeField(fieldName);
    }
}
