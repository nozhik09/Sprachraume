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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
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
    public UserData blockUser(@RequestParam Long admin, @RequestParam Long user) {
        return userService.blockUser(admin, user);

    }

    @PutMapping("/unlock")
    public UserData unlockUser(@RequestParam Long admin, @RequestParam Long user) {
        return userService.unlockUser(admin, user);
    }

    @PutMapping("/setAdmin")
    public UserData appointAnAdministrator(@RequestParam Long admin, @RequestParam Long user) {
        return userService.appointAnAdministrator(admin, user);
    }

    @DeleteMapping("/delete")
    public String deleteAccount(@RequestParam Long id) {
        return userService.deleteAccount(id);
    }


    @GetMapping("/blockingUsers")
    public List<UserData> getAllBlockUsers(@RequestParam Boolean status) {
        return userService.getAllBlockUsers(status);
    }

    @GetMapping("/ratingBetween")
    public List<UserData> getUserByRatingBetween(@RequestParam Double rating) {
        return userService.getUsersByRatingBetween(rating);
    }

    @GetMapping("/rating")
    public List<UserData> getUserByRating(@RequestParam Double rating) {
        return userService.getUsersByRating(rating);
    }

    @GetMapping("/findAnyUsers")
    public List<UserData> searchUsers(@RequestParam String keyword) {
        return userService.searchUsers(keyword);
    }

    @Operation(
            summary = "Upload user avatar",
            description = "Uploads an image file and sets it as the user's avatar"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    @PostMapping("/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestParam Long userId,
            @RequestParam MultipartFile file) throws IOException {

        String avatarUrl = userService.uploadAvatar(userId, file);
        return ResponseEntity.ok("Uploaded successfully. URL: " + avatarUrl);
    }


    @Operation(
            summary = "Get user avatar by userId",
            description = "Returns the user's avatar image based on the avatar path stored in DB"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar image"),
            @ApiResponse(responseCode = "404", description = "User or file not found")
    })
    @GetMapping("/avatar/{userId}")
    public ResponseEntity<Resource> getAvatar(@PathVariable Long userId) throws IOException {
        return userService.getAvatarByUserId(userId);
    }

    @GetMapping("/file/avatar/{fileName:.+}")
    public ResponseEntity<Resource> getAvatarByName(@PathVariable String fileName) throws IOException {
        Resource resource = userService.loadAvatarResource(fileName);

        String contentType = Files.probeContentType(resource.getFile().toPath());
        MediaType mediaType = contentType != null
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

}
