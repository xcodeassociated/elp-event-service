package com.xcodeassociated.service.repository.domain;

import com.xcodeassociated.service.model.domain.Event;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    @NotNull
    Page<Event> findAll(@NotNull Pageable pageable);

    Page<Event> findAllEventsByStopAfter(Long millis, Pageable pageable);

    Optional<Event> findEventById(String id);

    Optional<Event> findEventByUuid(String uuid);

    Page<Event> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Event> findEventsByIdIn(List<String> ids, Pageable pageable);

    @NotNull
    Event save(@NotNull Event event);

    void deleteById(String id);

    Page<Event> getAllEventsByQuery(EventSearchDto dto, Pageable pageable);

    Page<Event> findEventsByQueryWithoutLocation(EventSearchDto dto, boolean active, Pageable pageable);

    Page<Event> findEventsByQueryWithLocation(EventSearchDto dto, boolean active, Pageable pageable);

    Page<Event> getEventsByUser(String user, Pageable pageable);

    Page<Event> getEventsByModified(String user, Pageable pageable);

}
