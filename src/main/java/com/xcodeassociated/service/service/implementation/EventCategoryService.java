package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.EventCategory;
import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.repository.EventCategoryRepository;
import com.xcodeassociated.service.service.EventCategoryCommand;
import com.xcodeassociated.service.service.EventCategoryQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventCategoryService implements EventCategoryQuery, EventCategoryCommand {
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;

    @Override
    public EventCategoryDto getEventCategoryById(String id) {
        log.info("Getting event category by id: {}", id);
        return this.eventCategoryRepository.findEventCategoryById(id)
                .map(EventCategory::toDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public Page<EventCategoryDto> getAllCategories(Pageable pageable) {
        log.info("Getting all categories");
        return this.eventCategoryRepository.findAll(pageable).map(EventCategory::toDto);
    }

    @Override
    public List<EventCategoryDto> getAllCategories() {
        log.info("Getting all categories");
        return this.eventCategoryRepository.findAll().stream()
                .map(EventCategory::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventCategoryDto> getEventCategoryByIds(List<String> ids) {
        log.info("Getting event categories by ids: {}", ids);
        return this.eventCategoryRepository.findEventCategoryByIdIn(ids).stream()
                .map(EventCategory::toDto).collect(Collectors.toList());
    }

    List<EventCategory> getEventCategoryByIdsDocuments(List<String> ids) {
        log.info("Getting event documents categories by ids: {}", ids);
        return this.eventCategoryRepository.findEventCategoryByIdIn(ids);
    }

    @Override
    public EventCategoryDto saveCategory(EventCategoryDto dto) {
        log.info("Saving event category: {}", dto);
        return Stream.of(this.eventCategoryRepository.findEventCategoryById(dto.getId())
                .orElse(this.createUserDataFromDto(dto)))
                .map(e -> this.updateUserDataFromDto(e, dto))
                .map(this.eventCategoryRepository::save)
                .map(EventCategory::toDto)
                .findFirst()
                .get();
    }

    @Override
    public void deleteCategoryById(String id) {
        log.info("Deleting event category by id: {}", id);
        this.eventCategoryRepository.deleteById(id);
    }

    private EventCategory createUserDataFromDto(EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        final EventCategory eventCategory = EventCategory.fromDto(dto);
        eventCategory.setModifiedBy(modificationAuthor);
        return eventCategory;
    }

    private EventCategory updateUserDataFromDto(EventCategory eventCategory, EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        eventCategory.setModifiedBy(modificationAuthor);
        return eventCategory.update(dto);
    }
}
