package com.example.dairyinventoryservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Getter
@Setter
public class UnathorizedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private int statusCode;

    public UnathorizedException(String message) {
        super(message);
    }

    public UnathorizedException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
