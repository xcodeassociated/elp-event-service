package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Event;
import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.repository.EventTemplateRepository;
import com.xcodeassociated.service.service.EventServiceCommand;
import com.xcodeassociated.service.service.EventServiceQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.UserDataServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EventService implements EventServiceQuery, EventServiceCommand {
    private final EventRepository eventRepository;
    private final EventTemplateRepository<Event> eventTemplateRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;
    private final UserDataServiceInterface userDataService;

    @Override
    public Flux<EventDto> getAllEvents() {
        log.info("Getting all events");

        return this.eventRepository
                .findAll()
                .map(Event::toDto);
    }

    @Override
    public Flux<EventDto> getAllEventsByTitle(String title) {
        log.info("Getting Events with title: {}", title);
        return this.eventRepository.findByTitleContainingIgnoreCase(title)
                .map(Event::toDto);
    }

    @Override
    public Flux<EventDto> getAllEventsCreatedBy(String user) {
        log.info("Getting Events created by user: {}", user);
        // note: ReactiveQueryByExampleExecutor used via example document object
        Event example = new Event().toBuilder()
                .createdBy(user)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("uuid")
                .withIgnoreNullValues()
                .withIgnoreCase();

        log.info("Using example object: {} and matcher: {}", example, matcher);

        return this.eventRepository.findAll(Example.of(example, matcher))
                .map(Event::toDto);
    }

    @Override
    public Mono<EventDto> getEventById(String id) {
        log.info("Getting Event by id: {}", id);
        return this.eventRepository.findEventById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.E001, "id: " + id)))
                .map(Event::toDto);
    }

    @Override
    public Mono<EventDto> getEventByUuid(String uuid) {
        log.info("Getting Event by uuid: {}", uuid);

        return this.eventRepository.findEventByUuid(uuid)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.E001, "uuid: " + uuid)))
                .map(Event::toDto);
    }

    @Override
    public Mono<EventDto> createEvent(EventDto dto) {
        String author = this.oauthAuditorServiceInterface.getModificationAuthor();
        log.info("Creating event by: {},  with dto: {}", author, dto);

        final Event event = Event.fromDto(dto);
        event.setModifiedBy(author);

        return this.eventRepository
                .save(event)
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

}
