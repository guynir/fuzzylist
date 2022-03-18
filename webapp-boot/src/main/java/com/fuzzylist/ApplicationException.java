package com.fuzzylist;

/**
 * Top-level exception for all check-exception in this application.
 *
 * @author Guy Raz Nir
 * @since 2022/03/16
 */
public class ApplicationException extends Exception {

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
