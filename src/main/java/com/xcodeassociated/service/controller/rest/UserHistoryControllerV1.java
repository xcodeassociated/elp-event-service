package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.service.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/record")
public class UserHistoryControllerV1 {
    private final UserHistoryServiceQuery userHistoryServiceQuery;
    private final UserHistoryServiceCommand userHistoryServiceCommand;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserEventRecordDto> getUserEventRecordById(@PathVariable String id) {
        log.info("Getting user event record for id: {}", id);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserEventRecordDto> getUserEventById(@PathVariable String id) {
        log.info("Getting user event record for id: {}", id);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordById(id), HttpStatus.OK);
    }

    @GetMapping("/by/user/{userId}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<UserEventRecordDto>> getUserEventRecordsByUserAuthId(@PathVariable String userId) {
        log.info("Getting all user event record for user auth id: {}", userId);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordsByUserAuthId(userId), HttpStatus.OK);
    }

    @GetMapping("/by/user/{userId}/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<UserEventDto>> getUserEventsByUserAuthId(@PathVariable String userId) {
        log.info("Getting all user events for user auth id: {}", userId);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventsByUserAuthId(userId), HttpStatus.OK);
    }

    @GetMapping("/by/event/{eventId}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<UserEventRecordDto>> getUserEventRecordsByEventId(@PathVariable String eventId) {
        log.info("Getting all user event record for event id: {}", eventId);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordsByEventId(eventId), HttpStatus.OK);
    }

    @GetMapping("/by/event/{eventId}/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<UserEventDto>> getUserEventsByEventId(@PathVariable String eventId) {
        log.info("Getting all user events for event id: {}", eventId);
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventsByEventId(eventId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserEventRecord(@PathVariable String id) {
        log.info("Delete user event record by id: {}", id);
        this.userHistoryServiceCommand.deleteUserEventRecord(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserEventRecordDto> registerUserEventRecord(@RequestBody UserEventRecordDto dto) {
        log.info("Register user event record: {}", dto);
        this.userHistoryServiceCommand.registerUserEventRecord(dto);
        return ResponseEntity.ok().build();
    }
}
