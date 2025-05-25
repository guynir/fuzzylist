package com.fuzzylist.common.validation;

/**
 * A definition of validator.
 *
 * @param <T>
 * @author Guy Raz Nir
 * @since 2025/04/13
 */
public interface Validator<T> {

    /**
     * Validate a given object's state.
     *
     * @param value Value to validate.
     */
    void validate(T value);
}
