package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import com.xcodeassociated.service.model.dto.LocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventServiceQuery {
    Page<EventDto> getAllEvents(Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsWithCategories(String authId, Pageable pageable);
    Page<EventDto> getAllEventsByTitle(String title, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title, String authId, Pageable pageable);
    Page<EventDto> getAllEventsCreatedBy(String authId, Pageable pageable);
    Page<EventDto> getAllEventsModifiedBy(String authId, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String authId, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsModifiedByWithCategories(String authId, Pageable pageable);
    EventDto getEventById(String id);
    EventWithCategoryDto getEventByIdWithCategories(String id, String authId);
    EventDto getEventByUuid(String uuid);
    EventWithCategoryDto getEventByUuidWithCategories(String uuid, String authId);
    Page<EventDto> getAllEventsByQuery(EventSearchDto dto, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsByQueryWithCategories(EventSearchDto dto, String authId, Pageable pageable);
    Page<EventDto> getAllEventsByPreference(String authId, LocationDto locationDto, Pageable pageable);
    Page<EventWithCategoryDto> getAllEventsByPreferenceWithCategories(String authId, LocationDto locationDto, Pageable pageable);
}
