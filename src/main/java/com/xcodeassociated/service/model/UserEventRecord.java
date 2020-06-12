package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
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
    private String userAuthID;

    @NotNull(message = "Event must not be null")
    private Event event;

    public static UserEventRecord fromDto(UserEventRecordDto dto) {
        return new UserEventRecord().toBuilder()
                .userAuthID(dto.getUserAuthID())
                .event(Event.fromDto(dto.getEvent()))
                .build();
    }

    @Override
    public boolean compare(UserEventRecord other) {
        return StringUtils.equals(this.userAuthID, other.getUserAuthID())
                && event.compare(other.getEvent());
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
                .userAuthID(this.userAuthID)
                .event(this.event.toDto())
                .build();
    }
}
