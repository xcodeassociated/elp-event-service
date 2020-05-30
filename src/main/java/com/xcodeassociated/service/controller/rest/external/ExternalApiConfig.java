package com.xcodeassociated.service.controller.rest.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcodeassociated.commons.correlationid.http.retrofit.CorrelationIdInterceptor;
import com.xcodeassociated.commons.keycloak.AuthorizationInterceptor;
import com.xcodeassociated.commons.keycloak.OAuth2TokenAuthenticator;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class ExternalApiConfig {

    private final int externalServiceTimeoutSeconds;

    public ExternalApiConfig(@Value("${external.defaultTimeoutSeconds:60}") int externalServiceTimeoutSeconds) {
        this.externalServiceTimeoutSeconds = externalServiceTimeoutSeconds;
    }

    @Bean
    public ExternalServiceApi externalServiceApiBuilder(@Value("${external.service.api.url}") String url,
                                                ObjectMapper mapper,
                                                OAuth2TokenAuthenticator authenticator,
                                                AuthorizationInterceptor authorizationInterceptor,
                                                CorrelationIdInterceptor correlationIdInterceptor) {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .readTimeout(this.externalServiceTimeoutSeconds, TimeUnit.SECONDS)
                        .writeTimeout(this.externalServiceTimeoutSeconds, TimeUnit.SECONDS)
                        .connectTimeout(this.externalServiceTimeoutSeconds, TimeUnit.SECONDS)
                        .authenticator(authenticator)
                        .addInterceptor(authorizationInterceptor)
                        .addInterceptor(correlationIdInterceptor)
                        .build())
                .baseUrl(url)
                .build()
                .create(ExternalServiceApi.class);
    }

}
