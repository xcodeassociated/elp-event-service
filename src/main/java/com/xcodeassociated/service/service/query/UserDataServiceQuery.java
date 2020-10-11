package com.xcodeassociated.service.service.query;

import com.xcodeassociated.service.model.domain.dto.UserDataDto;

public interface UserDataServiceQuery {
    UserDataDto getUserDataByAuthId(String authId);
}
