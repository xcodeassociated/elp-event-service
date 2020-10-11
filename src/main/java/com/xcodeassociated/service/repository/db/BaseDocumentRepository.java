package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface BaseDocumentRepository<T extends BaseDocument, ID>
        extends MongoRepository<T, ID>, QueryByExampleExecutor<T>, QuerydslPredicateExecutor<T> {}
