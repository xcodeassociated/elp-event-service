package com.xcodeassociated.service.model.domain.dto;

import com.xcodeassociated.service.model.db.dto.BaseEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class UserDataDto extends BaseEntityDto {
    private String userAuthID;
    private Double maxDistance;
    private List<EventCategoryDto> preferredCategories;
}
