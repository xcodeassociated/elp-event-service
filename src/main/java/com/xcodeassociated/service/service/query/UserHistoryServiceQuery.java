package com.xcodeassociated.service.service.query;

import com.xcodeassociated.service.model.domain.dto.UserEventDto;
import com.xcodeassociated.service.model.domain.dto.UserEventRecordDto;
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
