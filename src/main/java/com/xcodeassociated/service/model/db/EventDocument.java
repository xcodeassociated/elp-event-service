package com.xcodeassociated.service.model.db;

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

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class EventDocument extends ComparableBaseDocument<EventDocument> {

    @Indexed
    @NotNull(message = "Event title must not be null")
    private String title;

    private String description;

    @NotNull
    private Double[] location;

    @NotNull
    private Long start;

    @NotNull
    private Long stop;

    private Set<String> eventCategories;

    @Override
    public boolean compare(EventDocument other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && Objects.equals(List.of(this.location), List.of(other.getLocation()))
                && ObjectUtils.compare(this.start, other.getStart()) == 0
                && ObjectUtils.compare(this.stop, other.getStop()) == 0
                && this.eventCategories.equals(other.getEventCategories());
    }

}
