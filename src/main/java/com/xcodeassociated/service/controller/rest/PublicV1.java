package com.xcodeassociated.service.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/event/api/v1/public")
public class PublicV1 {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/name")
    public ResponseEntity<String> serviceName() {
        log.info("Processing `name` request in PublicV1");
        return new ResponseEntity<>(this.applicationName, HttpStatus.OK);
    }
}
