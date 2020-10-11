package com.xcodeassociated.service.model.domain;

import com.xcodeassociated.service.model.domain.dto.EventDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Value
@With
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class Event extends BaseDomainObject {
    @NotNull
    private final String title;

    private final String description;

    @NotNull
    private final Double[] location;

    @NotNull
    private final Long start;

    @NotNull
    private final Long stop;

    private final List<EventCategory> eventCategories;

    public EventDto toDto() {
        return EventDto.builder()
                .id(this.getId().orElse(null))
                .version(this.getVersion().orElse(null))
                .uuid(this.getUuid().orElse(null))
                .createdDate(this.getCreatedDate().map(Instant::toEpochMilli).orElse(null))
                .lastModifiedDate(this.getModifiedDate().map(Instant::toEpochMilli).orElse(null))
                .createdBy(this.getCreatedBy().orElse(null))
                .modifiedBy(this.getModifiedBy().orElse(null))
                .title(this.getTitle())
                .description(this.getDescription())
                .location(this.getLocation())
                .start(this.getStart())
                .stop(this.getStop())
                .categories(this.getEventCategories().stream()
                        .map(EventCategory::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
