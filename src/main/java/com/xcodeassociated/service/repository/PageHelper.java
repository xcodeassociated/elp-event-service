package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class PageHelper {
    private PageHelper() {
        throw new ServiceException(ErrorCode.S000, "PageHelper should not be instantiated");
    }

    static public  <T> Flux<T> apply(Flux<T> reactive, Pageable pageable) {
        return reactive
                .skip(pageable.getPageSize() * pageable.getPageSize())
                .take(pageable.getPageSize());
    }
}
