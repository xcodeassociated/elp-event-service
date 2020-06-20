package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserEventRecord;

import java.util.List;
import java.util.Optional;

public interface UserEventRecordRepository extends BaseRepository<UserEventRecord, String> {
    Optional<UserEventRecord> findUserEventRecordById(String id);
    List<UserEventRecord> findUserEventRecordsByUserAuthId(String authId);
    List<UserEventRecord> findUserEventRecordsByEventId(String authId);
    UserEventRecord save(UserEventRecord userEventRecord);
    void deleteById(String id);
}
