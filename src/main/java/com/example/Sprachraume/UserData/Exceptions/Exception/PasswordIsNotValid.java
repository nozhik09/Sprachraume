package com.example.Sprachraume.UserData.Exceptions.Exception;

import com.example.Sprachraume.UserData.Exceptions.GenerateApiException;

public class PasswordIsNotValid extends GenerateApiException {

    public PasswordIsNotValid(String message) {
        super(message);
    }
}
