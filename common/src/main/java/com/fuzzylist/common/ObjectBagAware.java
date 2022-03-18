package com.fuzzylist.common;

import java.io.Serializable;

/**
 * Data objects inheriting from this class accept an optimized trait of {@link Object#equals(Object)} and
 * {@link Object#hashCode()}.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public abstract class ObjectBagAware implements Serializable {

    /**
     * @return The state of an object (collection of fields values) as an object bag.
     */
    protected abstract ObjectBag state();

    @Override
    public int hashCode() {
        return state().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof ObjectBagAware oba && oba.state().equals(state()));
    }
}
