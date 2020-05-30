package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.EventDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Event extends ComparableBaseDocument<Event> {

    @Indexed
    @NotNull(message = "Event title must not be null")
    private String title;

    private String description;

    private Location location;

    public static Event fromDto(EventDto dto) {
        return new Event().toBuilder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(Location.fromDto(dto.getLocation()))
                .build();
    }

    @Override
    public boolean compare(Event other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && Objects.equals(this.location, other.getLocation());
    }

    public EventDto toDto() {
        return EventDto.builder()
                .id(getId())
                .uuid(getUuid())
                .title(this.title)
                .description(this.description)
                .location(Optional.ofNullable(this.location).map(Location::toDto).orElse(null))
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .build();
    }
}
