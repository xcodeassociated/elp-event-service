package com.xcodeassociated.service.service.implementation;

import com.xcodeassociated.service.exception.ServiceException;
import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.model.EventCategory;
import com.xcodeassociated.service.model.UserData;
import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.dto.UserDataWithCategoryDto;
import com.xcodeassociated.service.repository.EventCategoryRepository;
import com.xcodeassociated.service.repository.UserDataRepository;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.UserDataServiceCommand;
import com.xcodeassociated.service.service.UserDataServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserDataService implements UserDataServiceQuery, UserDataServiceCommand {
    private final UserDataRepository userDataRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;

    @Override
    public UserDataDto getUserDataByAuthId(String authId) {
        log.info("Getting user data by auth id: {}", authId);
        return this.userDataRepository.findUserDataByUserAuthID(authId)
                .map(UserData::toDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public UserDataWithCategoryDto getUserDataWithCategoryByAuthId(String authId) {
        log.info("Getting user data with category by auth id: {}", authId);
        return this.userDataRepository.findUserDataByUserAuthID(authId)
                .map(this::getUserDataWithCategoryDto)
                .orElseThrow(() -> new ServiceException(ErrorCode.S000, ""));
    }

    @Override
    public UserDataDto saveUserData(UserDataDto dto) {
        log.info("Save user data: {}", dto);
       return Stream.of(this.userDataRepository.findUserDataByUserAuthID(dto.getUserAuthID())
               .orElse(this.createUserDataFromDto(dto)))
               .map(e -> this.updateUserDataFromDto(e, dto))
               .map(this.userDataRepository::save)
               .map(UserData::toDto)
               .findFirst()
               .get();
    }

    @Override
    public void deleteUserData(String authId) {
        log.info("Delete user data by auth id: {}", authId);
        this.userDataRepository.deleteByUserAuthID(authId);
    }

    private UserDataWithCategoryDto getUserDataWithCategoryDto(UserData userData) {
        final List<EventCategory> eventCategories = this.eventCategoryRepository
                .findEventCategoryByIdIn(userData.getUserPreferredCategories()
                        .stream().collect(Collectors.toList()));

        return userData.toDto(eventCategories.stream().collect(Collectors.toSet()));
    }

    private UserData createUserDataFromDto(UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        final UserData userData = UserData.fromDto(dto);
        userData.setModifiedBy(modificationAuthor);
        return userData;
    }

    private UserData updateUserDataFromDto(UserData userData, UserDataDto dto) {
        final String modificationAuthor = this.oauthAuditorServiceInterface.getModificationAuthor();
        userData.setModifiedBy(modificationAuthor);
        return userData.update(dto);
    }
}
