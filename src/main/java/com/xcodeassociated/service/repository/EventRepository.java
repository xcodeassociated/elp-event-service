package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository extends BaseRepository<Event, String> {
    Flux<Event> findAll();
    Mono<Event> findEventById(String id);
    Mono<Event> findEventByUuid(String uuid);
    Flux<Event> findByTitleContainingIgnoreCase(String title);
}
