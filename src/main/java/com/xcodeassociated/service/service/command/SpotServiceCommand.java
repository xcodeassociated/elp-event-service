package com.xcodeassociated.service.service.command;

import com.xcodeassociated.service.model.domain.dto.SpotDto;

public interface SpotServiceCommand {
    SpotDto saveSpot(SpotDto dto);
    void deleteSpotById(String id);
}
