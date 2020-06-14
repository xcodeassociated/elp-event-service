package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.Event;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventRepository extends BaseRepository<Event, String> {
    Flux<Event> findAll(Sort sort);
    Mono<Event> findEventById(String id);
    Mono<Event> findEventByUuid(String uuid);
    Flux<Event> findByTitleContainingIgnoreCase(String title);
    Flux<Event> findAllByIdIn(List<String> ids);
}
