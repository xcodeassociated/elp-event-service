package com.xcodeassociated.service.model;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.helpers.CollectionsByValueComparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Document()
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UserData extends ComparableBaseDocument<UserData> {

    @Indexed
    @NotNull(message = "User Auth Id title must not be null")
    private String userAuthID;

    private Set<EventCategory> userPreferredCategories;

    private Long maxDistance;

    public static UserData fromDto(UserDataDto dto) {
        return new UserData().toBuilder()
                .userAuthID(dto.getUserAuthID())
                .userPreferredCategories(dto.getUserPreferredCategories().stream()
                        .map(EventCategory::fromDto)
                        .collect(Collectors.toSet()))
                .maxDistance(dto.getMaxDistance())
                .build();
    }

    public UserData update(UserDataDto dto) {
        if (!this.userAuthID.equals(dto.getUserAuthID())) {
            throw new ServiceException(ErrorCode.S000, "Changing userAuthID is not allowed in UserData");
        }
        Set<EventCategory> newUserPreferredCategories = dto.getUserPreferredCategories().stream()
                .map(EventCategory::fromDto)
                .collect(Collectors.toSet());
        if (!CollectionsByValueComparator.areCollectionsSame(this.userPreferredCategories, newUserPreferredCategories)) {
            this.userPreferredCategories.clear();
            this.userPreferredCategories.addAll(newUserPreferredCategories);
        }

        this.maxDistance = dto.getMaxDistance();

        return this;
    }

    @Override
    public boolean compare(UserData other) {
        return StringUtils.equals(this.userAuthID, other.getUserAuthID())
                && ObjectUtils.compare(this.maxDistance, other.getMaxDistance()) == 0
                && CollectionsByValueComparator.areCollectionsSame(this.userPreferredCategories, other.getUserPreferredCategories());
    }

    public UserDataDto toDto() {
        return UserDataDto.builder()
                .id(getId())
                .uuid(getUuid())
                .createdDate(this.getCreatedDate())
                .version(this.getVersion())
                .lastModifiedDate(this.getLastModifiedDate())
                .createdBy(this.getCreatedBy())
                .modifiedBy(this.getModifiedBy())
                .userAuthID(this.userAuthID)
                .userPreferredCategories(this.userPreferredCategories.stream()
                        .map(EventCategory::toDto)
                        .collect(Collectors.toSet()))
                .maxDistance(this.maxDistance)
                .build();
    }
}