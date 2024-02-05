package com.fuzzylist.validation;

import com.fuzzylist.common.TemplateString;

/**
 * Abstract constraint validator -- provides base implementation for all other constraint validators.
 *
 * @param <T> Generic type of validated object.
 */
public abstract class ConstraintValidator<T, V extends ConstraintValidator<T, V>> {

    /**
     * Object to be validated/evaluated.
     */
    protected T value;

    /**
     * Class constructor.
     *
     * @param value Object to validate/evaluate.
     */
    protected ConstraintValidator(T value) {
        this.value = value;
    }

    public ConstraintValidator<T, V> notNull(String templateErrorMessage) throws PropertyConstraintsException {
        if (value == null) {
            throw new PropertyConstraintsException(templateErrorMessage);
        }

        return this;
    }

    /**
     * Creates a new template string and populate it with base values, such the value provided to the validator.
     *
     * @param template Template to use.
     * @return New template string.
     */
    protected TemplateString newRenderer(String template) {
        return TemplateString.create(template).with("value", value);
    }

}
