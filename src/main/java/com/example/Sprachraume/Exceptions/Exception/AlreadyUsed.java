package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class AlreadyUsed extends GenerateApiException {
    public AlreadyUsed(String message) {
        super(message);
    }
}
