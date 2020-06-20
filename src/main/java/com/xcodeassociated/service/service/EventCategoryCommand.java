package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;

public interface EventCategoryCommand {
    EventCategoryDto saveCategory(EventCategoryDto dto);
    void deleteCategoryById(String id);
}
