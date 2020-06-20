package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventRecordDto;

public interface UserHistoryServiceCommand {
    UserEventRecordDto registerUserEventRecord(UserEventRecordDto dto);
    void deleteUserEventRecord(String id);
}
