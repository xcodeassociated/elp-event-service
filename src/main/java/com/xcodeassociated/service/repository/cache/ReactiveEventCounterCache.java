package com.xcodeassociated.service.repository.cache;

import com.xcodeassociated.service.model.cache.EventCounter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Profile("!test")
@Repository
@AllArgsConstructor
public class ReactiveEventCounterCache implements EventCounterCacheRepository {
    private final ReactiveRedisTemplate<String, EventCounter> redisTemplate;

    @Override
    public Mono<EventCounter> save(EventCounter data) {
        return this.redisTemplate.opsForValue()
                .set(data.getKey(), data)
                .map(__ -> data);
    }

    @Override
    public Mono<EventCounter> findByKey(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return this.redisTemplate.opsForValue().delete(key);
    }
}
