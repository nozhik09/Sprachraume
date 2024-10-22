package com.example.Sprachraume.Exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiExceptionHanding {
    private String message;
    private String errorCode ;
}
