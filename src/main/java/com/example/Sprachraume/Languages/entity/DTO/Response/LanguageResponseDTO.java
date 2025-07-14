package com.example.Sprachraume.Languages.entity.DTO.Response;
import com.example.Sprachraume.Languages.entity.Languages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponseDTO {
    private Long id;
    private Long userId;
    private Long languageId;
    private String skillLvl;
}
