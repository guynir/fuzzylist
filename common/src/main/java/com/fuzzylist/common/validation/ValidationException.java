package com.fuzzylist.common.validation;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * <p>A top-level validation exception.
 * </p>
 * Typically, all validation-related exception inherits from this one.
 *
 * @author Guy Raz Nir
 * @since 2025/04/13
 */
public class ValidationException extends RuntimeException {

    /**
     * Optional validation code.
     */
    private final String code;

    /**
     * <p>Class constructor.
     * </p>
     * Construct a new validation exception with no error code.
     *
     * @param message Error message.
     */
    public ValidationException(String message) {
        this(message, null);
    }

    /**
     * Class constructor.
     *
     * @param code    Optional validation code (maybe {@code null}).
     * @param message Error message.
     */
    public ValidationException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @return {@code true} if this exception has an error code, {@code false} if not.
     */
    public boolean hasCode() {
        return code != null;
    }

    /**
     * @return Error code or {@code null} if exception has no associated error code.
     */
    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ValidationException that)) return false;
        return Objects.equals(getMessage(), that.getMessage()) && Objects.equals(code, that.code);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", ValidationException.class.getSimpleName() + "[", "]")
                .add("message: " + getMessage());

        if (hasCode()) {
            joiner.add("code: " + getCode());
        }

        return joiner.toString();
    }
}
