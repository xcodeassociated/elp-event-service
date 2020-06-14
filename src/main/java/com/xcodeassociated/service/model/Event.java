package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import com.xcodeassociated.service.model.helpers.CollectionsByValueComparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Long start;

    private Long stop;

    private Set<String> eventCategories;

    public static Event fromDto(EventDto dto) {
        return new Event().toBuilder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(Location.fromDto(dto.getLocation()))
                .start(dto.getStart())
                .stop(dto.getStop())
                .eventCategories(dto.getEventCategories())
                .build();
    }

    @Override
    public boolean compare(Event other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && Objects.equals(this.location, other.getLocation())
                && ObjectUtils.compare(this.start, other.getStart()) == 0
                && ObjectUtils.compare(this.stop, other.getStop()) == 0
                && this.eventCategories.equals(other.getEventCategories());
    }

    public Event update(EventDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.start = dto.getStart();
        this.stop = dto.getStop();

        Location newLocation = Location.fromDto(dto.getLocation());
        if (!this.location.compare(newLocation)) {
            this.location = newLocation;
        }

        Set<String> newEventCategories = dto.getEventCategories();
        if (!this.eventCategories.equals(dto.getEventCategories())) {
            this.eventCategories.clear();
            this.eventCategories.addAll(newEventCategories);
        }

        return this;
    }

    public EventDto toDto() {
        return EventDto.builder()
                .id(getId())
                .uuid(getUuid())
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .title(this.title)
                .start(this.start)
                .stop(this.stop)
                .description(this.description)
                .location(Optional.ofNullable(this.location)
                        .map(Location::toDto).orElse(null))
                .eventCategories(this.eventCategories)
                .build();
    }

    public EventWithCategoryDto toDto(Set<EventCategory> categories) {
        return EventWithCategoryDto.builder()
                .id(getId())
                .uuid(getUuid())
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .title(this.title)
                .start(this.start)
                .stop(this.stop)
                .description(this.description)
                .location(Optional.ofNullable(this.location)
                        .map(Location::toDto).orElse(null))
                .eventCategories(this.eventCategories)
                .categories(categories.stream()
                        .map(EventCategory::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
