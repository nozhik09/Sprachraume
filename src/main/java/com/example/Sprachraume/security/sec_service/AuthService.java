package com.example.Sprachraume.security.sec_service;


import com.example.Sprachraume.Exceptions.Exception.EmailIsNotValidException;
import com.example.Sprachraume.Exceptions.Exception.InvalidPasswordException;
import com.example.Sprachraume.Exceptions.Exception.PasswordIsNotValidException;
import com.example.Sprachraume.Exceptions.Exception.UserIsBlockingException;
import com.example.Sprachraume.Languages.repository.LearningLanguageRepository;
import com.example.Sprachraume.Mapping.Mapper;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.service.UserService;
import com.example.Sprachraume.security.DTO.LoginRequestDTO;
import com.example.Sprachraume.security.DTO.LoginResponseDTO;
import com.example.Sprachraume.security.DTO.TokenResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final LearningLanguageRepository learningLanguageRepository;


    public LoginResponseDTO login(LoginRequestDTO requestDTO) throws AuthException {

        String email = requestDTO.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Имеил не может быть пустым");
        }
        if (!isValidEmail(email)) {
            throw new EmailIsNotValidException(String.format("Вам имеил %s не корректно введен", email));
        }
        UserData foundUser = (UserData) userService.loadUserByUsername(email);
        if (!foundUser.getStatus()) {
            throw new UserIsBlockingException("Пользователь заблоктирован");
        } else if (!isValidPassword(requestDTO.getPassword())) {
            throw new PasswordIsNotValidException("Пароль должен содержать 8 симоволов,1 спец знак , заглавную букву и 1 цифру");
        } else if (bCryptPasswordEncoder.matches(requestDTO.getPassword(), foundUser.getPassword())) {

            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(email, refreshToken);


            return Mapper.mapToLoginResponseDTO(foundUser,accessToken,refreshToken);
        }else{
            throw new InvalidPasswordException("Вы ввели неверный пароль");
        }
    }


    public TokenResponseDto getNewAccessToken(String inboundRefreshToken) {

        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);

        String email = refreshClaims.getSubject();
        String savedRefreshToken = refreshStorage.get(email);

        if (savedRefreshToken != null && savedRefreshToken.equals(inboundRefreshToken)) {

            UserData user = (UserData) userService.loadUserByUsername(email);
            String accessToken = tokenService.generateAccessToken(user);
            return new TokenResponseDto(accessToken, null);
        } else {
            return new TokenResponseDto(null, null);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!_*])(?!.*\\s).{8,32}$";
        return password.matches(passwordRegex);
    }
}

















