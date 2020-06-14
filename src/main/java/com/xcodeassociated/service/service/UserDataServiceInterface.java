package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;
import reactor.core.publisher.Mono;

public interface UserDataServiceInterface {
    Mono<UserDataDto> getUserDataBuAuthId(String authId);
    Mono<UserDataDto> saveUserData(UserDataDto dto);
    Mono<Void> deleteUserData(String authId);
}
