package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.commons.paging.CustomPageRequest;
import com.xcodeassociated.commons.paging.SortDirection;
import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.service.EventCategoryCommand;
import com.xcodeassociated.service.service.EventCategoryQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/category")
public class EventCategoryControllerV1 {
    private final EventCategoryQuery eventCategoryQuery;
    private final EventCategoryCommand eventCategoryCommand;

    @GetMapping("/paged")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<EventCategoryDto> getAll(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                                         @RequestParam(name = "sort_how", defaultValue = "asc") SortDirection sortDirection) {
        Pageable pageable = new CustomPageRequest(page, size, sortDirection, sortBy).toPageable();
        log.info("Getting all categories, pageable: {}", pageable);
        return this.eventCategoryQuery.getAllCategories(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<EventCategoryDto> getEventCategoryById(@PathVariable String id) {
        log.info("Getting category by id: {}", id);
        return this.eventCategoryQuery.getEventCategoryById(id);
    }

    @GetMapping("/{ids}")
    @PreAuthorize("hasRole('backend_service')")
    public Flux<EventCategoryDto> getEventCategoryByIds(@PathVariable String[] ids) {
        log.info("Getting categories for ids: {}", List.of(ids));
        return this.eventCategoryQuery.getEventCategoryByIds(List.of(ids));
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<EventCategoryDto> saveCategory(@RequestBody EventCategoryDto dto) {
        log.info("Saving category: {}", dto);
        return this.eventCategoryCommand.saveCategory(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteCategoryById(@PathVariable String id) {
        log.info("Deleting category: {}", id);
        return this.eventCategoryCommand.deleteCategoryById(id);
    }
}
