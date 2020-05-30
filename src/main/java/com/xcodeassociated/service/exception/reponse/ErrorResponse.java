package com.xcodeassociated.service.exception.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer status;
    private String errorCode;
    private String message;
    private long timestamp = Instant.now().toEpochMilli();
    private Object data;
}
