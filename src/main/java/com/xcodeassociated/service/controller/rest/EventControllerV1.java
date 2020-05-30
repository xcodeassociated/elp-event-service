package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.service.EventServiceCommand;
import com.xcodeassociated.service.service.EventServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/events")
public class EventControllerV1 {

    private final EventServiceQuery eventServiceQuery;
    private final EventServiceCommand eventServiceCommand;

    @GetMapping()
    public ResponseEntity<Flux<EventDto>> getAllEvents() {
        log.info("Processing `getAllEvents` request in EventControllerV1");

        return new ResponseEntity<>(this.eventServiceQuery.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Mono<EventDto>> getEvent(@PathVariable String id) {
        log.info("Processing `getEvent` request in EventControllerV1, id: {}", id);

        return new ResponseEntity<>(this.eventServiceQuery.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/by/uuid/{value}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Mono<EventDto>> getEventByUuid(@PathVariable String value) {
        log.info("Processing `getEventByUuid` request in EventControllerV1, value: {}", value);

        return new ResponseEntity<>(this.eventServiceQuery.getEventByUuid(value), HttpStatus.OK);
    }

    @GetMapping("/by/title/{title}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Flux<EventDto>> getEventsByTitle(@PathVariable String title) {
        log.info("Processing `getEventsByTitle` request in EventControllerV1, value: {}", title);

        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/by/createdby/{user}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Flux<EventDto>> getEventsCreatedBy(@PathVariable String user) {
        log.info("Processing `getEventsCreatedBy` request in EventControllerV1, value: {}", user);

        return new ResponseEntity<>(this.eventServiceQuery.getAllEventsCreatedBy(user), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Mono<EventDto>> createEvent(@RequestBody EventDto eventDto) {
        log.info("Processing `createEvent` request in EventControllerV1, eventDto: {}", eventDto);

        return new ResponseEntity<>(this.eventServiceCommand.createEvent(eventDto), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Mono<EventDto>> updateEvent(@RequestBody EventDto eventDto) {
        log.info("Processing `updateEvent` request in EventControllerV1, eventDto: {}", eventDto);

        return new ResponseEntity<>(this.eventServiceCommand.updateEvent(eventDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Mono<Void>> deleteEvent(@PathVariable String id) {
        log.info("Processing `deleteEvent` request in EventControllerV1, id: {}", id);

        return new ResponseEntity<>(this.eventServiceCommand.deleteEvent(id), HttpStatus.OK);
    }
}
