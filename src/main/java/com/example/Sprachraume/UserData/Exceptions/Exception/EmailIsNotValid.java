package com.example.Sprachraume.UserData.Exceptions.Exception;

import com.example.Sprachraume.UserData.Exceptions.GenerateApiException;

public class EmailIsNotValid extends GenerateApiException {
    public EmailIsNotValid(String message) {
        super(message);
    }


}
