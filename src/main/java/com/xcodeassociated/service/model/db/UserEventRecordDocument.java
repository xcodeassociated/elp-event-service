package com.xcodeassociated.service.model.db;

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
public class UserEventRecordDocument extends ComparableBaseDocument<UserEventRecordDocument> {

    @Indexed
    @NotNull(message = "User Auth Id title must not be null")
    private String userAuthId;

    @NotNull(message = "Event Id must not be null")
    private String eventId;

    @Override
    public boolean compare(UserEventRecordDocument other) {
        return StringUtils.equals(this.userAuthId, other.getUserAuthId())
                && StringUtils.equals(this.eventId, other.getEventId());
    }

}
