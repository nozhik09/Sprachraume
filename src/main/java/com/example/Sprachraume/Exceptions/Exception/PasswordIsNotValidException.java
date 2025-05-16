package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class PasswordIsNotValidException extends GenerateApiException {

    public PasswordIsNotValidException(String message) {
        super(message);
    }
}
