package com.xcodeassociated.service.service.kafka;

import com.xcodeassociated.events.model.KafkaEvent;

public interface KafkaEventServiceInterface {
    void handleKafkaEvent(KafkaEvent event);
}
