package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.db.dto.BaseEntityDto;
import com.xcodeassociated.service.model.domain.UserData;
import com.xcodeassociated.service.model.domain.dto.UserDataDto;
import com.xcodeassociated.service.repository.domain.EventCategoryRepository;
import com.xcodeassociated.service.repository.domain.UserDataRepository;
import com.xcodeassociated.service.repository.domain.provider.DomainObjectUtils;
import com.xcodeassociated.service.service.command.UserDataServiceCommand;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import com.xcodeassociated.service.service.query.UserDataServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserDataService implements UserDataServiceQuery, UserDataServiceCommand {
    private final UserDataRepository userDataRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceQuery oauthAuditorServiceQuery;

    @Override
    public UserDataDto getUserDataByAuthId(String authId) {
        log.info("Getting user data by auth id: {}", authId);

        return this.userDataRepository.findUserDataByUserAuthID(authId)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""))
                .toDto();
    }

    @Override
    public UserDataDto saveUserData(UserDataDto dto) {
        log.info("Save user data: {}", dto);

       return Stream.of(this.userDataRepository.findUserDataByUserAuthID(dto.getUserAuthID())
               .map(e -> this.updateUserDataFromDto(e, dto))
               .orElse(this.createUserDataFromDto(dto)))
               .map(this.userDataRepository::save)
               .findFirst()
               .get()
               .toDto();
    }

    @Override
    public void deleteUserData(String authId) {
        log.info("Delete user data by auth id: {}", authId);

        this.userDataRepository.deleteByUserAuthID(authId);
    }

    Optional<UserData> getUserDataOptionalByAuthId(String authId) {
        log.info("Getting user data by auth id: {}", authId);

        return this.userDataRepository.findUserDataByUserAuthID(authId);
    }

    private UserData createUserDataFromDto(UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();

        return UserData.builder()
                .modifiedBy(modificationAuthor)
                .userAuthID(Optional.ofNullable(dto.getUserAuthID()).orElseThrow())
                .maxDistance(dto.getMaxDistance())
                .preferredCategories(this.eventCategoryRepository.findEventCategoryByIdIn(dto.getPreferredCategories().stream()
                        .map(BaseEntityDto::getId)
                        .collect(Collectors.toList())))
                .build();
    }

    private UserData updateUserDataFromDto(UserData userData, UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceQuery.getModificationAuthor();
        var builder = DomainObjectUtils.copyBaseDomainObject(UserData.builder(), userData);

        return builder
                .modifiedBy(modificationAuthor)
                .userAuthID(Optional.ofNullable(dto.getUserAuthID()).orElseThrow())
                .maxDistance(dto.getMaxDistance())
                .preferredCategories(this.eventCategoryRepository.findEventCategoryByIdIn(dto.getPreferredCategories().stream()
                        .map(BaseEntityDto::getId)
                        .collect(Collectors.toList())))
                .build();
    }
}
