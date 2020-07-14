package com.xcodeassociated.service.service.implementation;

import com.google.api.client.util.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.*;
import com.xcodeassociated.service.model.dto.*;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.service.EventServiceCommand;
import com.xcodeassociated.service.service.EventServiceQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.implementation.helper.EventSearchDtoHelper;
import com.xcodeassociated.service.service.implementation.query.EventQuery;
import com.xcodeassociated.service.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final EventCategoryServiceServiceService eventCategoryService;
    private final UserDataService userDataService;
    private final UserHistoryService userHistoryService;
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<EventDto> getAllEvents(Pageable pageable) {
        log.info("Getting all events");
        return this.eventRepository.findAll(pageable)
                .map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsWithCategories(String authId, Pageable pageable) {
        log.info("Getting all events with categories");
        return this.mapEventRegistered(this.mapCategories(this.eventRepository.findAll(pageable)), authId);
    }

    @Override
    public Page<EventDto> getAllActiveEvents(Pageable pageable) {
        log.info("Getting all active events");
        Long currentMillis = System.currentTimeMillis();
        return this.eventRepository.findAllEventsByStopAfter(currentMillis, pageable)
                .map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllActiveEventsWithCategories(String authId, Pageable pageable) {
        log.info("Getting all active events with categories");
        Long currentMillis = System.currentTimeMillis();
        return this.mapEventRegistered(this.mapCategories(this.eventRepository.findAllEventsByStopAfter(currentMillis, pageable)), authId);
    }

    @Override
    public Page<EventDto> getAllEventsByQuery(EventSearchDto dto, Pageable pageable) {
        log.info("Getting all events by search dto: {}", dto);
        return Utils.anyNonNull(dto.getLocation())
                ? this.findEventsByQueryWithLocation(dto, dto.getActive(), pageable).map(Event::toDto)
                : this.findEventsByQueryWithoutLocation(dto, dto.getActive(), pageable).map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsByQueryWithCategories(EventSearchDto dto, String authId, Pageable pageable) {
        log.info("Getting all events by search dto: {}", dto);
        return Utils.anyNonNull(dto.getLocation())
                ? this.mapEventRegistered(this.mapCategories(this.findEventsByQueryWithLocation(dto, dto.getActive(), pageable)), authId)
                : this.mapEventRegistered(this.mapCategories(this.findEventsByQueryWithoutLocation(dto, dto.getActive(), pageable)), authId);
    }

    @Override
    public Page<EventDto> getAllEventsByPreference(String authId, LocationDto locationDto, Pageable pageable) {
        log.info("Getting all events by user preference for user authId: {}", authId);
        Optional<UserData> userData = this.userDataService.getUserDataOptionalByAuthId(authId);
        if (userData.isEmpty()) {
            throw new ServiceException(ErrorCode.E002, "User has no data set");
        }

        // note: active events by preference
        EventSearchDto eventSearchDto = this.getEventSearchDtoFromUserData(userData.get(), locationDto, true);

        log.info("Using search dto for user preference event search: {}", eventSearchDto);
        return this.getAllEventsByQuery(eventSearchDto, pageable);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsByPreferenceWithCategories(String authId, LocationDto locationDto, Pageable pageable) {
        log.info("Getting all events by user preference with categories for user authId: {}", authId);
        Optional<UserData> userData = this.userDataService.getUserDataOptionalByAuthId(authId);
        if (userData.isEmpty()) {
            throw new ServiceException(ErrorCode.E002, "User has no data set");
        }

        // note: active events by preference
        EventSearchDto eventSearchDto = this.getEventSearchDtoFromUserData(userData.get(), locationDto, true);

        log.info("Using search dto for user preference event search: {}", eventSearchDto);
        return this.getAllEventsByQueryWithCategories(eventSearchDto, authId, pageable);
    }

    @Override
    public Page<EventDto> getAllEventsByTitle(String title, Pageable pageable) {
        log.info("Getting Events by title: {}", title);
        return this.eventRepository.findEventsByTitleContainingIgnoreCase(title, pageable)
                .map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsByTitleWithCategories(String title, String authId, Pageable pageable) {
        log.info("Getting Events with categories by title: {}", title);
        return this.mapEventRegistered(this.mapCategories(this.eventRepository.findEventsByTitleContainingIgnoreCase(title, pageable)), authId);
    }

    @Override
    public Page<EventDto> getAllEventsCreatedBy(String authId, Pageable pageable) {
        log.info("Getting Events created by user: {}", authId);
        return this.getEventsByUser(authId, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsModifiedBy(String authId, Pageable pageable) {
        log.info("Getting Events modified by user: {}", authId);
        return this.getEventsByModified(authId, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsCreatedByWithCategories(String authId, Pageable pageable) {
        log.info("Getting Events with categories created by user: {}", authId);
        return this.mapEventRegistered(this.mapCategories(this.getEventsByUser(authId, pageable)), authId);
    }

    @Override
    public Page<EventWithCategoryDto> getAllEventsModifiedByWithCategories(String authId, Pageable pageable) {
        log.info("Getting Events with categories created by user: {}", authId);
        return this.mapEventRegistered(this.mapCategories(this.getEventsByModified(authId, pageable)), authId);
    }

    @Override
    public EventDto getEventById(String id) {
        log.info("Getting Event by id: {}", id);
        return this.getById(id).toDto();
    }

    @Override
    public EventWithCategoryDto getEventByIdWithCategories(String id, String authId) {
        log.info("Getting Event with categories by id: {}", id);
        return this.mapEventRegistered(this.mapCategory(this.getById(id)), authId);
    }

    @Override
    public EventDto getEventByUuid(String uuid) {
        log.info("Getting Event by uuid: {}", uuid);
        return this.getByUuid(uuid).toDto();
    }

    @Override
    public EventWithCategoryDto getEventByUuidWithCategories(String uuid, String authId) {
        log.info("Getting Event with categories by uuid: {}", uuid);
        return this.mapEventRegistered(this.mapCategory(this.getByUuid(uuid)), authId);
    }

    @Override
    public EventDto createEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating event by: {},  with dto: {}", author, dto);

        final Event event = Event.creteFromDto(dto);
        event.setModifiedBy(author);

        return this.eventRepository.save(event).toDto();
    }

    @Override
    public List<EventDto> createEvent(List<EventDto> dtos) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating events by: {},  with dto: {}", author, dtos);
        return dtos.stream().map(e -> {
            log.info("Processing event dto: {}", e);
            Event event = Event.creteFromDto(e);
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

    private EventSearchDto getEventSearchDtoFromUserData(UserData userData, LocationDto locationDto, boolean active) {
        return new EventSearchDto()
                .toBuilder()
                .location(locationDto.getLocation())
                .active(active)
                .range(userData.getMaxDistance())
                .eventCategories(userData.getUserPreferredCategories().stream()
                        .map(e -> new EventCategoryDto()
                                .toBuilder()
                                .id(e)
                                .build())
                        .collect(Collectors.toSet())
                )
                .build();
    }

    private Page<Event> findEventsByQueryWithoutLocation(EventSearchDto dto, boolean active, Pageable pageable) {
        Long currentMillis = System.currentTimeMillis();
        BooleanExpression expression = EventQuery.toPredicate(dto, currentMillis, active)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "BooleanExpression empty"));
        log.info("Using expression: {}", expression);
        return this.eventRepository.findAll(expression, pageable);
    }

    private Page<Event> findEventsByQueryWithLocation(EventSearchDto dto, boolean active, Pageable pageable) {
        Long currentMillis = System.currentTimeMillis();
        Optional<BooleanExpression> expression = EventQuery.toPredicate(dto, currentMillis, active);
        if (expression.isPresent()) {
            log.info("Using expression: {}", expression.get());

            List<Double> location = List.of(dto.getLocation());
            List<Event> events = Lists.newArrayList(this.eventRepository.findAll(expression.get()));
            List<String> eventIds = events.stream()
                    .map(BaseDocument::getId)
                    .collect(Collectors.toList());

            Query query = EventQuery.getLocationQuery(dto, pageable, eventIds, location, Metrics.KILOMETERS);
            Query countQuery = EventQuery.countLocationQuery(dto, eventIds, location, Metrics.KILOMETERS);
            log.info("Using query: {} and countQuery: {}", query, countQuery);

            List<Event> result = this.mongoTemplate.find(query, Event.class);
            return PageableExecutionUtils.getPage(result, pageable, () -> this.mongoTemplate.count(countQuery, Event.class));

        } else if (EventSearchDtoHelper.dtoSearchable(dto)) {
            log.info("Expression is empty, using only location query");
            List<Double> location = List.of(dto.getLocation());

            Query query = EventQuery.getLocationQuery(dto, pageable, location, Metrics.KILOMETERS);
            Query countQuery = EventQuery.countLocationQuery(dto, location, Metrics.KILOMETERS);
            log.info("Using query: {} and countQuery: {}", query, countQuery);

            List<Event> result = this.mongoTemplate.find(query, Event.class);
            return PageableExecutionUtils.getPage(result, pageable, () -> this.mongoTemplate.count(countQuery, Event.class));

        } else {
            log.error("EventSearchDto: {} is not searchable", dto);
            throw new ServiceException(ErrorCode.E002, "EventSearchDto is not searchable");
        }
    }

    private Page<EventWithCategoryDto> mapEventRegistered(Page<EventWithCategoryDto> events, String authId) {
        return events.map(e -> {
            Optional<UserEventRecord> userEventRecord = this.userHistoryService.getUserEventForUserAuthIdAndEventId(authId, e.getId());
            Event event = Event.fromDto(e);
            List<EventCategoryDto> eventCategories = e.getCategories();
            return event.toDto(eventCategories, userEventRecord.isPresent());
        });
    }

    private EventWithCategoryDto mapEventRegistered(EventWithCategoryDto e, String authId) {
        if (StringUtils.isNoneBlank(authId)) {
            Optional<UserEventRecord> userEventRecord = this.userHistoryService.getUserEventForUserAuthIdAndEventId(authId, e.getId());
            Event event = Event.fromDto(e);
            List<EventCategoryDto> eventCategories = e.getCategories();
            return event.toDto(eventCategories, userEventRecord.isPresent());
        } else {
            return e;
        }
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
