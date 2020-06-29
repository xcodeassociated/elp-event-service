package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import com.xcodeassociated.service.model.dto.EventDto;
import com.xcodeassociated.service.model.dto.EventWithCategoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
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

    private Double[] location;

    private Long start;

    private Long stop;

    private Set<String> eventCategories;

    public static Event fromDto(EventDto dto) {
        return new Event().toBuilder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .createdDate(dto.getCreatedDate())
                .version(dto.getVersion())
                .lastModifiedDate(dto.getLastModifiedDate())
                .createdBy(dto.getCreatedBy())
                .modifiedBy(dto.getModifiedBy())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .start(dto.getStart())
                .stop(dto.getStop())
                .eventCategories(dto.getEventCategories())
                .build();
    }

    @Override
    public boolean compare(Event other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && Objects.equals(List.of(this.location), List.of(other.getLocation()))
                && ObjectUtils.compare(this.start, other.getStart()) == 0
                && ObjectUtils.compare(this.stop, other.getStop()) == 0
                && this.eventCategories.equals(other.getEventCategories());
    }

    public Event update(EventDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.start = dto.getStart();
        this.stop = dto.getStop();

        List<Double> newLocation = List.of(dto.getLocation());
        if (!List.of(this.location).equals(newLocation)) {
            this.location = dto.getLocation();
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
                .location(this.location)
                .eventCategories(this.eventCategories)
                .build();
    }

    public EventWithCategoryDto toDto(List<EventCategory> categories) {
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
                .location(this.location)
                .eventCategories(this.eventCategories)
                .categories(categories.stream().map(EventCategory::toDto).collect(Collectors.toList()))
                .build();
    }

    public EventWithCategoryDto toDto(List<EventCategoryDto> categories, boolean registered) {
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
                .location(this.location)
                .eventCategories(this.eventCategories)
                .categories(categories)
                .registered(registered)
                .build();
    }
}
