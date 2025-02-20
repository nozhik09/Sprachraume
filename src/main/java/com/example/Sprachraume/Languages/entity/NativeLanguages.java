package com.example.Sprachraume.Languages.entity;


import com.example.Sprachraume.UserData.entity.UserData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_native_languages")
public class NativeLanguages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserData user;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Languages language;

    @Column(name = "skill_level", nullable = false)
    private String skillLevel;
}
