package com.xcodeassociated.service.repository.implementation;

import com.xcodeassociated.service.model.BaseDocument;
import com.xcodeassociated.service.repository.EventTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class EventTemplate<T extends BaseDocument> implements EventTemplateRepository<T> {

    final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<T> findEventByQuery(Query query, Class<T> clazz) {
        return this.reactiveMongoTemplate.findOne(query, clazz);
    }

    @Override
    public Flux<T> findAllEventsByQuery(Query query, Class<T> clazz) {
        return this.reactiveMongoTemplate.find(query, clazz);
    }
}
