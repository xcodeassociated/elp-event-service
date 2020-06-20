package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;

import java.util.List;

public interface EventCategoryQuery {
    List<EventCategoryDto> getAllCategories();
    EventCategoryDto getEventCategoryById(String id);
    List<EventCategoryDto> getEventCategoryByIds(List<String> ids);
}
