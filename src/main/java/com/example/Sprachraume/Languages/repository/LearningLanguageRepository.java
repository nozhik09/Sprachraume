package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningLanguageRepository extends JpaRepository<LearningLanguage, Long> {
    List<LearningLanguage> findAllByUser(UserData user);
    List<LearningLanguage> findByUser(UserData user);
    List<LearningLanguage> findByLanguage(Languages languages);
    @Query("SELECT ll FROM LearningLanguage ll WHERE LOWER(ll.language.name) = LOWER(:languageName)")
    List<LearningLanguage> findByLanguageNameIgnoreCase(@Param("languageName") String languageName);
    Optional<LearningLanguage> findByUserAndLanguage(UserData userData,Languages languages);

}
