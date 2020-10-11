package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.SpotDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SpotDocumentRepository extends BaseDocumentRepository<SpotDocument, String> {
    @NotNull
    Page<SpotDocument> findAll(@NotNull Pageable pageable);

    Optional<SpotDocument> findSpotById(String id);

    @NotNull
    SpotDocument save(@NotNull SpotDocument spotDocument);

    void deleteSpotById(String id);
}
