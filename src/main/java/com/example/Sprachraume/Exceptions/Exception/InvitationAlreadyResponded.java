package com.example.Sprachraume.Exceptions.Exception;

import com.example.Sprachraume.Exceptions.GenerateApiException;

public class InvitationAlreadyResponded extends GenerateApiException {

    public InvitationAlreadyResponded(String message) {
        super(message);
    }
}
