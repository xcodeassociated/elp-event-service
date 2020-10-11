package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.UserEventRecordDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserEventRecordDocumentRepository extends BaseDocumentRepository<UserEventRecordDocument, String> {
    Optional<UserEventRecordDocument> findUserEventRecordDocumentById(String id);

    Optional<UserEventRecordDocument> findUserEventRecordDocumentByUserAuthIdAndEventId(String authId, String eventId);

    Page<UserEventRecordDocument> findUserEventRecordDocumentsByUserAuthId(String authId, Pageable pageable);

    Page<UserEventRecordDocument> findUserEventRecordDocumentsByEventId(String authId, Pageable pageable);

    boolean existsUserEventRecordDocumentByUserAuthIdAndEventId(String authId, String eventId);

    @NotNull
    UserEventRecordDocument save(@NotNull UserEventRecordDocument userEventRecordDocument);

    void deleteById(String id);
}
