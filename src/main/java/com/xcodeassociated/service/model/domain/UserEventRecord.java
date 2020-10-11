package com.xcodeassociated.service.model.domain;

import com.xcodeassociated.service.model.domain.dto.UserEventRecordDto;
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
public class UserEventRecord extends BaseDomainObject {
    private String userAuthId;

    @NotNull(message = "Event must not be null")
    private Event event;

    public UserEventRecordDto toDto() {
        return UserEventRecordDto.builder()
                .id(this.getId().orElse(null))
                .version(this.getVersion().orElse(null))
                .uuid(this.getUuid().orElse(null))
                .createdDate(this.getCreatedDate().map(Instant::toEpochMilli).orElse(null))
                .lastModifiedDate(this.getModifiedDate().map(Instant::toEpochMilli).orElse(null))
                .createdBy(this.getCreatedBy().orElse(null))
                .modifiedBy(this.getModifiedBy().orElse(null))
                .userAuthId(this.getUserAuthId())
                .eventId(this.getEvent().getId().orElseThrow())
                .build();
    }
}
