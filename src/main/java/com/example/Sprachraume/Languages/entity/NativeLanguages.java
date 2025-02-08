package com.example.Sprachraume.Languages.entity;


import com.example.Sprachraume.UserData.entity.UserData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_native_languages")
public class NativeLanguages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UserData userId;

    @Column(name = "language_id", nullable = false)
    private Languages languageId;

    @Column(name = "skill_level", nullable = false)
    private String skillLevel;
}
