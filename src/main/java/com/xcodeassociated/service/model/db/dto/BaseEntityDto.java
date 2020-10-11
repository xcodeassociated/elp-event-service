package com.xcodeassociated.service.model.db.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class BaseEntityDto {
    protected String id;
    protected String uuid;
    protected Long createdDate;
    protected Long version;
    protected Long lastModifiedDate;
    protected String createdBy;
    protected String modifiedBy;
}
