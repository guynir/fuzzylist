package com.fuzzylist.services.schema.model.constraints;

import com.fuzzylist.services.schema.model.ConstraintContext;
import com.fuzzylist.services.schema.model.ConstraintType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a text length constraint, holding optional minimum and maximum length values.
 *
 * @author Guy Raz Nir
 * @since 2025/05/02
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TextLengthConstraint extends ConstraintContext {

    /**
     * Optional minimum length of the text.
     */
    private Integer minimumLength;

    /**
     * Optional maximum length of the text.
     */
    private Integer maximumLength;


    public TextLengthConstraint() {
        this(null, null);
    }

    /**
     * Class constructor.
     *
     * @param minimumLength Minimum length of the text.
     * @param maximumLength Maximum length of the text.
     */
    public TextLengthConstraint(Integer minimumLength, Integer maximumLength) {
        super(ConstraintType.TEXT_LENGTH);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }
}
