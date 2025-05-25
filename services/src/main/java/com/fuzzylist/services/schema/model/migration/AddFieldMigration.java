package com.fuzzylist.services.schema.model.migration;

import com.fuzzylist.services.schema.model.FieldType;
import com.fuzzylist.services.schema.model.SchemaDefinition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import static com.fuzzylist.services.schema.model.migration.MigrationValidators.validateFieldName;

/**
 * Describe a migration step that adds a new field to a schema.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public final class AddFieldMigration extends Migration {

    /**
     * The name of the field to be added.
     */
    private final String fieldName;

    /**
     * The type of the field to be added.
     */
    private final FieldType fieldType;

    /**
     * Validate the definition of this migration.
     *
     * @throws MigrationException If validation fails.
     */
    @Override
    public void validate() throws MigrationException {
        validateFieldName(fieldName);
        MigrationValidators.notNull(fieldType, "Field type must not be null.");
    }

    /**
     * Apply a migration on a given schema definition.
     *
     * @param definition Definition of the schema to apply the migration on.
     * @throws MigrationException If migration fails.
     */
    @Override
    public void applyMigration(SchemaDefinition definition) throws MigrationException {
        Assert.notNull(definition, "Schema definition cannot be null.");

        if (definition.containsField(fieldName)) {
            throw new MigrationException(String.format("Cannot add field '%s' - it already exists.", fieldName));
        }

        definition.addField(fieldName, fieldType);
    }
}
