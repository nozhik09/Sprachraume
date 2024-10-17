package com.example.Sprachraume.UserData.repository;

import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData,Long> {

    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByNickname(String email);

}
