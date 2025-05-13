package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserHaveLowRating extends GenerateApiException {
    public UserHaveLowRating(String message) {
        super(message);
    }
}
