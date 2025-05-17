package com.example.dairyinventoryservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
public class RecordNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;
    private int statusCode;

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
