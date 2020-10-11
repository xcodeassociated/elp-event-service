package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.domain.Spot;
import com.xcodeassociated.service.model.domain.dto.SpotDto;
import com.xcodeassociated.service.repository.domain.SpotRepository;
import com.xcodeassociated.service.repository.domain.provider.DomainObjectUtils;
import com.xcodeassociated.service.service.command.SpotServiceCommand;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import com.xcodeassociated.service.service.query.SpotServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SpotService implements SpotServiceQuery, SpotServiceCommand {
    private final OauthAuditorServiceQuery oauthAuditorServiceQuery;
    private final SpotRepository spotRepository;

    @Override
    public SpotDto saveSpot(SpotDto dto) {
        log.info("Saving spot from dto: {}", dto);

        return Optional.ofNullable(dto.getId())
                .map(id -> this.spotRepository.findSpotById(id)
                        .map(e -> this.updateSpotFromDto(e, dto)))
                .orElse(Optional.of(this.createSpotFromDto(dto)))
                .map(this.spotRepository::save)
                .map(Spot::toDto)
                .orElseThrow();
    }

    @Override
    public void deleteSpotById(String id) {
        log.info("Deleting spot by id: {}", id);

        this.spotRepository.deleteById(id);
    }

    @Override
    public Page<SpotDto> getAllSpots(Pageable pageable) {
        log.info("Getting all spots paged: {}", pageable);

        return this.spotRepository.findAll(pageable).map(Spot::toDto);
    }

    @Override
    public SpotDto getSpotById(String id) {
        return this.spotRepository.findSpotById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ErrorCode.E001, "id: %s", id))
                .toDto();
    }

    private Spot createSpotFromDto(SpotDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();
        return Spot.builder()
                .modifiedBy(modificationAuthor)
                .location(Optional.ofNullable(dto.getLocation()).orElseThrow())
                .title(Optional.ofNullable(dto.getTitle()).orElseThrow())
                .description(dto.getDescription())
                .build();
    }

    private Spot updateSpotFromDto(Spot spot, SpotDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();
        var builder = DomainObjectUtils.copyBaseDomainObject(Spot.builder(), spot);

        return builder
                .modifiedBy(modificationAuthor)
                .location(Optional.ofNullable(dto.getLocation()).orElseThrow())
                .title(Optional.ofNullable(dto.getTitle()).orElseThrow())
                .description(dto.getDescription())
                .build();
    }
}
