package com.xcodeassociated.service.repository.domain.provider;

import com.xcodeassociated.service.model.db.EventCategoryDocument;
import com.xcodeassociated.service.model.domain.EventCategory;
import com.xcodeassociated.service.repository.db.EventCategoryDocumentRepository;
import com.xcodeassociated.service.repository.domain.EventCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventCategoryProvider implements EventCategoryRepository {
    private final EventCategoryDocumentRepository documentRepository;

    @Override
    public @NotNull Page<EventCategory> findAll(@NotNull Pageable pageable) {
        return this.documentRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<EventCategory> findEventCategoryById(String id) {
        return this.documentRepository.findEventCategoryById(id).map(this::toDomain);
    }

    @Override
    public List<EventCategory> findEventCategoryByIdIn(List<String> ids) {
        return this.documentRepository.findEventCategoryByIdIn(ids).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull EventCategory save(@NotNull EventCategory eventCategory) {
        return this.toDomain(this.documentRepository.save(this.toDocument(eventCategory)));
    }

    @Override
    public void deleteById(@NotNull String id) {
        this.documentRepository.deleteById(id);
    }

    private EventCategory toDomain(EventCategoryDocument document) {
        var builder= DomainObjectUtils.toBaseDomainObject(EventCategory.builder(), document);

        return builder
                .title(document.getTitle())
                .description(document.getDescription())
                .build();
    }

    private EventCategoryDocument toDocument(EventCategory eventCategory) {
        var builder = DomainObjectUtils.toBaseDocument(EventCategoryDocument.builder(), eventCategory);

        return builder
                .title(eventCategory.getTitle())
                .description(eventCategory.getDescription())
                .build();
    }
}
