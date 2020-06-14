package com.xcodeassociated.service.service.implementation;

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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserDataService implements UserDataServiceQuery, UserDataServiceCommand {
    private final UserDataRepository userDataRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final OauthAuditorServiceInterface oauthAuditorServiceInterface;

    @Override
    public Mono<UserDataDto> getUserDataByAuthId(String authId) {
        log.info("Getting user data by auth id: {}", authId);
        return this.userDataRepository.getUserDataByUserAuthID(authId)
                .map(UserData::toDto);
    }

    @Override
    public Mono<UserDataWithCategoryDto> getUserDataWithCategoryByAuthId(String authId) {
        log.info("Getting user data with category by auth id: {}", authId);
        return this.userDataRepository.getUserDataByUserAuthID(authId)
                .map(this::getUserDataWithCategoryDto);
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

    private UserDataWithCategoryDto getUserDataWithCategoryDto(UserData userData) {
        final List<EventCategory> eventCategories = this.eventCategoryRepository
                .getEventCategoriesByIdIn(userData.getUserPreferredCategories().stream()
                        .collect(Collectors.toList()))
                .collectList()
                .block();

        return new UserDataWithCategoryDto().toBuilder()
                .userAuthID(userData.getUserAuthID())
                .maxDistance(userData.getMaxDistance())
                .userPreferredCategories(eventCategories.stream()
                        .map(EventCategory::toDto)
                        .collect(Collectors.toSet()))
                .build();
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
