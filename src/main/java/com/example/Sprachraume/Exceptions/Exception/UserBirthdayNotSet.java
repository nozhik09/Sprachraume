package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserBirthdayNotSet extends GenerateApiException {
    public UserBirthdayNotSet(String message) {
        super(message);
    }
}
