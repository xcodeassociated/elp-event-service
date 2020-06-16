package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.BaseDocument;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveTemplateRepository<T extends BaseDocument> {
    Mono<T> findEventByQuery(Query query, Class<T> cls);
    Flux<T> findAllEventsByQuery(Query query, Class<T> cls);
}
