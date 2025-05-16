package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserIsBlockingException extends GenerateApiException {
    public UserIsBlockingException(String message) {
        super(message);
    }
}
