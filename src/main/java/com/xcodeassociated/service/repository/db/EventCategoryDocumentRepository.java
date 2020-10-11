package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.EventCategoryDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventCategoryDocumentRepository extends BaseDocumentRepository<EventCategoryDocument, String> {
    @NotNull
    Page<EventCategoryDocument> findAll(@NotNull Pageable pageable);

    Optional<EventCategoryDocument> findEventCategoryById(String id);

    List<EventCategoryDocument> findEventCategoryByIdIn(List<String> ids);

    @NotNull
    EventCategoryDocument save(@NotNull EventCategoryDocument eventCategoryDocument);

    void deleteById(@NotNull String id);
}
