package com.xcodeassociated.service.exception;

import com.xcodeassociated.service.exception.codes.ErrorCode;
import com.xcodeassociated.service.exception.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseServiceException extends RuntimeException {

    private List<Object> messageArgs;

    public BaseServiceException() {
        this.messageArgs = new ArrayList<>();
    }

    public BaseServiceException(List<Object> messageArgs) {
        this.messageArgs = messageArgs;
    }

    @Override
    public String getMessage() {
        String msg = getErrorCode().getMessage();
        return messageArgs.isEmpty() ? msg : String.format(msg, messageArgs.toArray());
    }

    public abstract ErrorCode getErrorCode();

    public abstract Object getData();

    public abstract HttpStatus getStatus();

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse().toBuilder()
            .status(getStatus().value())
            .errorCode(getErrorCode().name())
            .message(getMessage())
            .data(getData())
            .build();
    }
}
