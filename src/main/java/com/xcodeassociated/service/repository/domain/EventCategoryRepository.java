package com.xcodeassociated.service.repository.domain;

import com.xcodeassociated.service.model.domain.EventCategory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventCategoryRepository {
    @NotNull
    Page<EventCategory> findAll(@NotNull Pageable pageable);

    Optional<EventCategory> findEventCategoryById(String id);

    List<EventCategory> findEventCategoryByIdIn(List<String> ids);

    @NotNull
    EventCategory save(@NotNull EventCategory eventCategory);

    void deleteById(@NotNull String id);
}
