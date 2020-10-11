package com.xcodeassociated.service.repository.domain;

import com.xcodeassociated.service.model.domain.UserEventRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserEventRecordRepository {
    Optional<UserEventRecord> findUserEventRecordById(String id);

    Optional<UserEventRecord> findUserEventRecordByUserAuthIdAndEventId(String authId, String eventId);

    Page<UserEventRecord> findUserEventRecordsByUserAuthId(String authId, Pageable pageable);

    Page<UserEventRecord> findUserEventRecordsByEventId(String authId, Pageable pageable);

    @NotNull
    UserEventRecord save(@NotNull UserEventRecord UserEventRecord);

    void deleteById(String id);
}
