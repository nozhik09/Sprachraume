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
    @ExceptionHandler(MaxQuantityException.class)
    public ResponseEntity<ApiExceptionHanding> maxQuantityException (MaxQuantityException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserHaveLowRatingException.class)
    public ResponseEntity<ApiExceptionHanding> userHaveLowRating (UserHaveLowRatingException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserTooYoungException.class)
    public ResponseEntity<ApiExceptionHanding> userTooYoungException(UserTooYoungException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(UserBirthdayNotSetException.class)
    public ResponseEntity<ApiExceptionHanding> userBirthdayNotSet(UserBirthdayNotSetException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyUsedException.class)
    public ResponseEntity<ApiExceptionHanding> alreadyUsed(AlreadyUsedException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<ApiExceptionHanding> languageNotFound(LanguageNotFoundException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullOrEmptyException.class)
    public ResponseEntity<ApiExceptionHanding> nullOrEmpty(NullOrEmptyException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(InvitationAlreadyRespondedException.class)
    public ResponseEntity<ApiExceptionHanding> invitationAlreadyResponded(InvitationAlreadyRespondedException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }



    @ExceptionHandler(EmailIsNotValidException.class)
    public ResponseEntity<ApiExceptionHanding> emailIsNotValid(EmailIsNotValidException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmailIsUsingException.class)
    public ResponseEntity<ApiExceptionHanding> emailIsUsing(EmailIsUsingException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LanguageIsAddedException.class)
    public ResponseEntity<ApiExceptionHanding> emailIsUsing(LanguageIsAddedException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiExceptionHanding> invalidRole(InvalidRoleException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PasswordIsNotValidException.class)
    public ResponseEntity<ApiExceptionHanding> PasswordIsNotValid(PasswordIsNotValidException e) {
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

    @ExceptionHandler(UserIsBlockingException.class)
    public ResponseEntity<ApiExceptionHanding> userForbidden(UserIsBlockingException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.FORBIDDEN.toString()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiExceptionHanding> InvalidPassword(InvalidPasswordException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiExceptionHanding> roomNotFound(RoomNotFoundException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }


}
