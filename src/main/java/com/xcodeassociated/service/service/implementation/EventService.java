package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.domain.Event;
import com.xcodeassociated.service.model.domain.EventCategory;
import com.xcodeassociated.service.model.domain.UserData;
import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;
import com.xcodeassociated.service.model.domain.dto.EventDto;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import com.xcodeassociated.service.model.domain.dto.LocationDto;
import com.xcodeassociated.service.repository.domain.EventRepository;
import com.xcodeassociated.service.repository.domain.UserDetailsParams;
import com.xcodeassociated.service.repository.domain.provider.DomainObjectUtils;
import com.xcodeassociated.service.service.command.EventServiceCommand;
import com.xcodeassociated.service.service.query.EventServiceQuery;
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
public class EventService implements EventServiceQuery, EventServiceCommand {
    private final EventRepository eventRepository;
    private final OauthAuditorServiceQuery oauthAuditorServiceQuery;
    private final EventCategoryService eventCategoryService;
    private final UserDataService userDataService;

    @Override
    public Page<EventDto> getAllEvents(boolean includeUserDetails, Pageable pageable) {
        log.info("Getting all events");

        UserDetailsParams userDetailsParams = UserDetailsParams.builder()
                .userAuthId(this.oauthAuditorServiceQuery.getModificationAuthor())
                .build();

        return includeUserDetails ?
                this.eventRepository.findAll(userDetailsParams, pageable).map(Event::toDto)
                : this.eventRepository.findAll(null, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllActiveEvents(boolean includeUserDetails, Pageable pageable) {
        log.info("Getting all active events");

        Long currentMillis = System.currentTimeMillis();
        UserDetailsParams userDetailsParams = UserDetailsParams.builder()
                .userAuthId(this.oauthAuditorServiceQuery.getModificationAuthor())
                .build();

        return includeUserDetails ?
                this.eventRepository.findAllEventsByStopAfter(currentMillis, userDetailsParams, pageable).map(Event::toDto)
                : this.eventRepository.findAllEventsByStopAfter(currentMillis, null, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsByQuery(EventSearchDto dto, boolean includeUserDetails, Pageable pageable) {
        log.info("Getting all events by search dto: {}", dto);

        UserDetailsParams userDetailsParams = UserDetailsParams.builder()
                .userAuthId(this.oauthAuditorServiceQuery.getModificationAuthor())
                .build();

        return includeUserDetails ?
                this.eventRepository.getAllEventsByQuery(dto, userDetailsParams, pageable).map(Event::toDto)
                : this.eventRepository.getAllEventsByQuery(dto, null, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsByPreference(String authId, LocationDto locationDto, boolean includeUserDetails, Pageable pageable) {
        log.info("Getting all events by user preference for user authId: {}", authId);
        Optional<UserData> userData = this.userDataService.getUserDataOptionalByAuthId(authId);
        if (userData.isEmpty()) {
            throw new ServiceException(ErrorCode.E002, "User has no data set");
        }

        EventSearchDto eventSearchDto = this.getEventSearchDtoFromUserData(userData.get(), locationDto, true);

        log.info("Using search dto for user preference event search: {}", eventSearchDto);
        return this.getAllEventsByQuery(eventSearchDto, includeUserDetails, pageable);
    }

    @Override
    public Page<EventDto> getAllEventsByTitle(String title, Pageable pageable) {
        log.info("Getting Events by title: {}", title);
        return this.eventRepository.findEventsByTitleContainingIgnoreCase(title, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsCreatedBy(String authId, Pageable pageable) {
        log.info("Getting Events created by user: {}", authId);
        return this.eventRepository.getEventsByUser(authId, pageable).map(Event::toDto);
    }

    @Override
    public Page<EventDto> getAllEventsModifiedBy(String authId, Pageable pageable) {
        log.info("Getting Events modified by user: {}", authId);
        return this.eventRepository.getEventsByModified(authId, pageable).map(Event::toDto);
    }

    @Override
    public EventDto getEventById(String id) {
        log.info("Getting Event by id: {}", id);
        return this.getById(id).toDto();
    }

    @Override
    public EventDto getEventByUuid(String uuid) {
        log.info("Getting Event by uuid: {}", uuid);
        return this.getByUuid(uuid).toDto();
    }

    @Override
    public EventDto createEvent(EventDto dto) {
        String author = this.oauthAuditorServiceQuery.getModificationAuthor();
        log.info("Creating event by: {}, with dto: {}", author, dto);

        return this.eventRepository.save(this.eventFromDto(dto, author)).toDto();
    }

    @Override
    public List<EventDto> createEvent(List<EventDto> dtos) {
        String author = this.oauthAuditorServiceQuery.getModificationAuthor();
        log.info("Creating events by: {},  with dto: {}", author, dtos);
        return dtos.stream().map(e -> {
            log.info("Processing event dto: {}", e);
            return this.createEvent(e);
        }).collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(EventDto dto) {
        log.info("Update event with dto: {}", dto);

        return Stream.of(this.eventRepository.findEventById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, "id: " + dto.getId())))
                .map(e -> this.updateEventFromDto(e, dto))
                .map(this.eventRepository::save)
                .findFirst()
                .get()
                .toDto();
    }

    @Override
    public void deleteEvent(String id) {
        log.info("Deleting event by id: {}", id);
        this.eventRepository.deleteById(id);
    }

    private Event updateEventFromDto(Event event, EventDto dto) {
        final String author = this.oauthAuditorServiceQuery.getModificationAuthor();
        var builder = DomainObjectUtils.copyBaseDomainObject(Event.builder(), event);

        return builder
                .modifiedBy(author)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .start(dto.getStart())
                .stop(dto.getStop())
                .eventCategories(dto.getCategories().stream()
                        .map(c -> this.eventCategoryService.getEventCategoryByIdUnmapped(c.getId()))
                        .collect(Collectors.toList()))
                .build();
    }

    private Event eventFromDto(EventDto dto, String author) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .start(dto.getStart())
                .stop(dto.getStop())
                .eventCategories(dto.getCategories().stream()
                        .map(c -> this.eventCategoryService.getEventCategoryByIdUnmapped(c.getId()))
                        .collect(Collectors.toList()))
                .modifiedBy(author)
                .build();
    }

    private EventSearchDto getEventSearchDtoFromUserData(UserData userData, LocationDto locationDto, boolean active) {
        return new EventSearchDto()
                .toBuilder()
                .location(locationDto.getLocation())
                .active(active)
                .range(userData.getMaxDistance())
                .eventCategories(userData.getPreferredCategories().stream()
                        .map(e -> new EventCategoryDto()
                                .toBuilder()
                                .id(e.getId().orElseThrow())
                                .build())
                        .collect(Collectors.toSet())
                )
                .build();
    }

    private List<EventCategory> getEventCategoryByIds(Event e) {
        return this.eventCategoryService.getEventCategoryByIdsUnmapped(e.getEventCategories().stream()
                .map(c->c.getId().orElseThrow())
                .collect(Collectors.toList()));
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
