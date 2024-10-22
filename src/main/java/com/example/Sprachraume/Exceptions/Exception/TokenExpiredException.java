package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class TokenExpiredException extends GenerateApiException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
