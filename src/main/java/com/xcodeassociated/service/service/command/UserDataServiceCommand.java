package com.xcodeassociated.service.service.command;

import com.xcodeassociated.service.model.domain.dto.UserDataDto;

public interface UserDataServiceCommand {
    UserDataDto saveUserData(UserDataDto dto);
    void deleteUserData(String authId);
}
