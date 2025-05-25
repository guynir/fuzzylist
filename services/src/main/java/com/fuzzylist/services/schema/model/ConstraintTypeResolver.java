package com.fuzzylist.services.schema.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

public class ConstraintTypeResolver extends TypeIdResolverBase {

    public ConstraintTypeResolver() {
        super();
    }

    public ConstraintTypeResolver(JavaType baseType, TypeFactory typeFactory) {
        super(baseType, typeFactory);
    }

    /**
     * Extract an identifier for a given type.
     *
     * @param value An object which is a subtype of type {@link ConstraintContext}.
     * @return A string identifier for the type of the object.
     */
    @Override
    public String idFromValue(Object value) {
        if (value instanceof ConstraintContext constraintContext) {
            return constraintContext.getConstraintType().name();
        } else {
            throw new IllegalStateException("Expected a ConstraintContext instance, but got: " + value.getClass().getName());
        }
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return suggestedType.getName();
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        ConstraintType constraintType = ConstraintType.valueOf(id);
        Class<? extends ConstraintContext> contextClass = ConstraintResolver.resolveConstraintContext(constraintType);

        return context.getTypeFactory().constructType(contextClass);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
