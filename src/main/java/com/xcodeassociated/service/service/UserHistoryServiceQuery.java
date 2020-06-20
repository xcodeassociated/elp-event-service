package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserHistoryServiceQuery {
    UserEventRecordDto getUserEventRecordById(String id);
    UserEventDto getUserEventById(String id);
    Page<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId, Pageable pageable);
    Page<UserEventDto> getUserEventsByUserAuthId(String authId, Pageable pageable);
    Page<UserEventRecordDto> getUserEventRecordsByEventId(String eventId, Pageable pageable);
    Page<UserEventDto> getUserEventsByEventId(String eventId, Pageable pageable);
}
