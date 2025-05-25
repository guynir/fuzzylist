package com.fuzzylist.services.schema.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzzylist.services.schema.model.constraints.TextLengthConstraint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Test class for {@link ConstraintTypeResolver}.
 *
 * @author Guy Raz Nir
 * @since 2025/05/03
 */
public class ConstraintTypeResolverTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test that {@link ConstraintTypeResolver} can resolve the {@link ConstraintContext} to the right subclass.
     */
    @Test
    @DisplayName("Test should deserialize constraint context sub-class")
    public void testShouldDeserializeConstraintContextSubClass() throws JsonProcessingException {
        // Sample JSON that should deserialize into a TextLengthConstraint instance.
        final String json = """
                {
                    "constraintType": "TEXT_LENGTH",
                    "minimumLength": 5,
                    "maximumLength": 10
                }
                """;

        // Deserialize the JSON into a ConstraintContext object.
        ConstraintContext constraintContext = objectMapper.readValue(json, ConstraintContext.class);

        // Assert that we get the expected type of ConstraintContext.
        assertThat(constraintContext)
                .isNotNull()
                .isInstanceOf(TextLengthConstraint.class)
                .isEqualTo(new TextLengthConstraint(5, 10));


    }

    /**
     * Test that {@link ConstraintTypeResolver} can serialize the {@link ConstraintContext} subclass.
     */
    @Test
    @DisplayName("Test should serialize constraint context subclass")
    public void testShouldSerializeConstraintContextSubclass() throws JsonProcessingException {
        // Convert a TextLengthConstraint instance to a JSON string.
        TextLengthConstraint constraint = new TextLengthConstraint(5, 10);
        String generatedJson = objectMapper.writeValueAsString(constraint);

        // Deserialize the JSON string back to a Map for inspection.
        @SuppressWarnings("unchecked")
        Map<String, Object> map = objectMapper.readValue(generatedJson, Map.class);

        // Assert that the generated JSON contains the expected fields and values.
        assertThat(map)
                .hasSize(3)
                .containsEntry("constraintType", "TEXT_LENGTH")
                .containsEntry("minimumLength", 5)
                .containsEntry("maximumLength", 10);

    }
}
