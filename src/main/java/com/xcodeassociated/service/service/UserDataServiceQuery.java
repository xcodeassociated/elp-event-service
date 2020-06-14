package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;
import reactor.core.publisher.Mono;

public interface UserDataServiceQuery {
    Mono<UserDataDto> getUserDataBuAuthId(String authId);
}
