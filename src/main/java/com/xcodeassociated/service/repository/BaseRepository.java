package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T extends BaseDocument, ID>
        extends ReactiveCrudRepository<T, ID>, ReactiveQueryByExampleExecutor<T>, ReactiveSortingRepository<T, ID> {}
