package com.example.Sprachraume.UserData.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    private Long id;
    private String nickName;
    private String name;
    private String surname;
    private LocalDate birthdayDate;
    private String nativeLanguage;
    private String learningLanguage;
    private String skillLevel;
    private String foto;
    private String internalCurrency;
}
