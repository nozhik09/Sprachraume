package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class LanguageNotFound extends GenerateApiException {
    public LanguageNotFound(String message) {
        super(message);
    }
}
