package com.xcodeassociated.service.repository.implementation;

import com.xcodeassociated.service.model.BaseDocument;
import com.xcodeassociated.service.repository.ReactiveTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class ReactiveTemplate<T extends BaseDocument> implements ReactiveTemplateRepository<T> {

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
