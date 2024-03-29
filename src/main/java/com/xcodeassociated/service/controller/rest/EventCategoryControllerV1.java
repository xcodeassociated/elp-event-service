package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;
import com.xcodeassociated.service.service.command.EventCategoryServiceCommand;
import com.xcodeassociated.service.service.query.EventCategoryServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/category")
public class EventCategoryControllerV1 {
    private final EventCategoryServiceQuery eventCategoryServiceQuery;
    private final EventCategoryServiceCommand eventCategoryServiceCommand;

    @GetMapping("/paged")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Page<EventCategoryDto>> getAll(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                                         @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        log.info("Processing `getAll` request in EventCategoryControllerV1, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        return new ResponseEntity<>(this.eventCategoryServiceQuery.getAllCategories(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventCategoryDto> getEventCategoryById(@PathVariable String id) {
        log.info("Getting category by id: {}", id);
        return new ResponseEntity<>(this.eventCategoryServiceQuery.getEventCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/{ids}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<EventCategoryDto>> getEventCategoryByIds(@PathVariable String[] ids) {
        log.info("Getting categories for ids: {}", List.of(ids));
        return new ResponseEntity<>(new ArrayList<>(this.eventCategoryServiceQuery.getEventCategoryByIds(List.of(ids))), HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventCategoryDto> saveCategory(@RequestBody EventCategoryDto dto) {
        log.info("Saving category: {}", dto);
        return new ResponseEntity<>(this.eventCategoryServiceCommand.saveCategory(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable String id) {
        log.info("Deleting category: {}", id);
        this.eventCategoryServiceCommand.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
