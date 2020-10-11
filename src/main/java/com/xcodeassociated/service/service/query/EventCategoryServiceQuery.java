package com.xcodeassociated.service.service.query;

import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventCategoryServiceQuery {
    Page<EventCategoryDto> getAllCategories(Pageable pageable);
    EventCategoryDto getEventCategoryById(String id);
    List<EventCategoryDto> getEventCategoryByIds(List<String> ids);
}
