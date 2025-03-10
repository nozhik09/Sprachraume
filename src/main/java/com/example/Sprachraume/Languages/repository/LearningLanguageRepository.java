package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningLanguageRepository extends JpaRepository<LearningLanguage, Long> {
    List<LearningLanguage> findByUser(UserData user);
    List<LearningLanguage> findByLanguage(Languages languages);
    List<LearningLanguage> findByLanguage_Name(String learningLanguage);
    Optional<LearningLanguage> findByUserAndLanguage(UserData userData,Languages languages);

}
