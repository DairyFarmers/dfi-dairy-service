package com.example.dairyinventoryservice.exception.handler;

import com.example.dairyinventoryservice.dto.response.GeneralResponse;
import com.example.dairyinventoryservice.exception.BadRequestException;
import com.example.dairyinventoryservice.exception.RecordNotFoundException;
import com.example.dairyinventoryservice.exception.UnathorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException badRequestException, WebRequest webRequest) {
        log.error("badRequestExceptionHandler triggered badRequestException", badRequestException);
        log.error("badRequestExceptionHandler triggered badRequestException URL {} ", webRequest.getDescription(false));
        GeneralResponse errorResponse = new GeneralResponse(null,false, badRequestException.getMessage(),  badRequestException.getStatusCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(RecordNotFoundException recordNotFoundException, WebRequest webRequest) {
        log.error("notFoundExceptionHandler triggered notFoundException", recordNotFoundException);
        log.error("notFoundExceptionHandler triggered notFoundException URL {} ", webRequest.getDescription(false));
        GeneralResponse errorResponse = new GeneralResponse(null, false, recordNotFoundException.getMessage(),  recordNotFoundException.getStatusCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnathorizedException.class)
    public ResponseEntity<?> unauthorizedExceptionHandler(UnathorizedException unathorizedException, WebRequest webRequest) {
        log.error("unauthorizedExceptionHandler triggered unauthorizedException", unathorizedException);
        log.error("unauthorizedExceptionHandler triggered unauthorizedException URL {} ", webRequest.getDescription(false));
        GeneralResponse errorResponse = new GeneralResponse(null, false, unathorizedException.getMessage(), unathorizedException.getStatusCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
