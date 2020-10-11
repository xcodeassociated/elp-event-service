package com.xcodeassociated.service.repository.domain.provider;

import com.google.api.client.util.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.db.BaseDocument;
import com.xcodeassociated.service.model.db.EventDocument;
import com.xcodeassociated.service.model.db.QEventDocument;
import com.xcodeassociated.service.model.domain.Event;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import com.xcodeassociated.service.repository.db.EventDocumentRepository;
import com.xcodeassociated.service.repository.db.UserEventRecordDocumentRepository;
import com.xcodeassociated.service.repository.domain.EventCategoryRepository;
import com.xcodeassociated.service.repository.domain.EventRepository;
import com.xcodeassociated.service.repository.domain.UserDetailsParams;
import com.xcodeassociated.service.service.implementation.helper.EventSearchDtoHelper;
import com.xcodeassociated.service.service.implementation.query.EventQuery;
import com.xcodeassociated.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventProvider implements EventRepository {
    private final EventDocumentRepository documentRepository;
    private final EventCategoryRepository categoryRepository;
    private final UserEventRecordDocumentRepository userEventRecordDocumentRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public @NotNull Page<Event> findAll(UserDetailsParams userDetailsParams, @NotNull Pageable pageable) {
        return this.documentRepository.findAll(pageable).map(e -> this.toDomain(e, userDetailsParams));
    }

    @Override
    public Page<Event> findAllEventsByStopAfter(Long millis, UserDetailsParams userDetailsParams, Pageable pageable) {
        return this.documentRepository.findAllEventsByStopAfter(millis, pageable).map(e -> this.toDomain(e, userDetailsParams));
    }

    @Override
    public Optional<Event> findEventById(String id) {
        return this.documentRepository.findEventById(id).map(this::toDomain);
    }

    @Override
    public Optional<Event> findEventByUuid(String uuid) {
        return this.documentRepository.findEventByUuid(uuid).map(this::toDomain);
    }

    @Override
    public Page<Event> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable) {
        return this.documentRepository.findEventsByTitleContainingIgnoreCase(title, pageable).map(this::toDomain);
    }

    @Override
    public Page<Event> findEventsByIdIn(List<String> ids, Pageable pageable) {
        return this.documentRepository.findEventsByIdIn(ids, pageable).map(this::toDomain);
    }

    @Override
    public @NotNull Event save(@NotNull Event event) {
        return this.toDomain(this.documentRepository.save(this.toDocument(event)));
    }

    @Override
    public void deleteById(String id) {
        this.documentRepository.deleteById(id);
    }

    @Override
    public Page<Event> getAllEventsByQuery(EventSearchDto dto, UserDetailsParams userDetailsParams, Pageable pageable) {
        return Utils.anyNonNull(dto.getLocation())
                ? this.findEventsByQueryWithLocation(dto, dto.getActive(), userDetailsParams, pageable)
                : this.findEventsByQueryWithoutLocation(dto, dto.getActive(), userDetailsParams, pageable);
    }

    @Override
    public Page<Event> getEventsByModified(String user, Pageable pageable) {
        // note: ReactiveQueryByExampleExecutor used via example document object
        QEventDocument q = QEventDocument.eventDocument;
        Predicate expression = new BooleanBuilder()
                .and(q.modifiedBy.endsWithIgnoreCase(user))
                .getValue();

        return this.documentRepository.findAll(expression, pageable).map(this::toDomain);
    }

    @Override
    public Page<Event> getEventsByUser(String user, Pageable pageable) {
        // note: ReactiveQueryByExampleExecutor used via example document object
        EventDocument example = new EventDocument().toBuilder()
                .createdBy(user)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("uuid")
                .withIgnoreNullValues()
                .withIgnoreCase();

        return this.documentRepository.findAll(Example.of(example, matcher), pageable).map(this::toDomain);
    }

    private Page<Event> findEventsByQueryWithoutLocation(EventSearchDto dto, boolean active, UserDetailsParams userDetailsParams, Pageable pageable) {
        Long currentMillis = System.currentTimeMillis();
        BooleanExpression expression = EventQuery.toPredicate(dto, currentMillis, active)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "BooleanExpression empty"));
        log.info("Using expression: {}", expression);
        return this.documentRepository.findAll(expression, pageable).map(e -> this.toDomain(e, userDetailsParams));
    }

    private Page<Event> findEventsByQueryWithLocation(EventSearchDto dto, boolean active, UserDetailsParams userDetailsParams, Pageable pageable) {
        Long currentMillis = System.currentTimeMillis();
        Optional<BooleanExpression> expression = EventQuery.toPredicate(dto, currentMillis, active);
        if (expression.isPresent()) {
            log.info("Using expression: {}", expression.get());

            List<Double> location = List.of(dto.getLocation());
            List<EventDocument> eventDocuments = Lists.newArrayList(this.documentRepository.findAll(expression.get()));
            List<String> eventIds = eventDocuments.stream()
                    .map(BaseDocument::getId)
                    .collect(Collectors.toList());

            Query query = EventQuery.getLocationQuery(dto, pageable, eventIds, location, Metrics.KILOMETERS);
            Query countQuery = EventQuery.countLocationQuery(dto, eventIds, location, Metrics.KILOMETERS);
            log.info("Using query: {} and countQuery: {}", query, countQuery);

            List<EventDocument> result = this.mongoTemplate.find(query, EventDocument.class);
            return PageableExecutionUtils.getPage(result, pageable, () -> this.mongoTemplate.count(countQuery, EventDocument.class))
                    .map(e -> this.toDomain(e, userDetailsParams));

        } else if (EventSearchDtoHelper.dtoSearchable(dto)) {
            log.info("Expression is empty, using only location query");
            List<Double> location = List.of(dto.getLocation());

            Query query = EventQuery.getLocationQuery(dto, pageable, location, Metrics.KILOMETERS);
            Query countQuery = EventQuery.countLocationQuery(dto, location, Metrics.KILOMETERS);
            log.info("Using query: {} and countQuery: {}", query, countQuery);

            List<EventDocument> result = this.mongoTemplate.find(query, EventDocument.class);
            return PageableExecutionUtils.getPage(result, pageable, () -> this.mongoTemplate.count(countQuery, EventDocument.class))
                    .map(e -> this.toDomain(e, userDetailsParams));

        } else {
            log.error("EventSearchDto: {} is not searchable", dto);
            throw new ServiceException(ErrorCode.E002, "EventSearchDto is not searchable");
        }
    }

    private Event toDomain(EventDocument document) {
        return this.toDomain(document, null);
    }

    private Event toDomain(EventDocument document, UserDetailsParams userDetailsParams) {
        var builder = DomainObjectUtils.toBaseDomainObject(Event.builder(), document);

        Optional<Event.UserDetails> userDetails = Objects.isNull(userDetailsParams)
                ? Optional.empty() : Optional.of(this.processUserDetails(document.getId(), userDetailsParams));

        return builder
                .title(document.getTitle())
                .description(document.getDescription())
                .location(document.getLocation())
                .start(document.getStart())
                .stop(document.getStop())
                .eventCategories(this.categoryRepository.findEventCategoryByIdIn(new ArrayList<>(document.getEventCategories())))
                .userDetails(userDetails)
                .build();
    }

    private Event.UserDetails processUserDetails(String eventId, UserDetailsParams userDetailsParams) {
        return Event.UserDetails.builder()
                .registered(userEventRecordDocumentRepository.existsUserEventRecordDocumentByUserAuthIdAndEventId
                        (userDetailsParams.getUserAuthId(), eventId))
                .build();
    }

    private EventDocument toDocument(Event event) {
        var builder = DomainObjectUtils.toBaseDocument(EventDocument.builder(),  event);

        return builder
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .start(event.getStart())
                .stop(event.getStop())
                .eventCategories(event.getEventCategories().stream()
                        .filter(e-> e.getId().isPresent())
                        .map(e -> e.getId().orElseThrow())
                        .collect(Collectors.toSet()))
                .build();
    }

}
