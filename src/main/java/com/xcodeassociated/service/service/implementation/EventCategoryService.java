package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.model.EventCategory;
import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.repository.EventCategoryRepository;
import com.xcodeassociated.service.service.EventCategoryCommand;
import com.xcodeassociated.service.service.EventCategoryQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventCategoryService implements EventCategoryQuery, EventCategoryCommand {
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;

    @Override
    public Mono<EventCategoryDto> getEventCategoryById(String id) {
        log.info("Getting event category by id: {}", id);
        return this.eventCategoryRepository.getEventCategoryById(id)
                .map(EventCategory::toDto);
    }

    @Override
    public Flux<EventCategoryDto> getAllCategories() {
        log.info("Getting all categories");
        return this.eventCategoryRepository.getAll()
                .map(EventCategory::toDto);
    }

    @Override
    public Flux<EventCategoryDto> getEventCategoryByIds(List<String> ids) {
        log.info("Getting event categories by ids: {}", ids);
        return this.eventCategoryRepository.getEventCategoriesByIdIn(ids)
                .map(EventCategory::toDto);
    }

    Flux<EventCategory> getEventCategoryByIdsDocuments(List<String> ids) {
        log.info("Getting event documents categories by ids: {}", ids);
        return this.eventCategoryRepository.getEventCategoriesByIdIn(ids);
    }

    @Override
    public Mono<EventCategoryDto> saveCategory(EventCategoryDto dto) {
        log.info("Saving event category: {}", dto);
        return this.eventCategoryRepository.getEventCategoryById(dto.getId())
                .switchIfEmpty(this.createUserDataFromDto(dto))
                .map(e -> this.updateUserDataFromDto(e, dto))
                .map(this.eventCategoryRepository::save)
                .flatMap(e -> e.map(EventCategory::toDto));
    }

    @Override
    public Mono<Void> deleteCategoryById(String id) {
        log.info("Deleting event category by id: {}", id);
        return this.eventCategoryRepository.deleteById(id);
    }

    private Mono<EventCategory> createUserDataFromDto(EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        final EventCategory eventCategory = EventCategory.fromDto(dto);
        eventCategory.setModifiedBy(modificationAuthor);
        return Mono.just(eventCategory);
    }

    private EventCategory updateUserDataFromDto(EventCategory eventCategory, EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        eventCategory.setModifiedBy(modificationAuthor);
        return eventCategory.update(dto);
    }
}
