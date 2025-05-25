package com.fuzzylist.services.schema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>This class represents a field definition. It defines core properties, such as its name, type and default value.
 * </p>
 * A field definition also includes two types of validation tools:
 * <ul>
 *     <li>
 *         <i>constraints</i> - Define a set of constraints that can be applied before, during and after user input.
 *         (for example, minimum length of a text field, numbers-only text field).
 *     </li>
 *     <li>
 *         <i>validators</i> - Define a set of validators that are executed post-user input to ensure the value is valid
 *         (for example, regular expression to make sure a field matches a desired format).
 *     </li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinition {

    /**
     * Name of field.
     */
    private String fieldName;

    /**
     * Type of field.
     */
    private FieldType fieldType;

    /**
     * Default value to assign to the field. If {@code null} then this field has no default value.
     */
    private String defaultValue;

}
