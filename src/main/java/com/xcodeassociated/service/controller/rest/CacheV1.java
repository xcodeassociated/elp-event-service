package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.cache.EventCounter;
import com.xcodeassociated.service.repository.cache.EventCounterCacheRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Profile("!test")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/cache")
public class CacheV1 {
    private final EventCounterCacheRepository cacheRepository;

    @GetMapping("/get/{key}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<EventCounter> get(@PathVariable("key") String key) {
        return cacheRepository.findByKey(key);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<EventCounter> create(@RequestBody EventCounter counter) {
        return cacheRepository.save(counter);
    }

    @DeleteMapping("/delete/{key}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Boolean> delete(@PathVariable("key") String key) {
        return cacheRepository.delete(key);
    }

}
