package com.fuzzylist.common;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Collection of assertion utils used across the application.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public abstract class Assertions {

    /**
     * Private class constructor - prohibits instantiation.
     */
    private Assertions() {
    }

    /**
     * Assert that neither <i>c</i> itself is none null, nor one of its items.
     *
     * @param c                      Collection to inspect/assert.
     * @param collectionEmptyMessage Error message in case <i>c</i> is {@code null}.
     * @param nullItemMessage        Error message in case one of the collection's item is {@code null}.
     * @param <T>                    Generic type of collection.
     * @throws IllegalArgumentException In case either the collection or one of its items are {@code null}.
     */
    public static <T> void assertNotNull(Collection<T> c, String collectionEmptyMessage, String nullItemMessage)
            throws IllegalArgumentException {
        Assert.notNull(c, collectionEmptyMessage);
        c.forEach(i -> Assert.notNull(i, nullItemMessage));
    }

    /**
     * Assert that neither <i>c</i> itself is none null or empty, nor one of its items.
     *
     * @param c                      Collection to inspect/assert.
     * @param collectionEmptyMessage Error message in case <i>c</i> is {@code null}.
     * @param nullItemMessage        Error message in case one of the collection's item is {@code null}.
     * @param <T>                    Generic type of collection.
     * @throws IllegalArgumentException In case either the collection or one of its items are {@code null}.
     */
    public static <T> void assertNotNullOrEmpty(Collection<T> c, String collectionEmptyMessage, String nullItemMessage)
            throws IllegalArgumentException {
        Assert.notNull(c, collectionEmptyMessage);
        Assert.notEmpty(c, collectionEmptyMessage);
        c.forEach(i -> Assert.notNull(i, nullItemMessage));
    }

    /**
     * Asserts that a given string is neither {@code null} nor empty (zero-length or contains nothing but spaces).
     *
     * @param st      String to examine.
     * @param message Error message.
     * @throws IllegalArgumentException If <i>st</i> is {@code null} or empty.
     */
    public static void assertNotEmpty(String st, String message) throws IllegalArgumentException {
        if (StringUtils.hasText(st)) {
            throw new IllegalArgumentException(message);
        }
    }

}
