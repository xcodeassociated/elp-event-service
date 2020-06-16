package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.service.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Mono<UserEventRecordDto> getUserEventById(@PathVariable String id) {
        log.info("Getting user event record for id: {}", id);
        return this.userHistoryServiceQuery.getUserEventRecordById(id);
    }

    @GetMapping("/by/user/{userId}")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(@PathVariable String userId) {
        log.info("Getting all user event record for user auth id: {}", userId);
        return this.userHistoryServiceQuery.getUserEventRecordsByUserAuthId(userId);
    }

    @GetMapping("/by/user/{userId}/data")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventDto> getUserEventsByUserAuthId(@PathVariable String userId) {
        log.info("Getting all user events for user auth id: {}", userId);
        return this.userHistoryServiceQuery.getUserEventsByUserAuthId(userId);
    }

    @GetMapping("/by/event/{eventId}")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventRecordDto> getUserEventRecordsByEventId(@PathVariable String eventId) {
        log.info("Getting all user event record for event id: {}", eventId);
        return this.userHistoryServiceQuery.getUserEventRecordsByEventId(eventId);
    }

    @GetMapping("/by/event/{eventId}/data")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<UserEventDto> getUserEventsByEventId(@PathVariable String eventId) {
        log.info("Getting all user events for event id: {}", eventId);
        return this.userHistoryServiceQuery.getUserEventsByEventId(eventId);
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
