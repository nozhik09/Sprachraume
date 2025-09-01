package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class ForbiddenException extends GenerateApiException {
    public ForbiddenException(String message) {
        super(message);
    }
}
