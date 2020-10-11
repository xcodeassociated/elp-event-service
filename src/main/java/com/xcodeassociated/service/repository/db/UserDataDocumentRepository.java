package com.xcodeassociated.service.repository.db;

import com.xcodeassociated.service.model.db.UserDataDocument;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UserDataDocumentRepository extends BaseDocumentRepository<UserDataDocument, String> {
    Optional<UserDataDocument> findUserDataByUserAuthID(String authId);

    UserDataDocument save(@NotNull UserDataDocument userDataDocument);

    void deleteByUserAuthID(String authId);
}
