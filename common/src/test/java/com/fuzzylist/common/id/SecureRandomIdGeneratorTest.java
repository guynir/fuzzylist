package com.fuzzylist.common.id;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test suite for {@link SecureRandomIdGenerator}.
 *
 * @author Guy Raz Nir
 * @since 23/09/2019
 */
public class SecureRandomIdGeneratorTest {

    /**
     * Test that each call to {@link SecureRandomIdGenerator#generate()} generates a unique value.
     */
    @Test
    @DisplayName("Test should generate unique identifiers each call")
    public void testShouldGenerateUniqueIdentifiers() {
        // Generator under test.
        IdGenerator generator = new SecureRandomIdGenerator();

        // Number of times to generate unique identifiers.
        final int TIMES = 10000;

        // Collection that holds previously generated identifiers (to check an identifier does not repeat itself).
        Set<String> generatedIds = new HashSet<>(TIMES);

        for (int counter = 0; counter < TIMES; counter++) {
            // Generate new identifier. Ensure it was not previously generated.
            String id = generator.generate();
            boolean added = generatedIds.add(id);
            Assertions.assertThat(added).isTrue();
        }
    }

}