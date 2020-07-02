package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.model.dto.SpotDto;
import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.dto.UserDataWithCategoryDto;
import com.xcodeassociated.service.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/spot")
public class SpotControllerV1 {
    private final SpotServiceQuery spotServiceQuery;
    private final SpotServiceCommand spotServiceCommand;

    @GetMapping("/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<SpotDto>> getAll(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                         @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        log.info("Processing `getAll` request in SpotControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.spotServiceQuery.getAllSpots(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<SpotDto> getUserDataByAuthId(@PathVariable String id) {
        log.info("Getting spot by id: {}", id);
        return new ResponseEntity<>(this.spotServiceQuery.getSpotById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<SpotDto> saveUserDataByToken(@RequestBody SpotDto dto) {
        log.info("Saving spot by dto: {}", dto);
        return new ResponseEntity<>(this.spotServiceCommand.saveSpot(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserDataByAuthId(@PathVariable String id) {
        log.info("Deleting user data for auth id: {}", id);
        this.spotServiceCommand.deleteSpotById(id);
        return ResponseEntity.ok().build();
    }

}
