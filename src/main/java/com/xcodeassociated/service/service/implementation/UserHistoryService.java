package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.model.UserEventRecord;
import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.repository.UserEventRecordRepository;
import com.xcodeassociated.service.service.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserHistoryService implements UserHistoryServiceQuery, UserHistoryServiceCommand {
    private final UserEventRecordRepository userEventRecordRepository;
    private final EventRepository eventRepository;

    @Override
    public Mono<UserEventRecordDto> registerUserEventRecord(UserEventRecordDto dto) {
        log.info("Registering user event record: {}", dto);
        return this.userEventRecordRepository.save(UserEventRecord.fromDto(dto))
                .map(UserEventRecord::toDto);
    }

    @Override
    public Mono<Void> deleteUserEventRecord(String id) {
        log.info("Deleting user event record by id: {}", id);
        return this.userEventRecordRepository.deleteById(id);
    }

    @Override
    public Mono<UserEventRecordDto> getUserEventRecordById(String id) {
        log.info("Getting user event record by id: {}", id);
        return this.userEventRecordRepository.findUserEventRecordById(id)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Mono<UserEventDto> getUserEventById(String id) {
        log.info("Getting user event by id: {}", id);
        return this.userEventRecordRepository.findUserEventRecordById(id)
                .flatMap(this::getUserEventDto);
    }

    @Override
    public Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId) {
        log.info("Getting user event record by user auth id: {}", authId);
        return this.userEventRecordRepository.findUserEventRecordsByUserAuthId(authId)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Flux<UserEventDto> getUserEventsByUserAuthId(String authId) {
        log.info("Getting user events by user auth id: {}", authId);
        return this.userEventRecordRepository.findUserEventRecordsByUserAuthId(authId)
                .flatMap(this::getUserEventDto);
    }

    @Override
    public Flux<UserEventRecordDto> getUserEventRecordsByEventId(String eventId) {
        log.info("Getting user event record by event id: {}", eventId);
        return this.userEventRecordRepository.findUserEventRecordsByEventId(eventId)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Flux<UserEventDto> getUserEventsByEventId(String eventId) {
        log.info("Getting user events by event id: {}", eventId);
        return this.userEventRecordRepository.findUserEventRecordsByEventId(eventId)
                .flatMap(this::getUserEventDto);
    }

    private Mono<UserEventDto> getUserEventDto(UserEventRecord record) {
        return this.eventRepository.findEventById(record.getEventId())
                .map(e -> new UserEventDto().toBuilder()
                        .userAuthId(record.getUserAuthId())
                        .eventDto(e.toDto())
                        .build());
    }
}
