package com.xcodeassociated.service.model.domain;

import com.xcodeassociated.service.model.domain.dto.UserDataDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Value
@With
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class UserData extends BaseDomainObject {
    private String userAuthID;

    private List<EventCategory> preferredCategories;

    private Double maxDistance;

    public UserDataDto toDto() {
        return UserDataDto.builder()
                .id(this.getId().orElse(null))
                .version(this.getVersion().orElse(null))
                .uuid(this.getUuid().orElse(null))
                .createdDate(this.getCreatedDate().map(Instant::toEpochMilli).orElse(null))
                .lastModifiedDate(this.getModifiedDate().map(Instant::toEpochMilli).orElse(null))
                .createdBy(this.getCreatedBy().orElse(null))
                .modifiedBy(this.getModifiedBy().orElse(null))
                .userAuthID(this.getUserAuthID())
                .preferredCategories(this.getPreferredCategories().stream()
                        .map(EventCategory::toDto)
                        .collect(Collectors.toList()))
                .maxDistance(this.getMaxDistance())
                .build();
    }
}
