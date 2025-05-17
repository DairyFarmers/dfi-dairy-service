package com.example.dairyinventoryservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private int statusCode;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
