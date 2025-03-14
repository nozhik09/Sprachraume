package com.example.Sprachraume.UserData.controller;


import com.example.Sprachraume.Exceptions.ApiExceptionHanding;
import com.example.Sprachraume.UserData.entity.DTO.UserRequestDto;
import com.example.Sprachraume.UserData.entity.DTO.UserUpdateRequestDto;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "User registration", description = "Available to everyone")
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


    @Operation(summary = "Get All Users", description = "Available to Admin")
    @GetMapping
    public List<UserData> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get User by Id", description = "Available to ??")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User not Found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class)))})
    @GetMapping("/{id}")
    public UserData getUserById(@PathVariable(name = "id") Long id) {
        return userService.gitUserById(id);
    }


    @Operation(summary = "Update user data", description = "User/Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User update date", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User Not Found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update")
    public UserData updateUser(@RequestBody UserUpdateRequestDto requestDto) {
        return userService.updateUser(requestDto);
    }

    @PutMapping("/block")
    public UserData blockUser(@RequestParam Long admin,@RequestParam Long user){
        return userService.blockUser(admin,user);

    }
    @PutMapping("/unlock")
    public UserData unlockUser(@RequestParam Long admin,@RequestParam Long user){
        return userService.unlockUser(admin,user);
    }
    @PutMapping("/setAdmin")
    public UserData appointAnAdministrator(@RequestParam Long admin,@RequestParam Long user){
        return userService.appointAnAdministrator(admin,user);
    }

    @DeleteMapping("/delete")
    public String deleteAccount(@RequestParam Long id){
        return userService.deleteAccount(id);
    }

}
