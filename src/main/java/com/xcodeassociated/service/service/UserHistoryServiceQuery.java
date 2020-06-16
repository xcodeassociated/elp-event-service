package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserHistoryServiceQuery {
    Mono<UserEventRecordDto> getUserEventRecordById(String id);
    Mono<UserEventDto> getUserEventById(String id);
    Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId);
    Flux<UserEventDto> getUserEventsByUserAuthId(String authId);
    Flux<UserEventRecordDto> getUserEventRecordsByEventId(String eventId);
    Flux<UserEventDto> getUserEventsByEventId(String eventId);
}
