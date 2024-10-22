package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class InvalidTokenException extends GenerateApiException {

    public InvalidTokenException(String message){
        super(message);
    }

}
