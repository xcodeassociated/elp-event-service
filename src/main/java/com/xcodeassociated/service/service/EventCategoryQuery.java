package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventCategoryQuery {
    Flux<EventCategoryDto> getAllCategories();
    Mono<EventCategoryDto> getEventCategoryById(String id);
    Flux<EventCategoryDto> getEventCategoryByIds(List<String> ids);
}
