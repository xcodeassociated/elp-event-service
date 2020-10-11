package com.xcodeassociated.service.repository.domain.provider;

import com.xcodeassociated.service.model.db.UserDataDocument;
import com.xcodeassociated.service.model.domain.UserData;
import com.xcodeassociated.service.repository.db.UserDataDocumentRepository;
import com.xcodeassociated.service.repository.domain.EventCategoryRepository;
import com.xcodeassociated.service.repository.domain.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDataProvider implements UserDataRepository {
    private final UserDataDocumentRepository documentRepository;
    private final EventCategoryRepository categoryRepository;

    @Override
    public Optional<UserData> findUserDataByUserAuthID(String authId) {
        return this.documentRepository.findUserDataByUserAuthID(authId).map(this::toDomain);
    }

    @Override
    public UserData save(@NotNull UserData userDataDocument) {
        return this.toDomain(this.documentRepository.save(this.toDocument(userDataDocument)));
    }

    @Override
    public void deleteByUserAuthID(String authId) {
        this.documentRepository.deleteByUserAuthID(authId);
    }

    private UserData toDomain(UserDataDocument document) {
        var builder= DomainObjectUtils.toBaseDomainObject(UserData.builder(), document);

        return builder
                .userAuthID(document.getUserAuthID())
                .maxDistance(document.getMaxDistance())
                .preferredCategories(this.categoryRepository.findEventCategoryByIdIn(new ArrayList<>(document.getUserPreferredCategories())))
                .build();
    }

    private UserDataDocument toDocument(UserData userData) {
        var builder= DomainObjectUtils.toBaseDocument(UserDataDocument.builder(), userData);

        return builder
                .userAuthID(userData.getUserAuthID())
                .maxDistance(userData.getMaxDistance())
                .userPreferredCategories(userData.getPreferredCategories().stream()
                        .filter(e-> e.getId().isPresent())
                        .map(e -> e.getId().orElseThrow())
                        .collect(Collectors.toSet()))
                .build();
    }
}
