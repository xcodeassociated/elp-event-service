package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.UserEventRecord;
import com.xcodeassociated.service.model.dto.UserEventDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import com.xcodeassociated.service.repository.EventRepository;
import com.xcodeassociated.service.repository.UserEventRecordRepository;
import com.xcodeassociated.service.service.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.SystemException;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserHistoryService implements UserHistoryServiceQuery, UserHistoryServiceCommand {
    private final UserEventRecordRepository userEventRecordRepository;
    private final EventRepository eventRepository;

    @Override
    public UserEventRecordDto registerUserEventRecord(UserEventRecordDto dto) {
        log.info("Registering user event record: {}", dto);
        return Optional.of(this.userEventRecordRepository.save(UserEventRecord.fromDto(dto)))
                .map(UserEventRecord::toDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public void deleteUserEventRecord(String id) {
        log.info("Deleting user event record by id: {}", id);
        this.userEventRecordRepository.deleteById(id);
    }

    @Override
    public void deleteUserEventRecordByAuthIdAndEventId(String authId, String eventId) {
        log.info("Deleting user event record by authId: {} and eventId: {}", authId, eventId);
        Optional<UserEventRecord> userEventRecord = this.userEventRecordRepository.findUserEventRecordByUserAuthIdAndEventId(authId, eventId);
        if (userEventRecord.isPresent()) {
            this.userEventRecordRepository.deleteById(Objects.requireNonNull(userEventRecord.get().getId()));
        } else {
            throw new ServiceException(ErrorCode.E002, "UserEventRecord not found");
        }
    }

    @Override
    public UserEventRecordDto getUserEventRecordById(String id) {
        log.info("Getting user event record by id: {}", id);
        return this.userEventRecordRepository.findUserEventRecordById(id)
                .map(UserEventRecord::toDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public UserEventDto getUserEventById(String id) {
        log.info("Getting user event by id: {}", id);
        return this.userEventRecordRepository.findUserEventRecordById(id)
                .map(this::getUserEventDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public Page<UserEventRecordDto> getUserEventRecordsByUserAuthId(String authId, Pageable pageable) {
        log.info("Getting user event record by user auth id: {}", authId);
        return this.userEventRecordRepository.findUserEventRecordsByUserAuthId(authId, pageable)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Page<UserEventDto> getUserEventsByUserAuthId(String authId, Pageable pageable) {
        log.info("Getting user events by user auth id: {}", authId);
        return this.userEventRecordRepository.findUserEventRecordsByUserAuthId(authId, pageable)
                .map(this::getUserEventDto);
    }

    @Override
    public Page<UserEventRecordDto> getUserEventRecordsByEventId(String eventId, Pageable pageable) {
        log.info("Getting user event record by event id: {}", eventId);
        return this.userEventRecordRepository.findUserEventRecordsByEventId(eventId, pageable)
                .map(UserEventRecord::toDto);
    }

    @Override
    public Page<UserEventDto> getUserEventsByEventId(String eventId, Pageable pageable) {
        log.info("Getting user events by event id: {}", eventId);
        return this.userEventRecordRepository.findUserEventRecordsByEventId(eventId, pageable)
                .map(this::getUserEventDto);
    }

    Optional<UserEventRecord> getUserEventForUserAuthIdAndEventId(String authId, String eventId) {
        log.info("Getting UserEventRecord for authId: {} and eventId: {}", authId, eventId);
        return this.userEventRecordRepository.findUserEventRecordByUserAuthIdAndEventId(authId, eventId);
    }

    private UserEventDto getUserEventDto(UserEventRecord record) {
        return this.eventRepository.findEventById(record.getEventId())
                .map(e -> new UserEventDto().toBuilder()
                        .userAuthId(record.getUserAuthId())
                        .eventDto(e.toDto())
                        .build())
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }
}
