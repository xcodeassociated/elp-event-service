package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.EventDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventDocumentRepository extends BaseDocumentRepository<EventDocument, String> {
    @NotNull
    Page<EventDocument> findAll(@NotNull Pageable pageable);

    Page<EventDocument> findAllEventsByStopAfter(Long millis, Pageable pageable);

    Optional<EventDocument> findEventById(String id);

    Optional<EventDocument> findEventByUuid(String uuid);

    Page<EventDocument> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<EventDocument> findEventsByIdIn(List<String> ids, Pageable pageable);

    @NotNull
    EventDocument save(@NotNull EventDocument document);

    void deleteById(@NotNull String id);
}
