package com.xcodeassociated.service.service.command;

import com.xcodeassociated.service.model.domain.dto.EventDto;

import java.util.List;

public interface EventServiceCommand {
    EventDto createEvent(EventDto dto);
    List<EventDto> createEvent(List<EventDto> dtos);
    EventDto updateEvent(EventDto dto);
    void deleteEvent(String id);
}
