package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.domain.dto.EventDto;
import com.xcodeassociated.service.model.domain.dto.EventSearchDto;
import com.xcodeassociated.service.model.domain.dto.LocationDto;
import com.xcodeassociated.service.service.command.EventServiceCommand;
import com.xcodeassociated.service.service.query.EventServiceQuery;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/events")
public class EventControllerV1 {
    private final OauthAuditorServiceQuery oauthAuditorService;
    private final EventServiceQuery eventServiceQuery;
    private final EventServiceCommand eventServiceCommand;

    @GetMapping("/paged")
    public ResponseEntity<Page<EventDto>> getAllEvents(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                       @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection,
                                                       @RequestParam(name = "user_details", defaultValue = "false") String userDetails) {

        log.info("Processing `getAllEvents` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}, userDetails: {}",
                page, size, sortBy, sortDirection, userDetails);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEvents(Boolean.parseBoolean(userDetails), pageable), HttpStatus.OK);
    }

    @GetMapping("/active/paged")
    public ResponseEntity<Page<EventDto>> getAllActiveEvents(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                       @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection,
                                                       @RequestParam(name = "user_details", defaultValue = "false") String userDetails) {

        log.info("Processing `getAllEvents` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}, userDetails: {}",
                page, size, sortBy, sortDirection, userDetails);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllActiveEvents(Boolean.parseBoolean(userDetails), pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        log.info("Processing `getEvent` request in EventControllerV1, id: {}", id);
        return new ResponseEntity<>(this.eventServiceQuery.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/by/uuid/{value}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> getEventByUuid(@PathVariable String value) {
        log.info("Processing `getEventByUuid` request in EventControllerV1, value: {}", value);
        return new ResponseEntity<>(this.eventServiceQuery.getEventByUuid(value), HttpStatus.OK);
    }

    @GetMapping("/by/title/{title}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> getEventsByTitle(@PathVariable String title,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                           @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getEventsByTitle` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByTitle(title, pageable), HttpStatus.OK);
    }

    @GetMapping("/by/createdby/{user}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> getEventsCreatedBy(@PathVariable String user,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getEventsCreatedBy` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsCreatedBy(user, pageable), HttpStatus.OK);
    }

    @GetMapping("/by/modifiedby/{user}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> getEventsModifiedBy(@PathVariable String user,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                              @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getEventsModifiedBy` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsModifiedBy(user, pageable), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
        log.info("Processing `createEvent` request in EventControllerV1, eventDto: {}", eventDto);
        return new ResponseEntity<>(this.eventServiceCommand.createEvent(eventDto), HttpStatus.OK);
    }

    @PostMapping("/create/multiple")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<EventDto>> createEvents(@RequestBody List<EventDto> eventDtos) {
        log.info("Processing `createEvents` request in EventControllerV1, eventDto: {}", eventDtos);
        return new ResponseEntity<>(this.eventServiceCommand.createEvent(eventDtos), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto eventDto) {
        log.info("Processing `updateEvent` request in EventControllerV1, eventDto: {}", eventDto);
        return new ResponseEntity<>(this.eventServiceCommand.updateEvent(eventDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        log.info("Processing `deleteEvent` request in EventControllerV1, id: {}", id);
        this.eventServiceCommand.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/preferred/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> getPreferredEvents(@RequestBody LocationDto locationDto,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection,
                                                             @RequestParam(name = "user_details", defaultValue = "false") String userDetails) {

        log.info("Processing `getPreferredEvents` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}, userDetails: {}",
                page, size, sortBy, sortDirection, userDetails);

        String userAuthId = this.oauthAuditorService.getUserSub();
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByPreference(userAuthId, locationDto, Boolean.parseBoolean(userDetails), pageable), HttpStatus.OK);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> searchEventByQuery(@RequestBody EventSearchDto dto,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection,
                                             @RequestParam(name = "user_details", defaultValue = "false") String userDetails) {

        log.info("Processing `searchEventByQuery` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}, userDetails: {}",
                page, size, sortBy, sortDirection, userDetails);
        log.info("Search dto: {}", dto);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByQuery(dto, Boolean.parseBoolean(userDetails), pageable), HttpStatus.OK);
    }
}
