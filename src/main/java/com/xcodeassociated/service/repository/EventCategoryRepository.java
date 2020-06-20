package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.EventCategory;

import java.util.List;
import java.util.Optional;

public interface EventCategoryRepository extends BaseRepository<EventCategory, String> {
    List<EventCategory> findAll();
    Optional<EventCategory> findEventCategoryById(String id);
    List<EventCategory> findEventCategoryByIdIn(List<String> ids);
    EventCategory save(EventCategory eventCategory);
    void deleteById(String id);
}
