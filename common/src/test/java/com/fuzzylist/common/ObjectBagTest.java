package com.fuzzylist.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ObjectBag}.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public class ObjectBagTest {

    /**
     * Test that two states with primitive values are equal.
     */
    @Test
    @DisplayName("Test should detect equality between equal states.")
    public void testShouldResultInEquality() {
        ObjectBag os1 = new ObjectBag(null, "Hi");
        ObjectBag os2 = new ObjectBag(null, "Hi");

        Assertions.assertThat(os1).isEqualTo(os2);
    }

    /**
     * Test that two different states with primitive values are not equal.
     */
    @Test
    @DisplayName("Test should detect inequality between unequal states.")
    public void testShouldResultInInequality() {
        ObjectBag os1 = new ObjectBag(null, "Hi");
        ObjectBag os2 = new ObjectBag(null, "Goodbye");

        Assertions.assertThat(os1).isNotEqualTo(os2);
    }

}
