package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class PasswordIsNotValid extends GenerateApiException {

    public PasswordIsNotValid(String message) {
        super(message);
    }
}
