package com.fuzzylist.validation;


import com.fuzzylist.common.TemplateString;
import org.springframework.util.StringUtils;

/**
 * A validator targeted for strings.
 *
 * @author Guy Raz Nir
 * @since 2024/02/04
 */
public class StringConstraintValidator extends ConstraintValidator<String, StringConstraintValidator> {

    /**
     * Class constructor.
     *
     * @param value Value to validate.
     */
    private StringConstraintValidator(String value) {
        super(value);
    }

    /**
     * Creates a new constraint validator.
     *
     * @param value Value to assign to validator.
     * @return New validator.
     */
    public static StringConstraintValidator newValidator(String value) {
        return new StringConstraintValidator(value);
    }

    /**
     * Validate that a given string has a minimum length.
     *
     * @param minLength Minimum length requirement.
     * @param error     Error message (template).
     * @return This instance.
     * @throws PropertyConstraintsException If current value's length is less than <i>minValue</i>.
     */
    public StringConstraintValidator minLength(int minLength, String error) throws PropertyConstraintsException {
        if (value.length() < minLength) {
            TemplateString ts = newRenderer(error).with("minLength", minLength);
            throw new PropertyConstraintsException(ts.format());
        }

        return this;
    }

    /**
     * Validate that a given string is no longer than given length.
     *
     * @param maxLength Maximum length requirement.
     * @param error     Error message (template).
     * @return This instance.
     * @throws PropertyConstraintsException If current value's length is more than <i>maxLength</i>.
     */
    public StringConstraintValidator maxLength(int maxLength, String error) throws PropertyConstraintsException {
        if (value.length() > maxLength) {
            TemplateString ts = newRenderer(error).with("maxLength", maxLength);
            throw new PropertyConstraintsException(ts.format());
        }

        return this;
    }

    /**
     * Make sure the current value is not empty.
     *
     * @param error Error message (template).
     * @return This instance.
     * @throws PropertyConstraintsException If current value is either {@code null}, 0-length or contains
     *                                      only white spaces.
     */
    public StringConstraintValidator notEmpty(String error) throws PropertyConstraintsException {
        if (!StringUtils.hasText(value)) {
            throw new PropertyConstraintsException(newRenderer(error).format());
        }

        return this;
    }
}
