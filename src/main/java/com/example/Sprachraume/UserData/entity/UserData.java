    package com.example.Sprachraume.UserData.entity;


    import com.example.Sprachraume.Languages.entity.LearningLanguage;
    import com.example.Sprachraume.Languages.entity.NativeLanguages;
    import com.example.Sprachraume.Role.Role;
    import com.example.Sprachraume.Rooms.entity.Room;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.NoArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import java.time.LocalDate;
    import java.util.Collection;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Table(name = "users")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @EqualsAndHashCode(exclude = {"nativeLanguages", "learningLanguages"})
    public class UserData implements UserDetails {

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

        @Column(name = "avatar")
        private String avatar;


        @Column(name = "rating")
        private Double rating;

        @Column(name = "internal_currency")
        private String internalCurrency;

        @Column(name = "status")
        private Boolean status;

        @JsonManagedReference
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<NativeLanguages> nativeLanguages = new HashSet<>();

        @JsonManagedReference
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<LearningLanguage> learningLanguages = new HashSet<>();

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "users_role",
                joinColumns = @JoinColumn(name = "users_id"),
                inverseJoinColumns = @JoinColumn(name = "roles_id"))
        private Set<Role> roles;

        @JsonManagedReference("creator-rooms")
        @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<Room> createdRooms;


        @Override
        public boolean isAccountNonExpired() {
            return UserDetails.super.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return UserDetails.super.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return UserDetails.super.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return UserDetails.super.isEnabled();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }

        @Override
        public String getUsername() {
            return "";
        }
    }
