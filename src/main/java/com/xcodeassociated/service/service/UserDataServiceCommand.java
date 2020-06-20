package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;

public interface UserDataServiceCommand {
    UserDataDto saveUserData(UserDataDto dto);
    void deleteUserData(String authId);
}
