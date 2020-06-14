package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.EventCategory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventCategoryRepository extends BaseRepository<EventCategory, String> {
    Flux<EventCategory> getAll();
    Mono<EventCategory> getEventCategoryById(String id);
    Flux<EventCategory> getEventCategoriesByIdIn(List<String> ids);
    Mono<EventCategory> save(EventCategory eventCategory);
    Mono<Void> deleteById(String id);
}
