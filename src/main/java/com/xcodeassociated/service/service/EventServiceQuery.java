package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;

import java.util.List;

public interface EventServiceQuery {
    List<EventDto> getAllEvents();
    List<EventWithCategoryDto> getAllEventsWithCategories();
    List<EventDto> getAllEventsByTitle(String title);
    List<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title);
    List<EventDto> getAllEventsCreatedBy(String user);
    List<EventDto> getAllEventsModifiedBy(String user);
    List<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String user);
    List<EventWithCategoryDto> getAllEventsModifiedByWithCategories(String user);
    EventDto getEventById(String id);
    EventWithCategoryDto getEventByIdWithCategories(String id);
    EventDto getEventByUuid(String uuid);
    EventWithCategoryDto getEventByUuidWithCategories(String uuid);
    List<EventDto> getAllEventsByQuery(EventSearchDto dto);
}
