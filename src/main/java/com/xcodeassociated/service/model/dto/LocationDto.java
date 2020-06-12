package com.xcodeassociated.service.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class LocationDto extends BaseEntityDto {
    private Long latitude;
    private Long longitude;
}
