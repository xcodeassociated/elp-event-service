package com.xcodeassociated.service.repository.domain;

import com.xcodeassociated.service.model.domain.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface UserDataRepository {
    Optional<UserData> findUserDataByUserAuthID(String authId);

    UserData save(@NotNull UserData userDataDocument);

    void deleteByUserAuthID(String authId);
}
