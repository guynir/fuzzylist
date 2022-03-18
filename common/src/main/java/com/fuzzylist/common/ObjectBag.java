package com.fuzzylist.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a state of object (collection of values of all fields).
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public class ObjectBag implements Serializable {

    /**
     * Collection of values representing state.
     */
    private final Object[] state;

    /**
     * Class constructor.
     *
     * @param state State.
     */
    public ObjectBag(Object... state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || o instanceof ObjectBag os && Arrays.equals(this.state, os.state);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }
}
