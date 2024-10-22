package com.example.Sprachraume.security.DTO;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long id;

    private String nickname;

    private String name;

    private String email;

    private String surname;

    private LocalDate birthdayDate;

    private String nativeLanguage;

    private String learningLanguage;

    private String skillLevel;

    private Long rating;

    private String internalCurrency;

    private Boolean status;

    private String accessToken;

    private String refreshToken;

    private String message;
}
