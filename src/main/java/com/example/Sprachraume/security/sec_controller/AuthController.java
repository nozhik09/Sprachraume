package com.example.Sprachraume.security.sec_controller;

import com.example.Sprachraume.Exceptions.ApiExceptionHanding;
import com.example.Sprachraume.security.DTO.LoginRequestDTO;
import com.example.Sprachraume.security.DTO.LoginResponseDTO;
import com.example.Sprachraume.security.DTO.RefreshRequestDto;
import com.example.Sprachraume.security.DTO.TokenResponseDto;
import com.example.Sprachraume.security.sec_service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @Operation(summary = "Log in", description = "Available to all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization was successful"),
            @ApiResponse(responseCode = "422", description = "Email validation failed", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "404", description = "USER NOT FOUND", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "403", description = "User is blocked", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "400", description = "You entered the wrong password", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "400", description = "Empty email or null", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "422", description = "Password validation failed", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiExceptionHanding.class)))})


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDto) {
        try {
            return authService.login(loginRequestDto);
        } catch (AuthException e) {
            return null;
        }
    }


    @PostMapping("/refresh")
    public TokenResponseDto getAccessToken(@RequestBody RefreshRequestDto requestDto) {
        return authService.getNewAccessToken(requestDto.getRefreshToken());
    }


    @GetMapping("/login/google")
    public String login() {
        return "login";
    }

    @GetMapping("/register/google")
    public String register() {
        return "register";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }


}
