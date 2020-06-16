package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.BaseDocument;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface BaseRepository<T extends BaseDocument, ID>
        extends ReactiveCrudRepository<T, ID>, ReactiveQueryByExampleExecutor<T> {}
