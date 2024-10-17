package com.example.Sprachraume.UserData.controller;


import com.example.Sprachraume.UserData.Exceptions.ApiExceptionHanding;
import com.example.Sprachraume.UserData.entity.DTO.UserRequestDto;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "Регистрация пользователя", description = "Доступно всем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "User is registered",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Empty email or null", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "422", description = "Email validation failed", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "409", description = "Email is already in use", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "422", description = "Password validation failed", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class)))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserData createUser(@RequestBody UserRequestDto requestDto) {
        return userService.registerNewUser(requestDto);
    }



}
