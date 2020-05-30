package com.xcodeassociated.service.controller.kafka;

import com.xcodeassociated.events.model.KafkaEvent;

public interface KafkaProducerInterface {
    void publish(KafkaEvent message);
}
