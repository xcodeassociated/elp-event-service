package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.service.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/record")
public class UserHistoryControllerV1 {
    private final UserHistoryServiceQuery userHistoryServiceQuery;
    private final UserHistoryServiceCommand userHistoryServiceCommand;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserEventRecordDto> getUserEventRecordById(@PathVariable String id) {
        log.info("Getting user event record for id: {}", id);
        return this.userHistoryServiceQuery.getUserEventRecordById(id);
    }

    @GetMapping("/{id}/data")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserEventDto> getUserEventById(@PathVariable String id) {
        log.info("Getting user event record for id: {}", id);
        return this.userHistoryServiceQuery.getUserEventById(id);
    }

    @GetMapping("/by/user/{userId}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(@PathVariable String userId, @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                    @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        log.info("Getting all user event record for user auth id: {}, pageable: {}", userId, pageable);
        return this.userHistoryServiceQuery.getUserEventRecordsByUserAuthId(userId, pageable);
    }

    @GetMapping("/by/user/{userId}/data/paged")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventDto> getUserEventsByUserAuthId(@PathVariable String userId, @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                        @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        log.info("Getting all user events for user auth id: {}, pageable: {}", userId, pageable);
        return this.userHistoryServiceQuery.getUserEventsByUserAuthId(userId, pageable);
    }

    @GetMapping("/by/event/{eventId}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventRecordDto> getUserEventRecordsByEventId(@PathVariable String eventId, @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                 @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        log.info("Getting all user event record for event id: {}, pageable: {}", eventId, pageable);
        return this.userHistoryServiceQuery.getUserEventRecordsByEventId(eventId, pageable);
    }

    @GetMapping("/by/event/{eventId}/data/paged")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventDto> getUserEventsByEventId(@PathVariable String eventId, @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                     @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        log.info("Getting all user events for event id: {}, pageable: {}", eventId, pageable);
        return this.userHistoryServiceQuery.getUserEventsByEventId(eventId, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteUserEventRecord(@PathVariable String id) {
        log.info("Delete user event record by id: {}", id);
        return this.userHistoryServiceCommand.deleteUserEventRecord(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserEventRecordDto> registerUserEventRecord(@RequestBody UserEventRecordDto dto) {
        log.info("Register user event record: {}", dto);
        return this.userHistoryServiceCommand.registerUserEventRecord(dto);
    }
}
