package com.fuzzylist.configurations;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.common.id.SecureRandomIdGenerator;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.fuzzylist.repositories",
        queryLookupStrategy = QueryLookupStrategy.Key.USE_DECLARED_QUERY)
@ComponentScan("com.fuzzylist")
@PropertySources({
        @PropertySource("classpath:/defaults.properties"),
        @PropertySource(value = "classpath:/_local.properties", ignoreResourceNotFound = true)
})
public class ApplicationConfiguration {

    /**
     * Size, in characters, of identifiers assigned to list.
     */
    private static final int LIST_ID_LENGTH = 32;

    @Bean
    public IdGenerator listKeyGenerator() {
        return new SecureRandomIdGenerator(LIST_ID_LENGTH);
    }
}
