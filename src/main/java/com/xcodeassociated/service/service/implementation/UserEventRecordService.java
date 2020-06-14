package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.model.UserEventRecord;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.repository.UserEventRecordRepository;
import com.xcodeassociated.service.service.UserEventRecordServiceCommand;
import com.xcodeassociated.service.service.UserEventRecordServiceQuery;
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
public class UserEventRecordService implements UserEventRecordServiceQuery, UserEventRecordServiceCommand {
    private final UserEventRecordRepository userEventRecordRepository;

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
        return this.userEventRecordRepository.getUserEventRecordById(id)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Flux<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId) {
        log.info("Getting user event record by user auth id: {}", authId);
        return this.userEventRecordRepository.getUserEventRecordsByUserAuthId(authId)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Flux<UserEventRecordDto> getUserEventRecordsByEventId(String eventId) {
        log.info("Getting user event record by event id: {}", eventId);
        return this.userEventRecordRepository.getUserEventRecordsByEventId(eventId)
                .map(UserEventRecord::toDto);
    }
}
