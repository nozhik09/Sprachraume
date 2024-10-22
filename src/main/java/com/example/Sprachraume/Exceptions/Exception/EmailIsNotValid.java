package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class EmailIsNotValid extends GenerateApiException {
    public EmailIsNotValid(String message) {
        super(message);
    }


}
