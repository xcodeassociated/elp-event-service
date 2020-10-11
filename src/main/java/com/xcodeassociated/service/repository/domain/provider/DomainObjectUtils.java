package com.xcodeassociated.service.repository.domain.provider;

import com.xcodeassociated.service.model.db.BaseDocument;
import com.xcodeassociated.service.model.domain.BaseDomainObject;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainObjectUtils {
    private DomainObjectUtils() {
        throw new RuntimeException("Constructor called for a utility class");
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseDomainObject, U extends BaseDocument.BaseDocumentBuilder<?, ?>> U toBaseDocument(U builder, T object) {
        return (U) builder.id(object.getId()
                        .orElse(BaseDocument.DEFAULT.getId()))
                .uuid(object.getUuid()
                        .orElse(UUID.randomUUID().toString()))
                .createdDate(object.getCreatedDate()
                        .map(Instant::toEpochMilli)
                        .orElse(BaseDocument.DEFAULT.getCreatedDate()))
                .lastModifiedDate(object.getModifiedDate()
                        .map(Instant::toEpochMilli)
                        .orElse(BaseDocument.DEFAULT.getLastModifiedDate()))
                .createdBy(object.getCreatedBy()
                        .orElse(BaseDocument.DEFAULT.getCreatedBy()))
                .modifiedBy(object.getModifiedBy()
                        .orElse(BaseDocument.DEFAULT.getModifiedBy()))
                .version(object.getVersion()
                        .orElse(BaseDocument.DEFAULT.getVersion()));
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseDocument, U extends BaseDomainObject.BaseDomainObjectBuilder<?, ?>> U toBaseDomainObject(U builder, T document) {
        return (U) builder.id(document.getId())
                .version(document.getVersion())
                .uuid(document.getUuid())
                .createdDate(epochMillisToInstant(document.getCreatedDate()).orElse(null))
                .modifiedDate(epochMillisToInstant(document.getLastModifiedDate()).orElse(null))
                .createdBy(document.getCreatedBy())
                .modifiedBy(document.getModifiedBy());
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseDomainObject, U extends BaseDomainObject.BaseDomainObjectBuilder<?, ?>> U copyBaseDomainObject(U builder, T object) {
        return (U) builder.id(object.getId().orElse(null))
                .version(object.getVersion().orElse(null))
                .uuid(object.getUuid().orElse(null))
                .createdDate(object.getCreatedDate().orElse(null))
                .modifiedDate(object.getModifiedDate().orElse(null))
                .createdBy(object.getCreatedBy().orElse(null))
                .modifiedBy(object.getModifiedBy().orElse(null));
    }

    private static Optional<Instant> epochMillisToInstant(long epochMillis) {
        return epochMillis == 0 ? Optional.empty() : Optional.of(Instant.ofEpochMilli(epochMillis));
    }
}
