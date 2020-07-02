package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;

public interface EventCategoryServiceCommand {
    EventCategoryDto saveCategory(EventCategoryDto dto);
    void deleteCategoryById(String id);
}
