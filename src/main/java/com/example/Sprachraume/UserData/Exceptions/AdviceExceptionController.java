package com.example.Sprachraume.UserData.Exceptions;


import com.example.Sprachraume.UserData.Exceptions.Exception.EmailIsNotValid;
import com.example.Sprachraume.UserData.Exceptions.Exception.EmailIsUsingException;
import com.example.Sprachraume.UserData.Exceptions.Exception.InvalidRoleException;
import com.example.Sprachraume.UserData.Exceptions.Exception.PasswordIsNotValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class AdviceExceptionController {


    @ExceptionHandler(EmailIsNotValid.class)
    public ResponseEntity<ApiExceptionHanding> emailIsNotValid(EmailIsNotValid e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmailIsUsingException.class)
    public ResponseEntity<ApiExceptionHanding> emailIsUsing(EmailIsUsingException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiExceptionHanding> invalidRole(InvalidRoleException e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PasswordIsNotValid.class)
    public ResponseEntity<ApiExceptionHanding> passwordIsNotValid(PasswordIsNotValid e) {
        return new ResponseEntity<>(new ApiExceptionHanding(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }




}
