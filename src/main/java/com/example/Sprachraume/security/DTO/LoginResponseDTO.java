package com.example.Sprachraume.security.DTO;


import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    private Set<NativeLanguages> nativeLanguages;

    private Set<LearningLanguage> learningLanguages;

    private Long rating;

    private String internalCurrency;

    private Boolean status;

    private String accessToken;

    private String refreshToken;

    private String message;
}
