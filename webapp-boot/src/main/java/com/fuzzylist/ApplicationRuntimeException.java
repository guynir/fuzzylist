package com.fuzzylist;

/**
 * Top-level exception for all runtime exceptions in the application.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException() {
    }

    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(Throwable cause) {
        super(cause);
    }

    public ApplicationRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
