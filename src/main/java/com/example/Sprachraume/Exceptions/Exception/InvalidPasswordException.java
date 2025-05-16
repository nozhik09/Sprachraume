package com.example.Sprachraume.Exceptions.Exception;


import com.example.Sprachraume.Exceptions.GenerateApiException;

public class InvalidPasswordException extends GenerateApiException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
