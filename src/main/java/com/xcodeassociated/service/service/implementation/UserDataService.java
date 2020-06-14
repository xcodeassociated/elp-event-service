package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.model.UserData;
import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.repository.UserDataRepository;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.UserDataServiceInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserDataService implements UserDataServiceInterface {
    private final UserDataRepository userDataRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;

    @Override
    public Mono<UserDataDto> getUserDataBuAuthId(String authId) {
        log.info("Getting user data by auth id: {}", authId);
        return this.userDataRepository.getUserDataByUserAuthID(authId)
                .map(UserData::toDto);
    }

    @Override
    public Mono<UserDataDto> saveUserData(UserDataDto dto) {
        log.info("Save user data: {}", dto);
       return this.userDataRepository.getUserDataByUserAuthID(dto.getUserAuthID())
               .switchIfEmpty(this.createUserDataFromDto(dto))
               .map(e -> this.updateUserDataFromDto(e, dto))
               .map(this.userDataRepository::save)
               .flatMap(e -> e.map(UserData::toDto));
    }

    @Override
    public Mono<Void> deleteUserData(String authId) {
        log.info("Delete user data by auth id: {}", authId);
        return this.userDataRepository.deleteByUserAuthID(authId);
    }

    private Mono<UserData> createUserDataFromDto(UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        final UserData userData = UserData.fromDto(dto);
        userData.setModifiedBy(modificationAuthor);
        return Mono.just(userData);
    }

    private UserData updateUserDataFromDto(UserData userData, UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        userData.setModifiedBy(modificationAuthor);
        return userData.update(dto);
    }
}
