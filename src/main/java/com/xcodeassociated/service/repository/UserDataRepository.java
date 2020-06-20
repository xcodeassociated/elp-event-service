package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserData;

import java.util.Optional;

public interface UserDataRepository extends BaseRepository<UserData, String> {
    Optional<UserData> findUserDataByUserAuthID(String authId);
    UserData save(UserData userData);
    void deleteByUserAuthID(String authId);
}
