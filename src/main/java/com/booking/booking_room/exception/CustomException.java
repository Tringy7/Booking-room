package com.booking.booking_room.exception;

import com.booking.booking_room.dto.exception.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomException {
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<RestResponse<Object>> handleCommonException(CommonException customExceptionCommon) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Exception occur...");
        res.setMessage(customExceptionCommon.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
