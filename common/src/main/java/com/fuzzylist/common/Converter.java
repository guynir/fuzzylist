package com.fuzzylist.common;

import java.io.Serializable;

/**
 * <p>This interface defines a generic converter that serializes and deserializes objects from type S to an object of
 * type T and vise versa.</p>
 *
 * @param <S> Source type.
 * @param <D> Raw data format that can be persisted to transmitted over network.
 * @author Guy Raz Nir
 * @since 2005/05/20
 */
public interface Converter<S extends Serializable, D extends Serializable> {

    /**
     * Serialize an object of type S to an object of type T.
     *
     * @param object The source object to convert. May be {@code null}.
     * @return The converted object of type T. May return {@code null} if the source is {@code null}.
     */
    D serialize(S object);

    /**
     * Converts an object of type T to an object of type S.
     *
     * @param data Raw data to convert back to an object of type S.
     * @return Object of type S. May return {@code null} if the <i>data</i> is {@code null}.
     */
    S deserialize(D data);

}
