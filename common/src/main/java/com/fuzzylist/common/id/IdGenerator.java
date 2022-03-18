package com.fuzzylist.common.id;

/**
 * Generate unique identifier for each call. Each underlying implementation may have its custom setup.
 *
 * @author Guy Raz Nir
 * @since 19/06/2016
 */
public interface IdGenerator {

    /**
     * @return Unique identifier for each call.
     */
    String generate();

}