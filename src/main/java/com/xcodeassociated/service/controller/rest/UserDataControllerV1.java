package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.model.dto.UserDataWithCategoryDto;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.UserDataServiceCommand;
import com.xcodeassociated.service.service.UserDataServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/user")
public class UserDataControllerV1 {
    private final UserDataServiceQuery userDataServiceQuery;
    private final UserDataServiceCommand userDataServiceCommand;
    private final OauthAuditorServiceInterface oauthAuditorService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> getUserDataByAuthId(@PathVariable String id) {
        log.info("Getting user data for auth id: {}", id);
        return this.userDataServiceQuery.getUserDataByAuthId(id);
    }

    @GetMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> getUserDataByToken() {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Getting user data for auth id: {}", authId);
        return this.userDataServiceQuery.getUserDataByAuthId(authId);
    }

    @GetMapping("/{id}/data")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataWithCategoryDto> getUserDataWithCategoryByAuthId(@PathVariable String id) {
        log.info("Getting user data with category for auth id: {}", id);
        return this.userDataServiceQuery.getUserDataWithCategoryByAuthId(id);
    }

    @GetMapping("/data")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataWithCategoryDto> getUserDataWithCategoryByAuthId() {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Getting user data with category for auth id: {}", authId);
        return this.userDataServiceQuery.getUserDataWithCategoryByAuthId(authId);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> saveUserDataByAuthId(@PathVariable String id, @RequestBody UserDataDto dto) {
        dto.setUserAuthID(id);

        log.info("Saving user data: {} for auth id: {}", dto, id);
        return this.userDataServiceCommand.saveUserData(dto);
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> saveUserDataByToken(@RequestBody UserDataDto dto) {
        String authId = this.oauthAuditorService.getUserSub();
        dto.setUserAuthID(authId);

        log.info("Saving user data: {} for auth id: {}", dto, authId);
        return this.userDataServiceCommand.saveUserData(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteUserDataByAuthId(@PathVariable String id) {
        log.info("Deleting user data for auth id: {}", id);
        return this.userDataServiceCommand.deleteUserData(id);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteUserDataByToken(@RequestBody UserDataDto dto) {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Deleting user data for auth id: {}", authId);
        return this.userDataServiceCommand.deleteUserData(authId);
    }

}
