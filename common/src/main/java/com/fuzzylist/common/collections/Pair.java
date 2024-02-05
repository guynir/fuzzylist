package com.fuzzylist.common.collections;

/**
 * Represents a generic paired values, represented as key and value.
 *
 * @param <K> Generic type of key.
 * @param <V> Generic type of value.
 * @author Guy Raz Nir
 * @since 2022/01/18
 */
public class Pair<K, V> {

    /**
     * Key.
     */
    public K key;

    /* Value. */
    public V value;

    /**
     * Class constructor.
     */
    public Pair() {
    }

    /**
     * Class constructor.
     *
     * @param key   Initial key.
     * @param value Initial value.
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Helper method for easily creating new {@code Pair} instance.
     *
     * @param key   Key.
     * @param value Value.
     * @param <K>   Generic type of key.
     * @param <V>   Generic type of value.
     * @return New {@code Pair} instance.
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
}
