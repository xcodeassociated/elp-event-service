package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.SpotDto;
import com.xcodeassociated.service.model.dto.UserEventRecordDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Spot extends ComparableBaseDocument<Spot> {

    @Indexed
    @NotNull(message = "Event title must not be null")
    private String title;

    private String description;

    @NotNull
    private Double[] location;

    public static Spot fromDto(SpotDto dto) {
        return new Spot().toBuilder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .build();
    }

    @Override
    public boolean compare(Spot other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && List.of(this.location).equals(List.of(other.getLocation()));
    }

    public SpotDto toDto() {
        return SpotDto.builder()
                .id(getId())
                .uuid(getUuid())
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .title(this.title)
                .description(this.description)
                .location(this.location)
                .build();
    }
}
