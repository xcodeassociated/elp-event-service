package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import reactor.core.publisher.Mono;

public interface EventServiceCommand {
    Mono<EventDto> createEvent(EventDto dto);
    Mono<EventDto> updateEvent(EventDto dto);
    Mono<Void> deleteEvent(String id);
}
