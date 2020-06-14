package com.xcodeassociated.service.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class LocationDto extends BaseEntityDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
