package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserEventRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserEventRecordRepository extends BaseRepository<UserEventRecord, String> {
    Mono<UserEventRecord> findUserEventRecordById(String id);
    Flux<UserEventRecord> findUserEventRecordsByUserAuthId(String authId);
    Flux<UserEventRecord> findUserEventRecordsByEventId(String authId);
    Mono<UserEventRecord> save(UserEventRecord userEventRecord);
    Mono<Void> deleteById(String id);
}
