package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.LearningLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningLanguageRepository extends JpaRepository<LearningLanguage,Long> {


    List<LearningLanguage> findByUserId(Long userId);
    LearningLanguage findByUserIdAndLanguageId(Long userId, Long languageId);


}
