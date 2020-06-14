package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.LocationDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Optional;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Location extends ComparableBaseDocument<Location> {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static Location fromDto(LocationDto dto) {
        return new Location().toBuilder()
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    // note: update not designed for this document

    @Override
    public boolean compare(Location location) {
        return ObjectUtils.compare(this.latitude, location.getLatitude()) == 0
                && ObjectUtils.compare(this.longitude, location.getLongitude()) == 0;
    }

    public LocationDto toDto() {
        return new LocationDto().toBuilder()
                .latitude(Optional.ofNullable(this.latitude).orElse(null))
                .longitude(Optional.ofNullable(this.longitude).orElse(null))
                .build();
    }
}
