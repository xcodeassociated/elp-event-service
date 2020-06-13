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
public class EventSearchDto extends BaseEntityDto {
    private String title;
    private LocationDto location;
    private Long range;
    private Long start;
    private Long stop;
    private Set<EventCategoryDto> eventCategories;
}
