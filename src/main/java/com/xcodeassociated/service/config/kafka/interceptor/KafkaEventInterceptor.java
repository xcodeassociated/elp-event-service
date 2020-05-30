package com.xcodeassociated.service.config.kafka.interceptor;

import com.xcodeassociated.commons.correlationid.base.ThreadCorrelationId;
import com.xcodeassociated.commons.correlationid.kafka.KafkaRecordInterceptor;
import com.xcodeassociated.events.model.KafkaEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventInterceptor extends KafkaRecordInterceptor<String, KafkaEvent> {

    public KafkaEventInterceptor(ThreadCorrelationId threadCorrelationId) {
        super(threadCorrelationId);
    }

}
