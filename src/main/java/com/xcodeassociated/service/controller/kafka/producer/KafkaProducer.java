package com.xcodeassociated.service.controller.kafka.producer;

import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.service.controller.kafka.KafkaProducerInterface;
import com.xcodeassociated.service.exception.KafkaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Profile("!test")
@Component
@Slf4j
public class KafkaProducer implements KafkaProducerInterface {

    private final String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Integer producerRequestTimeout;

    public KafkaProducer(@Value("${kafka.producer.topic}") String topic,
                         @Value("${kafka.producer.request.timeout:1000}") Integer producerRequestTimeout,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.producerRequestTimeout = producerRequestTimeout;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(KafkaEvent message) {
        try {
            log.info("Publishing message {} to topic {}", message, this.topic);
            kafkaTemplate.send(this.topic, "", message).get(this.producerRequestTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to publish message {} to topic {}", message, this.topic, e);
            throw new KafkaException(e.getMessage());
        }
    }

}
