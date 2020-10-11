package com.xcodeassociated.service.service.query;

import com.xcodeassociated.service.model.domain.dto.EventDto;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import com.xcodeassociated.service.model.domain.dto.LocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventServiceQuery {
    Page<EventDto> getAllEvents(Pageable pageable);
    Page<EventDto> getAllActiveEvents(Pageable pageable);
    Page<EventDto> getAllEventsByTitle(String title, Pageable pageable);
    Page<EventDto> getAllEventsCreatedBy(String authId, Pageable pageable);
    Page<EventDto> getAllEventsModifiedBy(String authId, Pageable pageable);
    EventDto getEventById(String id);
    EventDto getEventByUuid(String uuid);
    Page<EventDto> getAllEventsByQuery(EventSearchDto dto, Pageable pageable);
    Page<EventDto> getAllEventsByPreference(String authId, LocationDto locationDto, Pageable pageable);
}
