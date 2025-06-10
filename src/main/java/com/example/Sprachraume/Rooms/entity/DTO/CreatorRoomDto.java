package com.example.Sprachraume.Rooms.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorRoomDto {
    private String nickname;
    private String name;
    private String email;
    private String surname;
    private LocalDate birthdayDate;
    private String avatar;
    private Double rating;
    private String internalCurrency;
    private Boolean status;
    private String nativeLanguage;
    private String learningLanguages;
    private String role;
}
