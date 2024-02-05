package com.fuzzylist.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link TemplateString}.
 *
 * @author Guy Raz Nir
 * @since 2024/02/05
 */
public class TemplateStringTest {

    /**
     * Test should generate a message based on a given template and parameter.
     */
    @Test
    @DisplayName("Test should generate message from template input and parameters")
    public void testShouldGenerateMessageFromTemplate() {
        String message = TemplateString
                .create("My name is ${name}.")
                .with("name", "Guy")
                .format();

        Assertions.assertThat(message).isEqualTo("My name is Guy.");
    }

    /**
     * When provided template is empty or unspecified, the renderer should return {@code null} and not throw any
     * exception.
     */
    @Test
    @DisplayName("Test should return null")
    public void testShouldReturnNull() {
        String nullMessage = TemplateString.create().format();
        Assertions.assertThat(nullMessage).isNull();
    }
}
