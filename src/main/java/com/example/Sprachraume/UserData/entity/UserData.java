package com.example.Sprachraume.UserData.entity;


import com.example.Sprachraume.Role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "surname")
    private String surname;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday_date")
    private LocalDate birthdayDate;

    @Column(name = "native_language")
    private String nativeLanguage;

    @Column(name = "learning_language")
    private String learningLanguage;

    @Column(name = "skill_level")
    private String skillLevel;

    @Column(name = "foto")
    private String foto;

    @Column(name = "rating")
    private Long rating;

    @Column(name = "internal_currency")
    private Long internalCurrency;

    @Column(name = "status")
    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_role",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles;
}
