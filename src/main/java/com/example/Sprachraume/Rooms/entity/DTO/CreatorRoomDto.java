package com.example.Sprachraume.Rooms.entity.DTO;


import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

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
    private Set<NativeLanguages> nativeLanguage;
    private Set<LearningLanguage> learningLanguages;
    private Set<Role> role;
}
