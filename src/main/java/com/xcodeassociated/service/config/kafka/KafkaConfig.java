package com.xcodeassociated.service.config.kafka;

import com.xcodeassociated.commons.correlationid.base.ThreadCorrelationId;
import com.xcodeassociated.commons.correlationid.kafka.KafkaProducerInterceptor;
import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.service.config.kafka.interceptor.KafkaEventInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Profile("!test")
@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig implements KafkaListenerConfigurer {

    public static final KafkaEvent NULL_EVENT = new KafkaEvent();

    private final String bootstrapServers;
    private final boolean kafkaBatchEnabled;
    private final int kafkaBatchSize;
    private final int kafkaMaxPollIntervalMs;
    private final Integer producerRequestTimeout;
    private final LocalValidatorFactoryBean validator;

    public KafkaConfig(@Value("${kafka.bootstrap-servers}") String bootstrapServers,
                       @Value("${kafka.batch.enabled:false}") boolean kafkaBatchEnabled,
                       @Value("${kafka.batch.size:50}") int kafkaBatchSize,
                       @Value("${kafka.max-poll-interval-ms:300000}") int kafkaMaxPollIntervalMs,
                       @Value("${kafka.producer.request.timeout:1000}") Integer producerRequestTimeout,
                       LocalValidatorFactoryBean validator) {
        this.bootstrapServers = bootstrapServers;
        this.kafkaBatchEnabled = kafkaBatchEnabled;
        this.kafkaBatchSize = kafkaBatchSize;
        this.kafkaMaxPollIntervalMs = kafkaMaxPollIntervalMs;
        this.producerRequestTimeout = producerRequestTimeout;
        this.validator = validator;

        log.info("Kafka bootstrap servers: {}, batch enabled: {}, batch size: {}",
                bootstrapServers, kafkaBatchEnabled, kafkaBatchSize);
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, NonRetryingJsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        if (this.kafkaBatchEnabled) {
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.kafkaBatchSize);
        }
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, this.kafkaMaxPollIntervalMs);
        return props;
    }

    @Bean
    public ConsumerFactory<String, KafkaEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                this.consumerConfigs(),
                new StringDeserializer(),
                new NonRetryingJsonDeserializer<>(KafkaEvent.class, NULL_EVENT)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> kafkaListenerContainerFactory(KafkaEventInterceptor recordInterceptor) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.consumerFactory());
        factory.setBatchListener(this.kafkaBatchEnabled);
        factory.setRecordInterceptor(recordInterceptor);
        return factory;
    }

    private Map<String, Object> producerConfigs(ThreadCorrelationId threadCorrelationId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.producerRequestTimeout);
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());
        props.put(ThreadCorrelationId.class.getSimpleName(), threadCorrelationId);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(ThreadCorrelationId threadCorrelationId) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(threadCorrelationId));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar kafkaListenerEndpointRegistrar) {
        kafkaListenerEndpointRegistrar.setValidator(validator);
    }

}
