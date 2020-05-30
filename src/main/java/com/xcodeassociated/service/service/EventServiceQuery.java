package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventServiceQuery {
    Flux<EventDto> getAllEvents();
    Flux<EventDto> getAllEventsByTitle(String title);
    Flux<EventDto> getAllEventsCreatedBy(String user);
    Mono<EventDto> getEventById(String id);
    Mono<EventDto> getEventByUuid(String uuid);
}
