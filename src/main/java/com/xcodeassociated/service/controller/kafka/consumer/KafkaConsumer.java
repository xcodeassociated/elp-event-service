package com.xcodeassociated.service.controller.kafka.consumer;

import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.service.controller.kafka.KafkaConsumerInterface;
import com.xcodeassociated.service.exception.ValidationException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.service.KafkaEventServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Profile("!test")
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer implements KafkaConsumerInterface {

    private final KafkaEventServiceInterface eventService;

    @Override
    @KafkaListener(
            id = "${kafka.consumer-group-id}",
            topics = {"${kafka.consumer.topic}"},
            errorHandler = "validationErrorHandler"
    )
    public void onDataEvent(@Valid @Payload KafkaEvent event,
                                @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                                @Header(KafkaHeaders.OFFSET) Integer offset) {
        log.info("Received form partition: {}, offset: {},  event: {}", partition, offset, event);
        this.eventService.handleKafkaEvent(event);
    }

    @Bean
    public KafkaListenerErrorHandler validationErrorHandler() {
        return (m, e) -> {
            String msg = e.getCause().getMessage();
            log.error("Kafka message validation failed: {}", msg);
            throw new ValidationException(ErrorCode.V000, msg);
        };
    }

}
