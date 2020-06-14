package com.xcodeassociated.service.repository;

import com.xcodeassociated.service.model.UserData;
import reactor.core.publisher.Mono;

public interface UserDataRepository extends BaseRepository<UserData, String> {
    Mono<UserData> getUserDataByUserAuthID(String authId);
    Mono<UserData> save(UserData userData);
    Mono<Void> deleteByUserAuthID(String authId);
}
