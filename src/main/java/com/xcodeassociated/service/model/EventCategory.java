package com.xcodeassociated.service.model;

import com.xcodeassociated.service.model.dto.EventCategoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class EventCategory extends ComparableBaseDocument<EventCategory> {

    @Indexed
    @NotNull(message = "Category title must not be null")
    private String title;

    private String description;

    public static EventCategory fromDto(EventCategoryDto dto) {
        return new EventCategory().toBuilder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public EventCategory update(EventCategoryDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        return this;
    }

    @Override
    public boolean compare(EventCategory other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription());
    }

    public EventCategoryDto toDto() {
        return EventCategoryDto.builder()
                .id(getId())
                .uuid(getUuid())
                .title(this.title)
                .description(this.description)
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .build();
    }
}
