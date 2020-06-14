package com.xcodeassociated.service.service;

import com.xcodeassociated.events.model.KafkaEvent;

public interface KafkaEventServiceInterface {
    void handleKafkaEvent(KafkaEvent event);
}
