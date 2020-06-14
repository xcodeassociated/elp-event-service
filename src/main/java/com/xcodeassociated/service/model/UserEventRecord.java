package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UserEventRecord extends ComparableBaseDocument<UserEventRecord> {

    @Indexed
    @NotNull(message = "User Auth Id title must not be null")
    private String userAuthId;

    @NotNull(message = "Event Id must not be null")
    private String eventId;

    public static UserEventRecord fromDto(UserEventRecordDto dto) {
        return new UserEventRecord().toBuilder()
                .userAuthId(dto.getUserAuthId())
                .eventId(dto.getEventId())
                .build();
    }

    // note: update not designed for this document

    @Override
    public boolean compare(UserEventRecord other) {
        return StringUtils.equals(this.userAuthId, other.getUserAuthId())
                && StringUtils.equals(this.eventId, other.getEventId());
    }

    public UserEventRecordDto toDto() {
        return UserEventRecordDto.builder()
                .id(getId())
                .uuid(getUuid())
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .userAuthId(this.userAuthId)
                .eventId(this.eventId)
                .build();
    }
}
