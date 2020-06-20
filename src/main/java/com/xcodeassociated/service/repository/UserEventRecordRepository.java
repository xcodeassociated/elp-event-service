package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserEventRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserEventRecordRepository extends BaseRepository<UserEventRecord, String> {
    Optional<UserEventRecord> findUserEventRecordById(String id);
    Page<UserEventRecord> findUserEventRecordsByUserAuthId(String authId, Pageable pageable);
    Page<UserEventRecord> findUserEventRecordsByEventId(String authId, Pageable pageable);
    UserEventRecord save(UserEventRecord userEventRecord);
    void deleteById(String id);
}
