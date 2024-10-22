package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserIsBlocking extends GenerateApiException {
    public UserIsBlocking(String message) {
        super(message);
    }
}
