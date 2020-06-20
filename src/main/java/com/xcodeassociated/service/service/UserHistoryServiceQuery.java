package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;

import java.util.List;

public interface UserHistoryServiceQuery {
    UserEventRecordDto getUserEventRecordById(String id);
    UserEventDto getUserEventById(String id);
    List<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId);
    List<UserEventDto> getUserEventsByUserAuthId(String authId);
    List<UserEventRecordDto> getUserEventRecordsByEventId(String eventId);
    List<UserEventDto> getUserEventsByEventId(String eventId);
}
