package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.domain.dto.UserEventDto;
import com.xcodeassociated.service.model.domain.dto.UserEventRecordDto;
import com.xcodeassociated.service.service.command.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import com.xcodeassociated.service.service.query.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/record")
public class UserHistoryControllerV1 {
    private final UserHistoryServiceQuery userHistoryServiceQuery;
    private final UserHistoryServiceCommand userHistoryServiceCommand;
    private final OauthAuditorServiceQuery oauthAuditorService;

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

    @GetMapping("/by/user/{userId}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<UserEventRecordDto>> getUserEventRecordsByUserAuthId(@PathVariable String userId,
                                                                                    @RequestParam(defaultValue = "1") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                    @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getUserEventRecordsByUserAuthId` request in UserHistoryControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordsByUserAuthId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/by/user/{userId}/paged/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<UserEventDto>> getUserEventsByUserAuthId(@PathVariable String userId,
                                                                        @RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                        @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {


        log.info("Processing `getUserEventsByUserAuthId` request in UserHistoryControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventsByUserAuthId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/by/event/{eventId}/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<UserEventRecordDto>> getUserEventRecordsByEventId(@PathVariable String eventId,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                                 @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getUserEventRecordsByEventId` request in UserHistoryControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventRecordsByEventId(eventId, pageable), HttpStatus.OK);
    }

    @GetMapping("/by/event/{eventId}/paged/data")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<UserEventDto>> getUserEventsByEventId(@PathVariable String eventId,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                                     @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {

        log.info("Processing `getUserEventsByEventId` request in UserHistoryControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.userHistoryServiceQuery.getUserEventsByEventId(eventId, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserEventRecord(@PathVariable String id) {
        log.info("Delete user event record by id: {}", id);
        this.userHistoryServiceCommand.deleteUserEventRecord(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/by/event/{eventId}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserEventRecordByAuthIdAndEventId(@PathVariable String eventId) {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Delete user event record by authId: {} and eventId: {}", authId, eventId);
        this.userHistoryServiceCommand.deleteUserEventRecordByAuthIdAndEventId(authId, eventId);
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
