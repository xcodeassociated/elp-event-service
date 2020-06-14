package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserHistoryServiceQuery {
    Mono<UserEventRecordDto> getUserEventRecordById(String id);
    Mono<UserEventDto> getUserEventById(String id);
    Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId, Pageable pageable);
    Flux<UserEventDto> getUserEventsByUserAuthId(String authId, Pageable pageable);
    Flux<UserEventRecordDto> getUserEventRecordsByEventId(String eventId, Pageable pageable);
    Flux<UserEventDto> getUserEventsByEventId(String eventId, Pageable pageable);
}
