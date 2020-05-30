package com.xcodeassociated.service.exception.handlers;

import com.xcodeassociated.service.exception.BaseServiceException;
import com.xcodeassociated.service.exception.reponse.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ClientOrderServiceExceptionHandler {

    @ExceptionHandler(BaseServiceException.class)
    ResponseEntity<ErrorResponse> handleBaseServiceException(BaseServiceException e) {
        return new ResponseEntity<>(e.toErrorResponse(), e.getStatus());
    }

}
