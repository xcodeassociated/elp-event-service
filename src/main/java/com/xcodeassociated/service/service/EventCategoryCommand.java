package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import reactor.core.publisher.Mono;

public interface EventCategoryCommand {
    Mono<EventCategoryDto> saveCategory(EventCategoryDto dto);
    Mono<Void> deleteCategoryById(String id);
}
