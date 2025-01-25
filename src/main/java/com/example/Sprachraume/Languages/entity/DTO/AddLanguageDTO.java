package com.example.Sprachraume.Languages.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLanguageDTO {
    private Long userId;
    private String languageName;
}
