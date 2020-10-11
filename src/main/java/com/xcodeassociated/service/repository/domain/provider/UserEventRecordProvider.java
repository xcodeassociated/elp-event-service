package com.xcodeassociated.service.repository.domain.provider;

import com.xcodeassociated.service.model.db.UserEventRecordDocument;
import com.xcodeassociated.service.model.domain.UserEventRecord;
import com.xcodeassociated.service.repository.db.UserEventRecordDocumentRepository;
import com.xcodeassociated.service.repository.domain.EventRepository;
import com.xcodeassociated.service.repository.domain.UserEventRecordRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserEventRecordProvider implements UserEventRecordRepository {
    private final UserEventRecordDocumentRepository documentRepository;
    private final EventRepository eventRepository;

    @Override
    public Optional<UserEventRecord> findUserEventRecordById(String id) {
        return this.documentRepository.findUserEventRecordDocumentById(id).map(this::toDomain);
    }

    @Override
    public Optional<UserEventRecord> findUserEventRecordByUserAuthIdAndEventId(String authId, String eventId) {
        return this.documentRepository.findUserEventRecordDocumentByUserAuthIdAndEventId(authId, eventId).map(this::toDomain);
    }

    @Override
    public Page<UserEventRecord> findUserEventRecordsByUserAuthId(String authId, Pageable pageable) {
        return this.documentRepository.findUserEventRecordDocumentsByUserAuthId(authId, pageable).map(this::toDomain);
    }

    @Override
    public Page<UserEventRecord> findUserEventRecordsByEventId(String authId, Pageable pageable) {
        return this.documentRepository.findUserEventRecordDocumentsByEventId(authId, pageable).map(this::toDomain);
    }

    @Override
    public @NotNull UserEventRecord save(@NotNull UserEventRecord UserEventRecord) {
        return this.toDomain(this.documentRepository.save(this.toDocument(UserEventRecord)));
    }

    @Override
    public void deleteById(String id) {
        this.documentRepository.deleteById(id);
    }

    private UserEventRecord toDomain(UserEventRecordDocument document) {
        var builder = DomainObjectUtils.toBaseDomainObject(UserEventRecord.builder(), document);

        return builder
                .userAuthId(document.getUserAuthId())
                .event(this.eventRepository.findEventById(document.getEventId()).orElseThrow())
                .build();
    }

    private UserEventRecordDocument toDocument(UserEventRecord userEventRecord) {
        var builder = DomainObjectUtils.toBaseDocument(UserEventRecordDocument.builder(), userEventRecord);

        return builder
                .userAuthId(userEventRecord.getUserAuthId())
                .eventId(userEventRecord.getEvent().getId().orElseThrow())
                .build();
    }

}
