package com.xcodeassociated.service.service.command;

import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;

public interface EventCategoryServiceCommand {
    EventCategoryDto saveCategory(EventCategoryDto dto);
    void deleteCategoryById(String id);
}
