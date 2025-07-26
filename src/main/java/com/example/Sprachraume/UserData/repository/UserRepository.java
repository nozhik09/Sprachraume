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
    List<UserData> findAllByStatus(Boolean status);


    List<UserData> findAllByRatingBetween(Double rating,Double maxRating);
    List<UserData> findAllByRating(Double rating);
    @Query("SELECT u FROM UserData u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.surname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserData> searchUsers(@Param("keyword") String keyword);

}
