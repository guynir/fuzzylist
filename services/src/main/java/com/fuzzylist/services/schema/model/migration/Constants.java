package com.fuzzylist.services.schema.model.migration;

import java.util.regex.Pattern;

/**
 * Constant values used in the migration process.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public class Constants {

    /**
     * Definition of a field name -- a string with letters, digits and underscore -- between 1 and 64 characters long.
     */
    public static final Pattern FILED_NAME_EXPRESSION = Pattern.compile("^[a-zA-Z0-9_]{1,64}$");

    /**
     * Error message for the field's name that does not qualify as a valid field name.
     */
    public static final String FIELD_NAME_VALIDATION_MESSAGE =
            "Field name must be 1-64 characters long and contain only letters, digits and underscores.";

}
