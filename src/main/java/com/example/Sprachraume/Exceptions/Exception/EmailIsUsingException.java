package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class EmailIsUsingException extends GenerateApiException {
    public EmailIsUsingException(String message) {
        super(message);
    }
}
