package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends BaseRepository<Event, String> {
    Page<Event> findAll(Pageable pageable);
    Page<Event> findAllEventsByStopAfter(Long millis, Pageable pageable);
    Optional<Event> findEventById(String id);
    Optional<Event> findEventByUuid(String uuid);
    Page<Event> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Event> findEventsByIdIn(List<String> ids, Pageable pageable);
}
