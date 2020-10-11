package com.xcodeassociated.service.model.db;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UserDataDocument extends ComparableBaseDocument<UserDataDocument> {

    @Indexed
    @NotNull(message = "User Auth Id title must not be null")
    private String userAuthID;

    private Set<String> userPreferredCategories;

    private Double maxDistance;

    @Override
    public boolean compare(UserDataDocument other) {
        return StringUtils.equals(this.userAuthID, other.getUserAuthID())
                && ObjectUtils.compare(this.maxDistance, other.getMaxDistance()) == 0
                && this.userPreferredCategories.equals(other.getUserPreferredCategories());
    }

}
