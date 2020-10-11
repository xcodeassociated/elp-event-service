package com.xcodeassociated.service.model.domain;

import com.xcodeassociated.service.model.domain.dto.EventCategoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Value
@With
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class EventCategory extends BaseDomainObject {
    private String title;

    private String description;

    public EventCategoryDto toDto() {
        return EventCategoryDto.builder()
                .id(this.getId().orElse(null))
                .version(this.getVersion().orElse(null))
                .uuid(this.getUuid().orElse(null))
                .createdDate(this.getCreatedDate().map(Instant::toEpochMilli).orElse(null))
                .lastModifiedDate(this.getModifiedDate().map(Instant::toEpochMilli).orElse(null))
                .createdBy(this.getCreatedBy().orElse(null))
                .modifiedBy(this.getModifiedBy().orElse(null))
                .title(this.getTitle())
                .description(this.getDescription())
                .build();

    }
}
