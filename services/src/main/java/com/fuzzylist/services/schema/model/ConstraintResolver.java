package com.fuzzylist.services.schema.model;

import com.fuzzylist.services.schema.model.constraints.TextLengthConstraint;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * A utility class to resolve constraint types to their corresponding context classes.
 *
 * @author Guy Raz Nir
 * @since 2025/05/02
 */
public class ConstraintResolver {

    /**
     * A map of constraint types to their corresponding context classes.
     */
    private static final Map<ConstraintType, Class<? extends ConstraintContext>> CONSTRAINTS_TYPE_TO_CONTEXT_CLASS_MAP;
    private static final Map<Class<? extends ConstraintContext>, ConstraintType> CONSTRAINTS_CLASS_TO_CONTEXT_TYPE_MAP;

    static {
        CONSTRAINTS_TYPE_TO_CONTEXT_CLASS_MAP = Map.of(
                ConstraintType.TEXT_LENGTH, TextLengthConstraint.class
        );

        CONSTRAINTS_CLASS_TO_CONTEXT_TYPE_MAP = Map.of(
                TextLengthConstraint.class, ConstraintType.TEXT_LENGTH
        );
    }

    /**
     * Resolve a constraint type to its corresponding context class.
     *
     * @param constraintType Constraint type to resolve.
     * @param <T>            Generic type of the constraint context.
     * @return The class of the constraint context.
     * @throws IllegalArgumentException If either <i>constraintType</i> is {@code null}.
     */
    public static <T extends Class<? extends ConstraintContext>> T resolveConstraintContext(ConstraintType constraintType)
            throws IllegalArgumentException {
        Assert.notNull(constraintType, "Constraint type cannot be null.");

        @SuppressWarnings("unchecked")
        T context = (T) CONSTRAINTS_TYPE_TO_CONTEXT_CLASS_MAP.get(constraintType);

        // Context should never ben null, as all constraint types should have a mapped context.
        // However, this is a guard in case a new constraint type is added without a context.
        if (context == null)
            throw new IllegalArgumentException("Constraint type not supported: " + constraintType);

        return context;
    }

    /**
     * Resolve a constraint context class to its corresponding constraint type.
     *
     * @param clazz Constraint context class to resolve.
     * @return The constraint type.
     * @throws IllegalArgumentException If <i>clazz</i> is {@code null}.
     */
    public static ConstraintType resolveConstraintType(Class<? extends ConstraintContext> clazz)
            throws IllegalArgumentException {
        Assert.notNull(clazz, "Constraint context type cannot be null.");

        ConstraintType contextType = CONSTRAINTS_CLASS_TO_CONTEXT_TYPE_MAP.get(clazz);

        // Context type should never ben null, as all constraint types should have a mapped context.
        // However, this is a guard in case a new constraint type is added without a context.
        if (contextType == null)
            throw new IllegalArgumentException("Constraint context not supported: " + clazz.getName());

        return contextType;
    }

}
