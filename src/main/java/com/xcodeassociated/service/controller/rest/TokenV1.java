package com.xcodeassociated.service.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/event/api/v1/token")
public class TokenV1 {

    @GetMapping("/whoami")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<String> whoami() {
        log.info("Processing `whoami` request in TokenV1");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            userName = kp.getKeycloakSecurityContext().getToken().getPreferredUsername();
        }

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<Set<String>> value() {
        log.info("Processing `value` request in TokenV1");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = new HashSet<>();
        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            roles = kp.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
        }

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/sub")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<String> sub() {
        log.info("Processing `sub` request in TokenV1");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sub = "";
        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp =
                    (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            sub = kp.getKeycloakSecurityContext().getToken().getSubject();
        }

        return new ResponseEntity<>(sub, HttpStatus.OK);
    }

    @GetMapping("/unauthorized")
    @PreAuthorize("hasRole('missing_role')")
    public ResponseEntity<Void> unauthorized() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
