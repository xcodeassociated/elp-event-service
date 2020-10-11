package com.xcodeassociated.service.service.query;

import java.util.Set;

public interface OauthAuditorServiceQuery {
    String getModificationAuthor();
    String getUsername();
    String getUserSub();
    Set<String> getUserRoles();
}
