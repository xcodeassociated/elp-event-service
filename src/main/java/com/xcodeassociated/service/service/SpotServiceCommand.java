package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.SpotDto;

public interface SpotServiceCommand {
    SpotDto saveSpot(SpotDto dto);
    void deleteSpotById(String id);
}
