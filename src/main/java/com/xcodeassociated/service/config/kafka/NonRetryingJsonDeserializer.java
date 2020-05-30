package com.xcodeassociated.service.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class NonRetryingJsonDeserializer<T> extends JsonDeserializer<T> {

    private final T nullObject;

    public NonRetryingJsonDeserializer(Class<T> targetType, T nullObject) {
        super(targetType, false);
        this.nullObject = nullObject;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            log.error("Failed to deserialize message for topic {}: {}", topic, new String(data, StandardCharsets.UTF_8));
            return nullObject;
        }
    }

}
