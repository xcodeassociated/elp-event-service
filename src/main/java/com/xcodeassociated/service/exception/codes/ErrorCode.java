package com.xcodeassociated.service.exception.codes;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // generic:
    E000("Unknown error"),
    E001("Element not found by criteria: %s"),
    E002("Error: %s"),
    // service:
    S000("Service exception: %s"),
    // kafka:
    K000("Kafka exception: %s"),
    // validation:
    V000("Validation exception: %s");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
