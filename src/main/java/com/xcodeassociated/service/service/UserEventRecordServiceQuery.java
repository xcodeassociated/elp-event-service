package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserEventRecordServiceQuery {
    Mono<UserEventRecordDto> getUserEventRecordById(String id);
    Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId);
    Flux<UserEventRecordDto> getUserEventRecordsByEventId(String eventId);
}
