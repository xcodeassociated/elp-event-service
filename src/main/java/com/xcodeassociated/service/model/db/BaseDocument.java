package com.xcodeassociated.service.model.db;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDocument implements Persistable<String> {

    public static BaseDocument DEFAULT = new BaseDocument() {};

    @Id
    private String id;

    @EqualsAndHashCode.Include
    private String uuid = UUID.randomUUID().toString();

    @CreatedDate
    private Long createdDate;

    @LastModifiedDate
    private Long lastModifiedDate;

    @CreatedBy
    private String createdBy;

    // note: has to set manually
    private String modifiedBy;

    @Version
    private Long version;

    @Override
    public boolean isNew() {
        return id == null;
    }

}
