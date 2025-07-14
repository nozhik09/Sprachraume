package com.example.Sprachraume.Rooms.entity.DTO;


import com.example.Sprachraume.Languages.entity.DTO.Response.LanguageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorRoomResponseDto {
    private String nickname;
    private String name;
    private String email;
    private String surname;
    private LocalDate birthdayDate;
    private String avatar;
    private Double rating;
    private String internalCurrency;
    private Boolean status;
    private Set<LanguageResponseDTO> nativeLanguage;
    private Set<LanguageResponseDTO> learningLanguages;
    private String role;
}
