package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserTooYoungException extends GenerateApiException {
    public UserTooYoungException(String message) {
        super(message);
    }
}
