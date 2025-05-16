package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class InvitationAlreadyRespondedException extends GenerateApiException {

    public InvitationAlreadyRespondedException(String message) {
        super(message);
    }
}
