package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.service.EventCategoryCommand;
import com.xcodeassociated.service.service.EventCategoryQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/category")
public class EventCategoryControllerV1 {
    private final EventCategoryQuery eventCategoryQuery;
    private final EventCategoryCommand eventCategoryCommand;

    @GetMapping()
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<EventCategoryDto>> getAll() {
        log.info("Getting all categories");
        return new ResponseEntity<>(this.eventCategoryQuery.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventCategoryDto> getEventCategoryById(@PathVariable String id) {
        log.info("Getting category by id: {}", id);
        return new ResponseEntity<>(this.eventCategoryQuery.getEventCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("/{ids}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<List<EventCategoryDto>> getEventCategoryByIds(@PathVariable String[] ids) {
        log.info("Getting categories for ids: {}", List.of(ids));
        return new ResponseEntity<>(this.eventCategoryQuery.getEventCategoryByIds(List.of(ids)), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<EventCategoryDto> saveCategory(@RequestBody EventCategoryDto dto) {
        log.info("Saving category: {}", dto);
        return new ResponseEntity<>(this.eventCategoryCommand.saveCategory(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable String id) {
        log.info("Deleting category: {}", id);
        this.eventCategoryCommand.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
