package com.xcodeassociated.service.model.domain;

import com.xcodeassociated.service.model.domain.dto.SpotDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
@With
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class Spot extends BaseDomainObject {
    private String title;

    private String description;

    @NotNull
    private Double[] location;

    public SpotDto toDto() {
        return SpotDto.builder()
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
                .build();
    }

}
