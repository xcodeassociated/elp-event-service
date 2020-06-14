package com.xcodeassociated.service.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class UserDataDto extends BaseEntityDto {
    private String userAuthID; // note: this value should be taken from user oauth token
    private Set<EventCategoryDto> userPreferredCategories;
    private Long maxDistance;
}
