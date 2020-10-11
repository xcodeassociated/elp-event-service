package com.xcodeassociated.service.model.domain.dto;

import com.xcodeassociated.service.model.db.dto.BaseEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class EventDto extends BaseEntityDto {
    @Data
    @Builder
    public static class UserDetailsDto {
        private Boolean registered;
    }

    private String title;
    private String description;
    private Double[] location;
    private Long start;
    private Long stop;
    private List<EventCategoryDto> categories;
    private UserDetailsDto userDetails;
}
