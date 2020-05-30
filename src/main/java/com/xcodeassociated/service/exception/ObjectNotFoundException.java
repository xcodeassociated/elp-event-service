package com.xcodeassociated.service.exception;

import com.xcodeassociated.service.exception.codes.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ObjectNotFoundException extends BaseServiceException {

    private final ErrorCode errorCode;

    public ObjectNotFoundException(ErrorCode errorCode, Object... messageFormatArgs) {
        super(Arrays.asList(messageFormatArgs));
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
