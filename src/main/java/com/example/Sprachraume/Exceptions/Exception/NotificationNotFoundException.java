package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class NotificationNotFoundException extends GenerateApiException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
