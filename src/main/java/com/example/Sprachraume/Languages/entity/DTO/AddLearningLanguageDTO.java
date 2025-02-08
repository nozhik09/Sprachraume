package com.example.Sprachraume.Languages.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLearningLanguageDTO {
    private Long userId;
    private String languageName;
    private String skillLevel;
}
