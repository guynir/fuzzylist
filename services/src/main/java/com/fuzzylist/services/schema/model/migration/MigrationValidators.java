package com.fuzzylist.services.schema.model.migration;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A collection of static methods for validating migrations' data.
 *
 * @author Guy Raz Nir
 * @since 2025/05/19
 */
public class MigrationValidators {

    /**
     * Validate that a given text is not null and contains non-whitespace characters at a given <i>minimumLength</i>.
     *
     * @param text         Text to validate.
     * @param minLength    Minimum length of the text.
     * @param trimmed      If true, the text will be trimmed before validation (leading/trailing spaces are removed).
     * @param errorMessage Error message, in case of validation failure.
     * @throws MigrationValidationException If validation failed.
     */
    public static void minimumTextLength(String text, int minLength, boolean trimmed, String errorMessage)
            throws MigrationValidationException {
        Assert.hasText(errorMessage, "Error message cannot be null or empty.");

        if (!StringUtils.hasText(text)) {
            throw new MigrationValidationException(errorMessage);
        }

        if (trimmed) {
            text = text.trim();
        }

        if (text.length() < minLength) {
            throw new MigrationValidationException(errorMessage);
        }
    }

    /**
     * Validate that a given text matches the given regular expression.
     *
     * @param text         Text to validate.
     * @param expression   Regular expression to match against.
     * @param errorMessage Error message, in case of validation failure.
     * @throws MigrationValidationException If validation failed.
     */
    public static void expressionMatch(String text, Pattern expression, String errorMessage)
            throws MigrationValidationException {
        Assert.notNull(expression, "Expression cannot be null.");
        Assert.notNull(errorMessage, "Error message cannot be null.");

        if (text != null && !expression.matcher(text).matches()) {
            throw new MigrationValidationException(errorMessage);
        }
    }

    /**
     * Validate that a given text matches the given regular expression.
     *
     * @param text         Text to validate.
     * @param expression   Regular expression to match against.
     * @param errorMessage Error message, in case of validation failure.
     * @throws MigrationValidationException If validation failed.
     * @throws PatternSyntaxException       In case the given expression is not a valid regular expression.
     */
    public static void expressionMatch(String text, String expression, String errorMessage)
            throws MigrationValidationException, PatternSyntaxException {
        expressionMatch(text, expression != null ? Pattern.compile(expression) : null, errorMessage);
    }


    /**
     * Validate that a given object is not {@code null}.
     *
     * @param value        Value to validate.
     * @param errorMessage Error message, in case of validation failure.
     * @throws MigrationValidationException If validation failed.
     */
    public static void notNull(Object value, String errorMessage) throws MigrationValidationException {
        Assert.notNull(errorMessage, "Error message cannot be null.");

        if (value == null) {
            throw new MigrationValidationException(errorMessage);
        }
    }

    /**
     * Validate that a given object is one of <i>allowedValues</i>.
     *
     * @param value         Value to test.
     * @param allowedValues Collection of allowed values.
     * @param errorMessage  Error message in case of validation failure.
     * @param <T>           Generic type of value and allowed values.
     * @throws MigrationValidationException If validation failed.
     */
    public static <T> void oneOf(T value, Collection<T> allowedValues, String errorMessage)
            throws MigrationValidationException {
        Assert.notNull(allowedValues, "Allowed values cannot be null.");
        Assert.notNull(errorMessage, "Error message cannot be null.");

        if (!allowedValues.contains(value)) {
            throw new MigrationValidationException(errorMessage);
        }
    }

    /**
     * <p>Validate that a given text is a valid field name.
     * </p>
     * Since this validation is common to many migration types, it is declared here for common use.
     *
     * @param fieldName Field name to validate.
     * @throws MigrationValidationException If validation failed.
     */
    public static void validateFieldName(String fieldName) throws MigrationValidationException {
        expressionMatch(fieldName,
                Constants.FILED_NAME_EXPRESSION,
                Constants.FIELD_NAME_VALIDATION_MESSAGE);
    }
}
