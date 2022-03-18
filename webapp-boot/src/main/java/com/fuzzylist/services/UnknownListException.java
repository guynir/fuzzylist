package com.fuzzylist.services;

import com.fuzzylist.ApplicationRuntimeException;

/**
 * This exception indicates a reference to a non-existing list.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
public class UnknownListException extends ApplicationRuntimeException {

    public UnknownListException() {
    }

    public UnknownListException(String message) {
        super(message);
    }
}
