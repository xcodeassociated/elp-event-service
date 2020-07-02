package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ObjectNotFoundException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.Spot;
import com.xcodeassociated.service.model.dto.SpotDto;
import com.xcodeassociated.service.repository.SpotRepository;
import com.xcodeassociated.service.service.SpotServiceCommand;
import com.xcodeassociated.service.service.SpotServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SpotService implements SpotServiceQuery, SpotServiceCommand {

    private final SpotRepository spotRepository;

    @Override
    public SpotDto saveSpot(SpotDto dto) {
        log.info("Saving spot from dto: {}", dto);
        return this.spotRepository.save(Spot.fromDto(dto)).toDto();
    }

    @Override
    public void deleteSpotById(String id) {
        log.info("Deleting spot by id: {}", id);
        this.spotRepository.deleteSpotById(id);
    }

    @Override
    public Page<SpotDto> getAllSpots(Pageable pageable) {
        log.info("Getting all spots paged: {}", pageable);
        return this.spotRepository.findAll(pageable).map(Spot::toDto);
    }

    @Override
    public SpotDto getSpotById(String id) {
        return this.spotRepository.findSpotById(id)
                .map(Spot::toDto)
                .orElseThrow(() ->  new ObjectNotFoundException(ErrorCode.E001, "id: %s", id));
    }
}
