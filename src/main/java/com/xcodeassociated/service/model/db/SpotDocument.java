package com.xcodeassociated.service.model.db;

import lombok.*;
import lombok.experimental.SuperBuilder;
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
public class SpotDocument extends ComparableBaseDocument<SpotDocument> {

    @Indexed
    @NotNull(message = "Event title must not be null")
    private String title;

    private String description;

    @NotNull
    private Double[] location;

    @Override
    public boolean compare(SpotDocument other) {
        return StringUtils.equals(this.title, other.getTitle())
                && StringUtils.equals(this.description, other.getDescription())
                && List.of(this.location).equals(List.of(other.getLocation()));
    }

}
