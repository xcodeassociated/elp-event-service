package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface BaseRepository<T extends BaseDocument, ID>
        extends MongoRepository<T, ID>, QueryByExampleExecutor<T>, QuerydslPredicateExecutor<T> {}
