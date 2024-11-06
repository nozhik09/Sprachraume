package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class RoomNotFoundException extends GenerateApiException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
