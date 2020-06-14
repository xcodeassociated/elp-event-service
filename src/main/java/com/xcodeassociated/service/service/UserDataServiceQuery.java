package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.dto.UserDataWithCategoryDto;
import reactor.core.publisher.Mono;

public interface UserDataServiceQuery {
    Mono<UserDataDto> getUserDataByAuthId(String authId);
    Mono<UserDataWithCategoryDto> getUserDataWithCategoryByAuthId(String authId);
}
