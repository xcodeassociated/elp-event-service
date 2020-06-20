package com.xcodeassociated.service.service;

import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.dto.UserDataWithCategoryDto;

public interface UserDataServiceQuery {
    UserDataDto getUserDataByAuthId(String authId);
    UserDataWithCategoryDto getUserDataWithCategoryByAuthId(String authId);
}
