package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NativeLanguagesRepository extends JpaRepository<NativeLanguages,Long> {

    List<NativeLanguages> findByLanguage_Name(String nativeLanguage);
    Optional<NativeLanguages> findByUserAndLanguage(UserData userData, Languages languages);
   List<NativeLanguages> findAllByUser(UserData userData);

}
