package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventCategoryQuery {
    Page<EventCategoryDto> getAllCategories(Pageable pageable);
    List<EventCategoryDto> getAllCategories();
    EventCategoryDto getEventCategoryById(String id);
    List<EventCategoryDto> getEventCategoryByIds(List<String> ids);
}
