package com.xcodeassociated.service.model.domain.dto;

import com.xcodeassociated.service.model.db.dto.BaseEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class UserEventDto extends BaseEntityDto {
    private String userAuthId;
    private EventDto eventDto;
}
