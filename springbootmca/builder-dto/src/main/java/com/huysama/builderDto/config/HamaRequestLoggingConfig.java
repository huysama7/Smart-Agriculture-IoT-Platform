package com.huysama.builderDto.config;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class HamaRequestLoggingConfig {
    public CommonsRequestLoggingFilter logFilter(String instanceId) {
        System.out.println("Instance ID: " + instanceId);
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
