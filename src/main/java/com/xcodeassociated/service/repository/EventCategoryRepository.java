package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventCategoryRepository extends BaseRepository<EventCategory, String> {
    Page<EventCategory> findAll(Pageable pageable);
    Optional<EventCategory> findEventCategoryById(String id);
    List<EventCategory> findEventCategoryByIdIn(List<String> ids);
    EventCategory save(EventCategory eventCategory);
    void deleteById(String id);
}
