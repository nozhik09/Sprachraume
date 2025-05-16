package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserBirthdayNotSetException extends GenerateApiException {
    public UserBirthdayNotSetException(String message) {
        super(message);
    }
}
