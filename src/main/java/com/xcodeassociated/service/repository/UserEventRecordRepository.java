package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserEventRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserEventRecordRepository extends BaseRepository<UserEventRecord, String> {
    Mono<UserEventRecord> getUserEventRecordById(String id);
    Flux<UserEventRecord> getUserEventRecordsByUserAuthId(String authId);
    Flux<UserEventRecord> getUserEventRecordsByEventId(String authId);
    Mono<UserEventRecord> save(UserEventRecord userEventRecord);
    Mono<Void> deleteById(String id);
}
