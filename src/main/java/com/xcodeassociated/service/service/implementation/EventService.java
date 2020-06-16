package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Event;
import com.xcodeassociated.service.model.EventCategory;
import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import com.xcodeassociated.service.model.dto.LocationDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.repository.ReactiveTemplateRepository;
import com.xcodeassociated.service.repository.PageHelper;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
class EventWrapper {
    private Event event;
    private Set<EventCategory> eventCategories;
}

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventService implements EventServiceQuery, EventServiceCommand {
    private final EventRepository eventRepository;
    private final ReactiveTemplateRepository<Event> reactiveTemplateRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;
    private final EventCategoryService eventCategoryService;

    @Override
    public Flux<EventDto> getAllEvents(Pageable pageable) {
        log.info("Getting all events");
        return PageHelper.apply(this.eventRepository.findAll(pageable.getSort()), pageable)
                .map(Event::toDto);
    }

    @Override
    public Flux<EventWithCategoryDto> getAllEventsWithCategories(Pageable pageable) {
        log.info("Getting all events with categories");
        return PageHelper.apply(this.eventRepository.findAll(pageable.getSort()), pageable)
                .map(this::toEventWrapper)
                .map(e -> e.getEvent().toDto(e.getEventCategories()));
    }

    public Flux<EventDto> getAllEventsByQuery(EventSearchDto dto) {
        log.info("Getting all events by search dto: {}", dto);
        double minDistance = 0;
        Double maxDistance = dto.getRange();
        LocationDto location = dto.getLocation();
        BasicQuery eventQuery = new BasicQuery("{geoLocation:{ $near: { $geometry: { type: 'Point', coordinates: " +
                "[" + location.getLatitude() + "," + location.getLongitude() + " ] }, " +
                "$minDistance: " + minDistance + ", $maxDistance: " + maxDistance + "}}}");

        Flux<Event> foundEvents = this.reactiveTemplateRepository.findAllEventsByQuery(eventQuery, Event.class);

    }

    @Override
    public Flux<EventDto> getAllEventsByTitle(String title) {
        log.info("Getting Events by title: {}", title);
        return this.eventRepository.findByTitleContainingIgnoreCase(title)
                .map(Event::toDto);
    }

    @Override
    public Flux<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title) {
        log.info("Getting Events with categories by title: {}", title);
        return this.eventRepository.findByTitleContainingIgnoreCase(title)
                .map(this::toEventWrapper)
                .map(e -> e.getEvent().toDto(e.getEventCategories()));

    }

    @Override
    public Flux<EventDto> getAllEventsCreatedBy(String user) {
        log.info("Getting Events created by user: {}", user);
        return this.getEventsByUser(user).map(Event::toDto);
    }

    @Override
    public Flux<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String user) {
        log.info("Getting Events with categories created by user: {}", user);
        return this.getEventsByUser(user)
                .map(this::toEventWrapper)
                .map(e -> e.getEvent().toDto(e.getEventCategories()));
    }

    @Override
    public Mono<EventDto> getEventById(String id) {
        log.info("Getting Event by id: {}", id);
        return this.getById(id).map(Event::toDto);
    }

    @Override
    public Mono<EventWithCategoryDto> getEventByIdWithCategories(String id) {
        log.info("Getting Event with categories by id: {}", id);
        return this.getById(id)
                .map(this::toEventWrapper)
                .map(e -> e.getEvent().toDto(e.getEventCategories()));
    }

    @Override
    public Mono<EventDto> getEventByUuid(String uuid) {
        log.info("Getting Event by uuid: {}", uuid);
        return this.getById(uuid).map(Event::toDto);
    }

    @Override
    public Mono<EventWithCategoryDto> getEventByUuidWithCategories(String uuid) {
        log.info("Getting Event with categories by uuid: {}", uuid);
        return this.getById(uuid)
                .map(this::toEventWrapper)
                .map(e -> e.getEvent().toDto(e.getEventCategories()));
    }

    @Override
    public Mono<EventDto> createEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating event by: {},  with dto: {}", author, dto);

        final Event event = Event.fromDto(dto);
        event.setModifiedBy(author);

        return this.eventRepository.save(event)
                .map(Event::toDto);
    }

    @Override
    public Mono<EventDto> updateEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Update by: {}, event with dto: {}", author, dto);

        return this.eventRepository.findEventById(dto.getId())
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.E001, "id: " + dto.getId())))
                .map(e -> {
                    e.setModifiedBy(author);
                    return e.update(dto);
                })
                .map(this.eventRepository::save)
                .flatMap(e -> e.map(Event::toDto));
    }

    @Override
    public Mono<Void> deleteEvent(String id) {
        log.info("Deleting event by id: {}", id);
        return this.eventRepository.deleteById(id);
    }

    private Flux<Event> getEventsByUser(String user) {
        // note: ReactiveQueryByExampleExecutor used via example document object
        Event example = new Event().toBuilder()
                .createdBy(user)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("uuid")
                .withIgnoreNullValues()
                .withIgnoreCase();

        log.info("Using example object: {} and matcher: {}", example, matcher);

        return this.eventRepository.findAll(Example.of(example, matcher));
    }

    private EventWrapper toEventWrapper(Event e) {
        return new EventWrapper().toBuilder()
                .event(e)
                .eventCategories(this.getEventCategories(e).stream().collect(Collectors.toSet()))
                .build();
    }

    private Mono<Event> getById(String id) {
        return this.eventRepository.findEventById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.E001, "id: " + id)));
    }

    private Mono<Event> getByUuid(String uuid) {
        return this.eventRepository.findEventByUuid(uuid)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.E001, "uuid: " + uuid)));
    }

    private List<EventCategory> getEventCategories(Event event) {
        return this.eventCategoryService.getEventCategoryByIdsDocuments(event.getEventCategories().stream()
                .collect(Collectors.toList()))
                .collectList()
                .block();
    }

}
