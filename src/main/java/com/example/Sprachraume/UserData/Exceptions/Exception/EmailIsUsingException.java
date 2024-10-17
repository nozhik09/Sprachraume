package com.example.Sprachraume.UserData.Exceptions.Exception;

import com.example.Sprachraume.UserData.Exceptions.GenerateApiException;

public class EmailIsUsingException extends GenerateApiException {
    public EmailIsUsingException(String message) {
        super(message);
    }
}
