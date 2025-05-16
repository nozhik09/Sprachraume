package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class LanguageNotFoundException extends GenerateApiException {
    public LanguageNotFoundException(String message) {
        super(message);
    }
}
