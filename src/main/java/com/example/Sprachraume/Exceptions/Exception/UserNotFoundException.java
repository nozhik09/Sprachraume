package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class UserNotFoundException extends GenerateApiException {

    public UserNotFoundException (String message){
        super(message);
    }
}
