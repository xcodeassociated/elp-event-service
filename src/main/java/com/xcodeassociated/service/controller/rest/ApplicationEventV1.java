package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.controller.rest.eventbus.EventBusPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event/api/v1/eventbus")
public class ApplicationEventV1 {
    private final EventBusPublisher eventBusPublisher;

    @PostMapping("/publish")
    ResponseEntity<Void> publish(@RequestBody String payload) {
        log.info("Processing `trigger` request in ApplicationEventV1 with payload: {}", payload);
        eventBusPublisher.publish(payload);

        log.info("Event published...");
        return ResponseEntity.ok().build();
    }
}
