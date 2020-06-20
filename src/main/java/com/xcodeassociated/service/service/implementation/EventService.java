package com.xcodeassociated.service.service.implementation;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Event;
import com.xcodeassociated.service.model.EventCategory;
import com.xcodeassociated.service.model.QEvent;
import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.service.EventServiceCommand;
import com.xcodeassociated.service.service.EventServiceQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
class EventWrapper {
    private Event event;
    private List<EventCategory> eventCategories;
}

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventService implements EventServiceQuery, EventServiceCommand {
    private final EventRepository eventRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;
    private final EventCategoryService eventCategoryService;

    @Override
    public Page<EventDto> getAllEvents(Pageable pageable) {
        log.info("Getting all events");
        return this.eventRepository.findAll(pageable)
                .map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsWithCategories(Pageable pageable) {
        log.info("Getting all events with categories");
        return this.mapCategories(this.eventRepository.findAll(pageable));
    }

    @Override
    public Page<EventDto> getAllEventsByQuery(EventSearchDto dto, Pageable pageable) {
        log.info("Getting all events by search dto: {}", dto);
        return Objects.isNull(dto.getLocation())
                ? this.findEventsByQueryWithoutLocation(dto, pageable).map(Event::toDto)
                : Page.empty();
    }

    private Page<Event> findEventsByQueryWithoutLocation(EventSearchDto dto
            , Pageable pageable) {
        Event example = new Event().toBuilder()
                .title(dto.getTitle())
                .eventCategories(dto.getEventCategories().stream()
                        .map(EventCategoryDto::getDescription)
                        .collect(Collectors.toSet()))
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("eventCategories", contains())
                .withIgnorePaths("uuid")
                .withIgnoreNullValues()
                .withIgnoreCase();

        log.info("Using example object: {} and matcher: {}", example, matcher);

        return this.eventRepository.findAll(Example.of(example, matcher), pageable);
    }

    @Override
    public Page<EventDto> getAllEventsByTitle(String title, Pageable pageable) {
        log.info("Getting Events by title: {}", title);
        return this.eventRepository.findEventsByTitleContainingIgnoreCase(title, pageable)
                .map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title, Pageable pageable) {
        log.info("Getting Events with categories by title: {}", title);
        return this.mapCategories(this.eventRepository.findEventsByTitleContainingIgnoreCase(title, pageable));
    }

    @Override
    public Page<EventDto> getAllEventsCreatedBy(String user, Pageable pageable) {
        log.info("Getting Events created by user: {}", user);
        return this.getEventsByUser(user, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsModifiedBy(String user, Pageable pageable) {
        log.info("Getting Events modified by user: {}", user);
        return this.getEventsByModified(user, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String user, Pageable pageable) {
        log.info("Getting Events with categories created by user: {}", user);
        return this.mapCategories(this.getEventsByUser(user, pageable));
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsModifiedByWithCategories(String user, Pageable pageable) {
        log.info("Getting Events with categories created by user: {}", user);
        return this.mapCategories(this.getEventsByModified(user, pageable));
    }

    @Override
    public EventDto getEventById(String id) {
        log.info("Getting Event by id: {}", id);
        return this.getById(id).toDto();
    }

    @Override
    public EventWithCategoryDto getEventByIdWithCategories(String id) {
        log.info("Getting Event with categories by id: {}", id);
        return this.mapCategory(this.getById(id));
    }

    @Override
    public EventDto getEventByUuid(String uuid) {
        log.info("Getting Event by uuid: {}", uuid);
        return this.getByUuid(uuid).toDto();
    }

    @Override
    public EventWithCategoryDto getEventByUuidWithCategories(String uuid) {
        log.info("Getting Event with categories by uuid: {}", uuid);
        return this.mapCategory(this.getByUuid(uuid));
    }

    @Override
    public EventDto createEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating event by: {},  with dto: {}", author, dto);

        final Event event = Event.fromDto(dto);
        event.setModifiedBy(author);

        return this.eventRepository.save(event).toDto();
    }

    @Override
    public List<EventDto> createEvent(List<EventDto> dtos) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating events by: {},  with dto: {}", author, dtos);
        return dtos.stream().map(e -> {
            log.info("Processing event dto: {}", e);
            Event event = Event.fromDto(e);
            event.setModifiedBy(author);
            return event;
        }).map(e -> {
            log.info("Saving event: {}", e);
            return this.eventRepository.save(e);
        }).map(Event::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Update by: {}, event with dto: {}", author, dto);

        return Stream.of(this.eventRepository.findEventById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, "id: " + dto.getId())))
                .map(e -> {
                    e.setModifiedBy(author);
                    return e.update(dto);
                })
                .map(this.eventRepository::save)
                .map(Event::toDto)
                .findFirst()
                .get();
    }

    @Override
    public void deleteEvent(String id) {
        log.info("Deleting event by id: {}", id);
        this.eventRepository.deleteById(id);
    }

    private Page<EventWithCategoryDto> mapCategories(Page<Event> events) {
        return events.map(e -> {
            List<EventCategory> eventCategories = this.getEventCategoryByIds(e);
            return new EventWrapper().toBuilder()
                    .event(e)
                    .eventCategories(eventCategories)
                    .build();
        }).map(e -> e.getEvent().toDto(e.getEventCategories()));
    }

    private EventWithCategoryDto mapCategory(Event event) {
        List<EventCategory> eventCategories = this.getEventCategoryByIds(event);
        return event.toDto(eventCategories);
    }

    private List<EventCategory> getEventCategoryByIds(Event e) {
        return this.eventCategoryService.getEventCategoryByIdsDocuments(e.getEventCategories().stream().collect(Collectors.toList()));
    }

    private Page<Event> getEventsByUser(String user, Pageable pageable) {
        // note: ReactiveQueryByExampleExecutor used via example document object
        Event example = new Event().toBuilder()
                .createdBy(user)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("uuid")
                .withIgnoreNullValues()
                .withIgnoreCase();

        log.info("Using example object: {} and matcher: {}", example, matcher);

        return this.eventRepository.findAll(Example.of(example, matcher), pageable);
    }

    private Page<Event> getEventsByModified(String user, Pageable pageable) {
        // note: ReactiveQueryByExampleExecutor used via example document object
        QEvent q = QEvent.event;
        Predicate expression = new BooleanBuilder()
                .and(q.modifiedBy.endsWithIgnoreCase(user))
                .getValue();

        return this.eventRepository.findAll(expression, pageable);
    }

    private Event getById(String id) {
        return this.eventRepository.findEventById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, "id: " + id));
    }

    private Event getByUuid(String uuid) {
        return this.eventRepository.findEventByUuid(uuid)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, "uuid: " + uuid));
    }

}
