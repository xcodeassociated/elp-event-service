package com.xcodeassociated.service.config.redis;

import com.xcodeassociated.service.model.cache.EventCounter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Profile("!test")
@Configuration
public class RedisConfig {
    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(@Value("${spring.redis.host}") String host,
                                                                         @Value("${spring.redis.port}") Integer port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, EventCounter> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer<EventCounter> valueSerializer =
                new Jackson2JsonRedisSerializer<>(EventCounter.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, EventCounter> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, EventCounter> context =
                builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
