package com.example.Sprachraume.Languages.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindUserByNativeAndLearningDTO {
    private String learningLanguage;
    private String nativeLanguage;
}
