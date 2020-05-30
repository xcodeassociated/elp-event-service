package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.events.model.KafkaEvent;
import com.xcodeassociated.events.model.v1.SampleDataEventV1;
import com.xcodeassociated.events.service.KafkaEventFactory;
import com.xcodeassociated.service.controller.kafka.KafkaProducerInterface;
import com.xcodeassociated.service.controller.rest.dto.KafkaRestData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("!test")
@RestController
@RequestMapping("/event/api/v1/kafka")
@AllArgsConstructor
@Slf4j
public class KafkaRestV1 {

    private final KafkaProducerInterface kafkaProducer;

    @PostMapping("/publish")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> newOrder(@RequestBody KafkaRestData dto) {
        log.info("Processing `publish` request in KafkaRestV1");
        SampleDataEventV1 data = new SampleDataEventV1().toBuilder().payload(dto.getData()).build();
        KafkaEvent event = KafkaEventFactory.kafkaEvent(data, "service");
        kafkaProducer.publish(event);
        return ResponseEntity.ok().build();
    }
}
