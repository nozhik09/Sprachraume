package com.example.Sprachraume.Exceptions;


import com.example.Sprachraume.Exceptions.Exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class AdviceExceptionController {

    @ExceptionHandler(AlreadyUsed.class)
    public ResponseEntity<ApiExceptionHanding> alreadyUsed(AlreadyUsed e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LanguageNotFound.class)
    public ResponseEntity<ApiExceptionHanding> languageNotFound(LanguageNotFound e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullOrEmpty.class)
    public ResponseEntity<ApiExceptionHanding> nullOrEmpty(NullOrEmpty e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(InvitationAlreadyResponded.class)
    public ResponseEntity<ApiExceptionHanding> invitationAlreadyResponded(InvitationAlreadyResponded e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }



    @ExceptionHandler(EmailIsNotValid.class)
    public ResponseEntity<ApiExceptionHanding> emailIsNotValid(EmailIsNotValid e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmailIsUsingException.class)
    public ResponseEntity<ApiExceptionHanding> emailIsUsing(EmailIsUsingException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LanguageIsAdded.class)
    public ResponseEntity<ApiExceptionHanding> emailIsUsing(LanguageIsAdded e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiExceptionHanding> invalidRole(InvalidRoleException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PasswordIsNotValid.class)
    public ResponseEntity<ApiExceptionHanding> PasswordIsNotValid(PasswordIsNotValid e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiExceptionHanding> UserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiExceptionHanding> TokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNAUTHORIZED.toString()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserIsBlocking.class)
    public ResponseEntity<ApiExceptionHanding> userForbidden(UserIsBlocking e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.FORBIDDEN.toString()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<ApiExceptionHanding> InvalidPassword(InvalidPassword e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiExceptionHanding> roomNotFound(RoomNotFoundException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }


}
