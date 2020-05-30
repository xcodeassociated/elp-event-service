package com.xcodeassociated.service.config.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicPaths implements com.xcodeassociated.commons.config.security.PublicPaths {

    @Override
    public List<String> getPaths() {
        return List.of("/api/v1/public/**");
    }

}
