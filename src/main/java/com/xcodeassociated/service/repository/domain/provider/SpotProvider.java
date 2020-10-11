package com.xcodeassociated.service.repository.domain.provider;

import com.xcodeassociated.service.model.db.SpotDocument;
import com.xcodeassociated.service.model.domain.Spot;
import com.xcodeassociated.service.repository.db.SpotDocumentRepository;
import com.xcodeassociated.service.repository.domain.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpotProvider implements SpotRepository {
    private final SpotDocumentRepository documentRepository;

    @Override
    public @NotNull Page<Spot> findAll(@NotNull Pageable pageable) {
        return this.documentRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Spot> findSpotById(String id) {
        return this.documentRepository.findSpotById(id).map(this::toDomain);
    }

    @Override
    public @NotNull Spot save(@NotNull Spot spot) {
        return this.toDomain(this.documentRepository.save(this.toDocument(spot)));
    }

    @Override
    public void deleteById(@NotNull String id) {
        this.documentRepository.deleteSpotById(id);
    }

    private Spot toDomain(SpotDocument document) {
        var builder = DomainObjectUtils.toBaseDomainObject(Spot.builder(), document);

        return builder
                .description(document.getDescription())
                .title(document.getTitle())
                .location(document.getLocation())
                .build();
    }

    private SpotDocument toDocument(Spot spot) {
        var builder = DomainObjectUtils.toBaseDocument(SpotDocument.builder(), spot);

        return builder
                .title(spot.getTitle())
                .description(spot.getDescription())
                .location(spot.getLocation())
                .build();
    }
}
