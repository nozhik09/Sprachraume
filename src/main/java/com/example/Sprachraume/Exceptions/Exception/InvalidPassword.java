package com.example.Sprachraume.Exceptions.Exception;


import com.example.Sprachraume.Exceptions.GenerateApiException;

public class InvalidPassword extends GenerateApiException {

    public InvalidPassword(String message) {
        super(message);
    }
}
