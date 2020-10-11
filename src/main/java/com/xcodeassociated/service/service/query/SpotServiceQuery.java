package com.xcodeassociated.service.service.query;

import com.xcodeassociated.service.model.domain.dto.SpotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpotServiceQuery {
    Page<SpotDto> getAllSpots(Pageable pageable);
    SpotDto getSpotById(String id);
}
