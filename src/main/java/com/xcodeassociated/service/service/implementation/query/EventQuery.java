package com.xcodeassociated.service.service.implementation.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.QEvent;
import com.xcodeassociated.service.model.dto.BaseEntityDto;
import com.xcodeassociated.service.model.dto.EventSearchDto;
import com.xcodeassociated.service.service.implementation.helper.EventSearchDtoHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventQuery {
    private EventQuery() {
        throw new ServiceException(ErrorCode.E002, "EventQuery should not be instantiated");
    }

    public static Optional<BooleanExpression> toPredicate(EventSearchDto dto, Long currentMillis, boolean active) {
        if (!EventSearchDtoHelper.dtoSearchable(dto)) {
            return Optional.empty();
        }
        return (active) ? toPredicateActive(dto, currentMillis) : toPredicate(dto);
    }

    private static Optional<BooleanExpression> toPredicate(EventSearchDto dto) {
        QEvent q = QEvent.event;
        return Stream.of(
                Optional.ofNullable(dto.getId())
                        .filter(StringUtils::isNotBlank)
                        .map(q.id::eq),
                Optional.ofNullable(dto.getUuid())
                        .filter(StringUtils::isNotBlank)
                        .map(q.uuid::eq),
                Optional.ofNullable(dto.getCreatedBy())
                        .filter(StringUtils::isNotBlank)
                        .map(q.createdBy::eq),
                Optional.ofNullable(dto.getModifiedBy())
                        .filter(StringUtils::isNotBlank)
                        .map(q.modifiedBy::eq),
                Optional.ofNullable(dto.getCreatedDate())
                        .map(q.createdDate::eq),
                Optional.ofNullable(dto.getTitle())
                        .filter(StringUtils::isNotBlank)
                        .map(q.title::like),
                Optional.ofNullable(dto.getStart())
                        .map(q.start::goe),
                Optional.ofNullable(dto.getStop())
                        .map(q.stop::loe),
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

    private static Optional<BooleanExpression> toPredicateActive(EventSearchDto dto, Long currentMillis) {
        QEvent q = QEvent.event;
        return Stream.of(
                Optional.ofNullable(dto.getId())
                        .filter(StringUtils::isNotBlank)
                        .map(q.id::eq),
                Optional.ofNullable(dto.getUuid())
                        .filter(StringUtils::isNotBlank)
                        .map(q.uuid::eq),
                Optional.ofNullable(dto.getCreatedBy())
                        .filter(StringUtils::isNotBlank)
                        .map(q.createdBy::eq),
                Optional.ofNullable(dto.getModifiedBy())
                        .filter(StringUtils::isNotBlank)
                        .map(q.modifiedBy::eq),
                Optional.ofNullable(dto.getTitle())
                        .filter(StringUtils::isNotBlank)
                        .map(q.title::like),
                Optional.ofNullable(currentMillis)
                        .map(q.stop::goe),
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

    @NotNull
    public static Query getLocationQuery(EventSearchDto dto, Pageable pageable, List<String> eventIds, List<Double> location, Metrics metrics) {
        Point point = new Point(location.get(0), location.get(1));
        Distance distance = new Distance(dto.getRange(), metrics);
        Circle circle = new Circle(point, distance);
        Criteria geoCriteria = Criteria.where("location").withinSphere(circle);
        Criteria idCriteria = Criteria.where("id").in(eventIds);
        Query query = Query.query(geoCriteria);
        query.addCriteria(idCriteria);
        query.with(pageable);
        return query;
    }

    @NotNull
    public static Query getLocationQuery(EventSearchDto dto, Pageable pageable, List<Double> location, Metrics metrics) {
        Point point = new Point(location.get(0), location.get(1));
        Distance distance = new Distance(dto.getRange(), metrics);
        Circle circle = new Circle(point, distance);
        Criteria geoCriteria = Criteria.where("location").withinSphere(circle);
        Query query = Query.query(geoCriteria);
        query.with(pageable);
        return query;
    }
}
