package com.fuzzylist.services.schema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;

/**
 * A java POJO that defines a schema.
 *
 * @author Guy Raz Nir
 * @since 2025/05/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaDefinition {

    /**
     * Holds fields definitions.
     */
    private LinkedHashMap<String, FieldDefinition> fields = new LinkedHashMap<>();

    /**
     * Add a newfield to this schema.
     *
     * @param fieldName Name of field.
     * @param fieldType Type of field.
     * @return This instance.
     * @throws IllegalArgumentException If either one of the arguments are {@code null}, if <i>fieldName</i> has no
     *                                  text.
     * @throws IllegalStateException    If the field already exists in this schema.
     */
    public SchemaDefinition addField(String fieldName, FieldType fieldType)
            throws IllegalArgumentException, IllegalStateException {
        Assert.hasText(fieldName, "Field name must be specified.");
        Assert.notNull(fieldType, "Field type must not be null.");

        if (fields.containsKey(fieldName)) {
            throw new IllegalStateException("Field name already exists: " + fieldName);
        }

        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setFieldName(fieldName);
        fieldDefinition.setFieldType(fieldType);
        fields.put(fieldName, fieldDefinition);

        return this;
    }

    /**
     * Look up a field by its name.
     *
     * @param fieldName Field name to look by.
     * @return Field definition.
     * @throws IllegalArgumentException If <i>fieldName</i> is {@code null}.
     * @throws IllegalStateException    If the field does not exist in this schema.
     */
    public FieldDefinition getField(String fieldName) throws IllegalArgumentException, IllegalStateException {
        Assert.notNull(fieldName, "Field name must not be null.");
        FieldDefinition definition = fields.get(fieldName);
        if (definition == null) {
            throw new IllegalStateException("Field does not exist: " + fieldName);
        }
        return definition;
    }

    /**
     * Test if a given field exists or not.
     *
     * @param fieldName Field name to test for.
     * @return {@code true} if field exists, {@code false} if not.
     */
    public boolean containsField(String fieldName) {
        return fields.containsKey(fieldName);
    }

    /**
     * Remove a field definition if it exists.
     *
     * @param fieldName Field name to remove.
     * @throws IllegalArgumentException If <i>fieldName</i> is {@code null}.
     */
    public void removeField(String fieldName) throws IllegalArgumentException {
        Assert.notNull(fieldName, "Field name must not be null.");
        fields.remove(fieldName);
    }
}
