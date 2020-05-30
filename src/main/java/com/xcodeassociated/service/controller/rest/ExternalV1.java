package com.xcodeassociated.service.controller.rest;

import com.xcodeassociated.service.controller.rest.external.ExternalServiceApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/event/api/v1/external")
public class ExternalV1 {

    private ExternalServiceApi api;

    @GetMapping("/whoami")
    @PreAuthorize("hasRole('backend_service')")
    public ResponseEntity<String> whoami() throws IOException {
        log.info("Processing `whoami` request in ExternalV1");
        String response = Objects.requireNonNull(this.api.getWhoami().execute().body()).string();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
