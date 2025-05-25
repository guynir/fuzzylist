package com.fuzzylist.services.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simple value holder class. Hold a value of type T.
 *
 * @param <T> Generic type of the value.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValueHolder<T> {

    /**
     * The value held by this holder.
     */
    private T value;

    /**
     * @return {@code true} if the value is set, {@code false} otherwise (if the value is {@code null}).
     */
    public boolean isSet() {
        return value != null;
    }

}
