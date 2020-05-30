package com.xcodeassociated.service.config.mongo;

import com.xcodeassociated.commons.config.audit.AuditorAwareImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
@RequiredArgsConstructor
public class AuditConfig {

    @Bean
    AuditorAware<String> auditorProviderMongo() {
        return new AuditorAwareImpl();
    }

}
