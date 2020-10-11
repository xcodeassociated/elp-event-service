package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.domain.EventCategory;
import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;
import com.xcodeassociated.service.repository.domain.EventCategoryRepository;
import com.xcodeassociated.service.repository.domain.provider.DomainObjectUtils;
import com.xcodeassociated.service.service.command.EventCategoryServiceCommand;
import com.xcodeassociated.service.service.query.EventCategoryServiceQuery;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventCategoryService implements EventCategoryServiceQuery, EventCategoryServiceCommand {
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceQuery oauthAuditorServiceQuery;

    @Override
    public EventCategoryDto getEventCategoryById(String id) {
        log.info("Getting event category by id: {}", id);

        return this.eventCategoryRepository.findEventCategoryById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""))
                .toDto();
    }

    EventCategory getEventCategoryByIdUnmapped(String id) {
        log.info("Getting event category by id: {}", id);

        return this.eventCategoryRepository.findEventCategoryById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public Page<EventCategoryDto> getAllCategories(Pageable pageable) {
        log.info("Getting all categories");

        return this.eventCategoryRepository.findAll(pageable)
                .map(EventCategory::toDto);
    }

    @Override
    public List<EventCategoryDto> getEventCategoryByIds(List<String> ids) {
        log.info("Getting event categories by ids: {}", ids);

        return this.eventCategoryRepository.findEventCategoryByIdIn(ids).stream()
                .map(EventCategory::toDto)
                .collect(Collectors.toList());
    }

    List<EventCategory> getEventCategoryByIdsUnmapped(List<String> ids) {
        log.info("Getting event categories by ids: {}", ids);

        return this.eventCategoryRepository.findEventCategoryByIdIn(ids);
    }

    @Override
    public EventCategoryDto saveCategory(EventCategoryDto dto) {
        log.info("Saving event category: {}", dto);

        return Stream.of(this.eventCategoryRepository.findEventCategoryById(dto.getId())
                .map(e -> this.updateEventCategoryFromDto(e, dto))
                .orElse(this.createEventCategoryFromDto(dto)))
                .map(this.eventCategoryRepository::save)
                .findFirst()
                .get()
                .toDto();
    }

    @Override
    public void deleteCategoryById(String id) {
        log.info("Deleting event category by id: {}", id);

        this.eventCategoryRepository.deleteById(id);
    }

    private EventCategory createEventCategoryFromDto(EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();
        return EventCategory.builder()
                .version(dto.getVersion())
                .modifiedBy(modificationAuthor)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    private EventCategory updateEventCategoryFromDto(EventCategory eventCategory, EventCategoryDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();
        var builder = DomainObjectUtils.copyBaseDomainObject(EventCategory.builder(), eventCategory);

        return builder
                .modifiedBy(modificationAuthor)
                .title(Optional.ofNullable(dto.getTitle()).orElseThrow())
                .description(dto.getDescription())
                .build();
    }
}
