package com.fuzzylist.services.schema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * <p>A constraint context holds the definitions of a constraint, such as limitations or rules parameters.
 * </p>
 * <p>For example, a length constraint may have a minimum and maximum length, while a regex constraint may have a
 * pattern.
 * </p>
 * Each constraint type has its own context, which is used to define the specific parameters for that constraint.
 *
 * @author Guy Raz Nir
 * @since 2025/05/02
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.PROPERTY,
        property = "constraintType")
@JsonTypeIdResolver(ConstraintTypeResolver.class)
@EqualsAndHashCode
public abstract class ConstraintContext {

    @Getter
    @JsonIgnore
    private final ConstraintType constraintType;

    protected ConstraintContext(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }
}
