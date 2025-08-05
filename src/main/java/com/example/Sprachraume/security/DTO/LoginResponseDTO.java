package com.example.Sprachraume.security.DTO;


import com.example.Sprachraume.Languages.entity.DTO.Response.LanguageResponseDTO;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Rooms.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private String avatar;
    private Double rating;
    private String internalCurrency;
    private Boolean status;
    private Set<LanguageResponseDTO> nativeLanguages ;
    private Set<LanguageResponseDTO> learningLanguages;
    private Set<Long> createdRooms;
    private Set<Role> roles;
    private String accessToken;
    private String refreshToken;
    private String message;

}
