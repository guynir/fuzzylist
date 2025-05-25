package com.fuzzylist.services.schema;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

@Configuration
@EntityScan(basePackages = "com.fuzzylist")
@EnableJpaRepositories(basePackages = "com.fuzzylist", queryLookupStrategy = QueryLookupStrategy.Key.USE_DECLARED_QUERY)
public class ServicesConfiguration {
}
