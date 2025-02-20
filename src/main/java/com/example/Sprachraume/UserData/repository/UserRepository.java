package com.example.Sprachraume.UserData.repository;

import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData,Long> {

    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByNickname(String email);

    @Query("SELECT u FROM UserData u JOIN u.nativeLanguages nl JOIN nl.language l WHERE l.name = :languageName")
    List<UserData> findByNativeLanguages(@Param("languageName") String languageName);
}
