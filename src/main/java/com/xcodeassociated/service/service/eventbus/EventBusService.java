package com.xcodeassociated.service.service.eventbus;

import com.xcodeassociated.service.eventbus.SampleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventBusService implements ApplicationListener<SampleEvent> {

    @Override
    public void onApplicationEvent(SampleEvent event) {
        log.info("Processing eventbus event: {} in EventBusService", event);
        // ...
    }
}
