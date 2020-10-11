package com.xcodeassociated.service.repository.domain;

import com.xcodeassociated.service.model.domain.Spot;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SpotRepository {
    @NotNull
    Page<Spot> findAll(@NotNull Pageable pageable);

    Optional<Spot> findSpotById(String id);

    @NotNull
    Spot save(@NotNull Spot spot);

    void deleteById(@NotNull String id);
}
