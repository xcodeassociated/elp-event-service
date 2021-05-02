package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.service.query.OauthAuditorServiceQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/event/api/v1/token")
public class TokenV1 {
    private final OauthAuditorServiceQuery oauthAuditorService;

    @GetMapping("/whoami")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<String> whoami() {
        log.info("Processing `whoami` request in TokenV1");
        return new ResponseEntity<>(this.oauthAuditorService.getUsername(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Set<String>> value() {
        log.info("Processing `value` request in TokenV1");
        return new ResponseEntity<>(this.oauthAuditorService.getUserRoles(), HttpStatus.OK);
    }

    @GetMapping("/sub")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<String> sub() {
        log.info("Processing `sub` request in TokenV1");
        return new ResponseEntity<>(this.oauthAuditorService.getUserSub(), HttpStatus.OK);
    }

    @GetMapping("/unauthorized")
    @PreAuthorize("hasRole('missing_role')")
    public ResponseEntity<Void> unauthorized() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
