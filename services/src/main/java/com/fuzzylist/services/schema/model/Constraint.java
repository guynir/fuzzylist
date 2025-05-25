package com.fuzzylist.services.schema.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * <p>A container for a constraint.
 * </p>
 * A constraint typically defines a single rule that can be applied before or during user input.
 *
 * @author Guy Raz Nir
 * @since 2025/05/01
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class Constraint {

    /**
     * The type of constraint.
     */
    @Getter
    private final ConstraintType constraintType;

    /**
     * The constraint definition.
     */
    @Getter
    private final ConstraintContext constraintContext;

    /**
     * Constructor.
     *
     * @param constraintType    Type of constraint.
     * @param constraintContext Context of constraint.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    protected Constraint(ConstraintType constraintType, ConstraintContext constraintContext)
            throws IllegalArgumentException {
        Assert.notNull(constraintType, "Constraint type cannot be null.");
        Assert.notNull(constraintContext, "Constraint context cannot ben null.");

        this.constraintType = constraintType;
        this.constraintContext = constraintContext;
    }
}
