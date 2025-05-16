package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class AlreadyUsedException extends GenerateApiException {
    public AlreadyUsedException(String message) {
        super(message);
    }
}
