package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.model.domain.dto.UserDataDto;
import com.xcodeassociated.service.service.command.UserDataServiceCommand;
import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import com.xcodeassociated.service.service.query.UserDataServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/userdata")
public class UserDataControllerV1 {
    private final UserDataServiceQuery userDataServiceQuery;
    private final UserDataServiceCommand userDataServiceCommand;
    private final OauthAuditorServiceQuery oauthAuditorService;

    @GetMapping()
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDataDto> getUserDataByToken() {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Getting user data for auth id: {}", authId);
        return new ResponseEntity<>(this.userDataServiceQuery.getUserDataByAuthId(authId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDataDto> getUserDataByAuthId(@PathVariable String id) {
        log.info("Getting user data for auth id: {}", id);
        return new ResponseEntity<>(this.userDataServiceQuery.getUserDataByAuthId(id), HttpStatus.OK);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDataDto> saveUserDataByAuthId(@PathVariable String id, @RequestBody UserDataDto dto) {
        dto.setUserAuthID(id);

        log.info("Saving user data: {} for auth id: {}", dto, id);
        return new ResponseEntity<>(this.userDataServiceCommand.saveUserData(dto), HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<UserDataDto> saveUserDataByToken(@RequestBody UserDataDto dto) {
        String authId = this.oauthAuditorService.getUserSub();
        dto.setUserAuthID(authId);

        log.info("Saving user data: {} for auth id: {}", dto, authId);
        return new ResponseEntity<>(this.userDataServiceCommand.saveUserData(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserDataByAuthId(@PathVariable String id) {
        log.info("Deleting user data for auth id: {}", id);
        this.userDataServiceCommand.deleteUserData(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Void> deleteUserDataByToken() {
        String authId = this.oauthAuditorService.getUserSub();
        log.info("Deleting user data for auth id: {}", authId);
        this.userDataServiceCommand.deleteUserData(authId);
        return ResponseEntity.ok().build();
    }

}
