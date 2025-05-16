package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class NullOrEmptyException extends GenerateApiException {
    public NullOrEmptyException(String message) {
        super(message);
    }
}
