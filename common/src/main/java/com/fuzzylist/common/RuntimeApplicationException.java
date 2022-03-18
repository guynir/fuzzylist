package com.fuzzylist.common;

/**
 * Top-level exception for all other application exceptions.
 *
 * @author Guy Raz Nir
 * @since 2021/10/17
 */
public class RuntimeApplicationException extends RuntimeException {

    public RuntimeApplicationException() {
        super();
    }

    public RuntimeApplicationException(String message) {
        super(message);
    }

    public RuntimeApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeApplicationException(Throwable cause) {
        super(cause);
    }

    protected RuntimeApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
