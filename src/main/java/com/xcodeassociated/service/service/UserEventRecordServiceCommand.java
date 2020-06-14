package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import reactor.core.publisher.Mono;

public interface UserEventRecordServiceCommand {
    Mono<UserEventRecordDto> registerUserEventRecord(UserEventRecordDto dto);
    Mono<Void> deleteUserEventRecord(String id);
}
