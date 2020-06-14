package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;
import reactor.core.publisher.Mono;

public interface UserDataServiceCommand {
    Mono<UserDataDto> saveUserData(UserDataDto dto);
    Mono<Void> deleteUserData(String authId);
}
