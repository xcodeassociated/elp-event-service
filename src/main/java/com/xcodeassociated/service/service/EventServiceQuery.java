package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventServiceQuery {
    Flux<EventDto> getAllEvents(Pageable pageable);
    Flux<EventWithCategoryDto> getAllEventsWithCategories(Pageable pageable);
    Flux<EventDto> getAllEventsByTitle(String title);
    Flux<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title);
    Flux<EventDto> getAllEventsCreatedBy(String user);
    Flux<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String user);
    Mono<EventDto> getEventById(String id);
    Mono<EventWithCategoryDto> getEventByIdWithCategories(String id);
    Mono<EventDto> getEventByUuid(String uuid);
    Mono<EventWithCategoryDto> getEventByUuidWithCategories(String uuid);
}
