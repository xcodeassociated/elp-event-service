package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.dto.UserDataDto;
import com.xcodeassociated.service.service.OauthAuditorServiceInterface;
import com.xcodeassociated.service.service.UserDataServiceInterface;
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
    private final UserDataServiceInterface userDataService;
    private final OauthAuditorServiceInterface oauthAuditorService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> getUserDataByAuthId(@PathVariable String id) {
        log.info("Getting user data for auth id: {}", id);
        return this.userDataService.getUserDataBuAuthId(id);
    }

    @GetMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> getUserDataByToken() {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Getting user data for auth id: {}", authId);
        return this.userDataService.getUserDataBuAuthId(authId);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> saveUserDataByAuthId(@PathVariable String id, @RequestBody UserDataDto dto) {
        dto.setUserAuthID(id);

        log.info("Saving user data: {} for auth id: {}", dto, id);
        return this.userDataService.saveUserData(dto);
    }

    @PostMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<UserDataDto> saveUserDataByToken(@RequestBody UserDataDto dto) {
        String authId = this.oauthAuditorService.getUserSub();
        dto.setUserAuthID(authId);

        log.info("Saving user data: {} for auth id: {}", dto, authId);
        return this.userDataService.saveUserData(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteUserDataByAuthId(@PathVariable String id) {
        log.info("Deleting user data for auth id: {}", id);
        return this.userDataService.deleteUserData(id);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('backend_service')")
    public Mono<Void> deleteUserDataByToken(@RequestBody UserDataDto dto) {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Deleting user data for auth id: {}", authId);
        return this.userDataService.deleteUserData(authId);
    }

}
