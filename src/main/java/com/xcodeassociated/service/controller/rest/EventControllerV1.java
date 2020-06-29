package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import com.xcodeassociated.service.model.dto.LocationDto;
import com.xcodeassociated.service.service.EventServiceCommand;
import com.xcodeassociated.service.service.EventServiceQuery;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
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
    private final OauthAuditorServiceInterface oauthAuditorService;
    private final EventServiceQuery eventServiceQuery;
    private final EventServiceCommand eventServiceCommand;

    @GetMapping("/paged")
    public ResponseEntity<Page<EventDto>> getAllEvents(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                       @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getAllEvents` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEvents(pageable), HttpStatus.OK);
    }

    @GetMapping("/paged/data")
    public ResponseEntity<Page<EventWithCategoryDto>> getAllEventsWithCategories(@RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                 @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        log.info("Processing `getAllEventsWithCategories` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsWithCategories(this.oauthAuditorService.getUserSub(), pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        log.info("Processing `getEvent` request in EventControllerV1, id: {}", id);
        return new ResponseEntity<>(this.eventServiceQuery.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventWithCategoryDto> getEventWithCategories(@PathVariable String id) {
        log.info("Processing `getEventWithCategories` request in EventControllerV1, id: {}", id);
        return new ResponseEntity<>(this.eventServiceQuery.getEventByIdWithCategories(id, this.oauthAuditorService.getUserSub()), HttpStatus.OK);
    }

    @GetMapping("/by/uuid/{value}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventDto> getEventByUuid(@PathVariable String value) {
        log.info("Processing `getEventByUuid` request in EventControllerV1, value: {}", value);
        return new ResponseEntity<>(this.eventServiceQuery.getEventByUuid(value), HttpStatus.OK);
    }

    @GetMapping("/by/uuid/{value}/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventWithCategoryDto> getEventByUuidWithCategories(@PathVariable String value) {
        log.info("Processing `getEventByUuidWithCategories` request in EventControllerV1, value: {}", value);
        return new ResponseEntity<>(this.eventServiceQuery.getEventByUuidWithCategories(value, this.oauthAuditorService.getUserSub()), HttpStatus.OK);
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

    @GetMapping("/by/title/{title}/paged/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventWithCategoryDto>> getEventsByTitleWithCategories(@PathVariable String title,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int size,
                                                                                     @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                     @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getEventsByTitleWithCategories` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByTitleWithCategories(title, this.oauthAuditorService.getUserSub(), pageable), HttpStatus.OK);
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

    @GetMapping("/by/createdby/{user}/paged/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventWithCategoryDto>> getEventsCreatedByWithCategories(@PathVariable String user,
                                                                                       @RequestParam(defaultValue = "1") int page,
                                                                                       @RequestParam(defaultValue = "10") int size,
                                                                                       @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                       @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getEventsCreatedByWithCategories` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsCreatedByWithCategories(user, pageable), HttpStatus.OK);
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
                                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getPreferredEvents` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        String userAuthId = this.oauthAuditorService.getUserSub();
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByPreference(userAuthId, locationDto, pageable), HttpStatus.OK);
    }

    @PostMapping("/preferred/paged/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventWithCategoryDto>> getPreferredEventsWithCategories(@RequestBody LocationDto locationDto,
                                                                                       @RequestParam(defaultValue = "1") int page,
                                                                                       @RequestParam(defaultValue = "10") int size,
                                                                                       @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                       @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getPreferredEventsWithCategories` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        String userAuthId = this.oauthAuditorService.getUserSub();
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByPreferenceWithCategories(userAuthId, locationDto, pageable), HttpStatus.OK);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventDto>> searchEventByQuery(@RequestBody EventSearchDto dto,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `searchEventByQuery` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);
        log.info("Search dto: {}", dto);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByQuery(dto, pageable), HttpStatus.OK);
    }

    @PostMapping("/search/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventWithCategoryDto>> searchEventByQueryWithCategories(@RequestBody EventSearchDto dto,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                             @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `searchEventByQuery` request in EventControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);
        log.info("Search dto: {}", dto);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByQueryWithCategories(dto, this.oauthAuditorService.getUserSub(), pageable), HttpStatus.OK);
    }
}
