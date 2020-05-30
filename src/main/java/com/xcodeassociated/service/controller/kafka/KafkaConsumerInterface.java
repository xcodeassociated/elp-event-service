package com.xcodeassociated.service.controller.kafka;

import com.xcodeassociated.events.model.KafkaEvent;

public interface KafkaConsumerInterface {
    void onDataEvent(KafkaEvent event, Integer partition, Integer offset);
}
