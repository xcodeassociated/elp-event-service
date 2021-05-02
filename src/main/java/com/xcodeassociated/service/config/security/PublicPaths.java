package com.xcodeassociated.service.config.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicPaths implements com.xcodeassociated.commons.config.security.PublicPaths {

    @Override
    public List<String> getPaths() {
        return List.of(
                "/api/v1/public/**",
                "/event/api/v1/public/**",
                "/event/v2/api-docs/**",
                "/event/v3/api-docs/**",
                "/event/api-docs/**",
                "/event/swagger-ui.html",
                "/event/swagger-ui/**",
                "/event/swagger-resources/**",
                "/event/webjars/**",
                "/event/configuration/**",
                "/event/favicon.ico",
                "/event/error",
                "/event/actuator/**"
        );
    }

}
