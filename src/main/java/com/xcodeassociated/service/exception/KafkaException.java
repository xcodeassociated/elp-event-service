package com.xcodeassociated.service.exception;

import com.xcodeassociated.service.exception.codes.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class KafkaException extends BaseServiceException {

    public KafkaException(String message) {
        super(Collections.singletonList(message));
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.K000;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public Object getData() {
        return null;
    }
}
