package com.fuzzylist.services.schemas;

import java.util.List;

/**
 *
 */
public class FieldDefinition {

    public String name;

    public String type;

    public boolean optional;

    public List<Object> constraints;

    public List<Object> validators;

}
