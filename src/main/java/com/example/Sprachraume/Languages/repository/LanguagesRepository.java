package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguagesRepository extends JpaRepository<Languages, Long> {
    Optional<Languages> findById(Long id);

    Languages findAllById(Long id);

    Optional<Languages> findByName(String name);
}
