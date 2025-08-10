package com.huysama.authenticationService;


import com.huysama.builderDto.config.HamaRequestLoggingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.UUID;

@Configuration
public class MiddleWareConfig extends HamaRequestLoggingConfig {
    public static final String instance = UUID.randomUUID().toString();
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        System.out.println("Instance ID: " + instance);
        return super.logFilter(instance);
    }

}
