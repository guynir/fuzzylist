package com.fuzzylist.services.schemas;

import org.hibernate.tool.schema.spi.SchemaManagementException;

public interface SchemaManagementService {

    void createSchema(String domainName, String schemaName) throws SchemaManagementException;

}
