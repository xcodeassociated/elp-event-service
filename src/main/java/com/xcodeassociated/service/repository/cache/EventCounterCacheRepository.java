package com.xcodeassociated.service.repository.cache;

import com.xcodeassociated.service.model.cache.EventCounter;
import reactor.core.publisher.Mono;

public interface EventCounterCacheRepository {
    Mono<EventCounter> save(EventCounter link);
    Mono<EventCounter> findByKey(String key);
    Mono<Boolean> delete(String key);
}
