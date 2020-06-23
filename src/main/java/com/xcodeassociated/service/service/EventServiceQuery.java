package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventServiceQuery {
    Page<EventDto> getAllEvents(Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsWithCategories(Pageable pageable);
    Page<EventDto> getAllEventsByTitle(String title, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title, Pageable pageable);
    Page<EventDto> getAllEventsCreatedBy(String user, Pageable pageable);
    Page<EventDto> getAllEventsModifiedBy(String user, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String user, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsModifiedByWithCategories(String user, Pageable pageable);
    EventDto getEventById(String id);
    EventWithCategoryDto getEventByIdWithCategories(String id);
    EventDto getEventByUuid(String uuid);
    EventWithCategoryDto getEventByUuidWithCategories(String uuid);
    Page<EventDto> getAllEventsByQuery(EventSearchDto dto, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsByQueryWithCategories(EventSearchDto dto, Pageable pageable);
}
