package com.example.Sprachraume.Languages.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_learning_languages")
public class LearningLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "language_id", nullable = false)
    private Long languageId;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Languages language;

    @Column(name = "skill_level", nullable = false)
    private String skillLevel;
}
