package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SpotRepository extends BaseRepository<Spot, String> {
    Page<Spot> findAll(Pageable pageable);
    Optional<Spot> findSpotById(String id);
    Spot save(Spot spot);
    void deleteSpotById(String id);
}
