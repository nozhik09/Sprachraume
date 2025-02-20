package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class NullOrEmpty extends GenerateApiException {
    public NullOrEmpty(String message) {
        super(message);
    }
}
