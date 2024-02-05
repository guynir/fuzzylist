package com.fuzzylist.validation;

import com.fuzzylist.ApplicationRuntimeException;

/**
 * This exception is raised when a property fails a constraint/validation. For example, if a list name is too short,
 * this exception is thrown.
 *
 * @author Guy Raz Nir
 * @since 2024/02/04
 */
public class PropertyConstraintsException extends ApplicationRuntimeException {

    public PropertyConstraintsException(String message) {
        super(message);
    }
}
