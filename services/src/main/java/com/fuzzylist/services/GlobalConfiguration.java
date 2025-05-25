package com.fuzzylist.services;

import com.fuzzylist.common.id.IdGenerator;
import com.fuzzylist.common.id.SecureRandomIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Spring configuration.
 */
@Configuration
public class GlobalConfiguration {

    /**
     * @return New {@code IdGenerator} bean.
     */
    @Bean
    public IdGenerator idGenerator() {
        return new SecureRandomIdGenerator();
    }
}
