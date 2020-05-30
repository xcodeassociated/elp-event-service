package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Location {
    private String latitude;
    private String longitude;

    public static Location fromDto(LocationDto dto) {
        return new Location().toBuilder()
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    public LocationDto toDto() {
        return new LocationDto().toBuilder()
                .latitude(Optional.ofNullable(this.latitude).orElse(null))
                .longitude(Optional.ofNullable(this.longitude).orElse(null))
                .build();
    }
}
