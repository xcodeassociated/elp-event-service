package com.xcodeassociated.service.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsParams {
    String userAuthId;
}
