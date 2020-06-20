package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends BaseRepository<Event, String> {
    List<Event> findAll();
    Optional<Event> findEventById(String id);
    Optional<Event> findEventByUuid(String uuid);
    List<Event> findEventsByTitleContainingIgnoreCase(String title);
    List<Event> findEventsByIdIn(List<String> ids);
//    List<Event> find(Query query);
}
