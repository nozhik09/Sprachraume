package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserHaveLowRatingException extends GenerateApiException {
    public UserHaveLowRatingException(String message) {
        super(message);
    }
}
