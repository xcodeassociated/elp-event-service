package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.domain.UserEventRecord;
import com.xcodeassociated.service.model.domain.dto.UserEventDto;
import com.xcodeassociated.service.model.domain.dto.UserEventRecordDto;
import com.xcodeassociated.service.repository.domain.EventRepository;
import com.xcodeassociated.service.repository.domain.UserEventRecordRepository;
import com.xcodeassociated.service.service.command.UserHistoryServiceCommand;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import com.xcodeassociated.service.service.query.UserHistoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserHistoryService implements UserHistoryServiceQuery, UserHistoryServiceCommand {
    private final OauthAuditorServiceQuery oauthAuditorServiceQuery;
    private final UserEventRecordRepository userEventRecordRepository;
    private final EventRepository eventRepository;

    @Override
    public UserEventRecordDto registerUserEventRecord(UserEventRecordDto dto) {
        log.info("Registering user event record: {}", dto);

        Optional<UserEventRecord> record = this.userEventRecordRepository.findUserEventRecordByUserAuthIdAndEventId(dto.getUserAuthId(), dto.getEventId());
        return record.isPresent() ?
                record.get().toDto() : this.userEventRecordRepository.save(this.createUserEventRecordFromDto(dto)).toDto();
    }

    @Override
    public void deleteUserEventRecord(String id) {
        log.info("Deleting user event record by id: {}", id);

        this.userEventRecordRepository.deleteById(id);
    }

    @Override
    public void deleteUserEventRecordByAuthIdAndEventId(String authId, String eventId) {
        log.info("Deleting user event record by authId: {} and eventId: {}", authId, eventId);

        this.userEventRecordRepository.findUserEventRecordByUserAuthIdAndEventId(authId, eventId)
                .ifPresentOrElse(e -> this.userEventRecordRepository.deleteById(e.getId().orElseThrow()),
                () -> { throw new ServiceException(ErrorCode.E002, "UserEventRecord not found"); });
    }

    @Override
    public UserEventRecordDto getUserEventRecordById(String id) {
        log.info("Getting user event record by id: {}", id);

        return this.userEventRecordRepository.findUserEventRecordById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""))
                .toDto();
    }

    @Override
    public UserEventDto getUserEventById(String id) {
        log.info("Getting user event by id: {}", id);

        return this.getUserEventDto(this.userEventRecordRepository.findUserEventRecordById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, "")));
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

    Optional<UserEventRecordDto> getUserEventForUserAuthIdAndEventId(String authId, String eventId) {
        log.info("Getting UserEventRecord for authId: {} and eventId: {}", authId, eventId);

        return this.userEventRecordRepository.findUserEventRecordByUserAuthIdAndEventId(authId, eventId)
                .map(UserEventRecord::toDto);
    }

    private UserEventDto getUserEventDto(UserEventRecord record) {
        return this.eventRepository.findEventById(record.getEvent().getId().orElseThrow())
                .map(e -> new UserEventDto().toBuilder()
                    .userAuthId(record.getUserAuthId())
                    .eventDto(e.toDto())
                    .build())
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    private UserEventRecord createUserEventRecordFromDto(UserEventRecordDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();

        return UserEventRecord.builder()
                .modifiedBy(modificationAuthor)
                .userAuthId(Optional.ofNullable(dto.getUserAuthId()).orElseThrow())
                .event(this.eventRepository.findEventById(Optional.ofNullable(dto.getEventId()).orElseThrow()).orElseThrow())
                .build();
    }
}
