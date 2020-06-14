package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserEventRecord;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserEventRecordRepository extends BaseRepository<UserEventRecord, String> {
    Mono<UserEventRecord> findUserEventRecordById(String id);
    Flux<UserEventRecord> findUserEventRecordsByUserAuthId(String authId, Sort sort);
    Flux<UserEventRecord> findUserEventRecordsByEventId(String authId, Sort sort);
    Mono<UserEventRecord> save(UserEventRecord userEventRecord);
    Mono<Void> deleteById(String id);
}
