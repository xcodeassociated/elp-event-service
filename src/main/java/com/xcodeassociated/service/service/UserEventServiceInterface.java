package com.xcodeassociated.service.service;

import com.xcodeassociated.events.model.KafkaEvent;

public interface UserEventServiceInterface {
    void handleKafkaEvent(KafkaEvent event);
}
