package com.fuzzylist.services.schema.model;

public class SchemaDefinitionBuilder {

    private final SchemaDefinition schema = new SchemaDefinition();;

    /**
     * Private class constructor to prevent direct instantiation.
     */
    private SchemaDefinitionBuilder() {
    }

    public static SchemaDefinition create() {
        // Create a new SchemaDefinition object
        return new SchemaDefinition();
    }



    public SchemaDefinition get() {
        return schema;
    }
}
