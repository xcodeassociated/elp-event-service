package com.xcodeassociated.service.service.command;

import com.xcodeassociated.service.model.domain.dto.UserEventRecordDto;

public interface UserHistoryServiceCommand {
    UserEventRecordDto registerUserEventRecord(UserEventRecordDto dto);
    void deleteUserEventRecord(String id);
    void deleteUserEventRecordByAuthIdAndEventId(String authId, String eventId);
}
