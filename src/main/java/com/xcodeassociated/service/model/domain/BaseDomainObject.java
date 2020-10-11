package com.xcodeassociated.service.model.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Optional;

@With
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseDomainObject {
    private String id;
    private String uuid;
    private Instant createdDate;
    private Instant modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private Long version;

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getUuid() {
        return Optional.ofNullable(uuid);
    }

    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public Optional<Instant> getModifiedDate() {
        return Optional.ofNullable(modifiedDate);
    }

    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public Optional<String> getModifiedBy() {
        return Optional.ofNullable(modifiedBy);
    }

    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }
}
