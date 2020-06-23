package com.xcodeassociated.service.service.implementation.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.QEvent;
import com.xcodeassociated.service.model.dto.BaseEntityDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.service.implementation.helper.EventSearchDtoHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventQuery {
    private EventQuery() {
        throw new ServiceException(ErrorCode.E002, "EventQuery should not be instantiated");
    }

    public static Optional<BooleanExpression> toPredicate(EventSearchDto dto) {
        if (!EventSearchDtoHelper.dtoSearchable(dto)) {
            return Optional.empty();
        }

        QEvent q = QEvent.event;
        return Stream.of(
                Optional.ofNullable(dto.getTitle())
                        .filter(StringUtils::isNotBlank)
                        .map(q.title::like),
                Optional.ofNullable(dto.getStart())
                        .map(q.start::goe),
                Optional.ofNullable(dto.getStop())
                        .map(q.start::loe),
                Optional.ofNullable(dto.getEventCategories())
                        .map(c -> c.stream()
                                .filter(Objects::nonNull)
                                .map(BaseEntityDto::getId)
                                .filter(StringUtils::isNoneBlank)
                                .collect(Collectors.toSet())
                        )
                        .map(p -> q.eventCategories.any().in(p))
        ).filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(BooleanExpression::and);
    }
}
