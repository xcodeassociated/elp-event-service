package com.xcodeassociated.service.controller.rest.eventbus;

import com.xcodeassociated.service.eventbus.SampleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventBusPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(final String payload) {
        log.info("Publishing custom event with payload: {}", payload);
        SampleEvent event = new SampleEvent(this, payload);
        applicationEventPublisher.publishEvent(event);
    }
}