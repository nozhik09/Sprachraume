package com.example.Sprachraume.Languages.repository;

import com.example.Sprachraume.Languages.entity.NativeLanguages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NativeLanguagesRepository extends JpaRepository<NativeLanguages,Long> {

    List<NativeLanguages> findByLanguage_Name(String nativeLanguage);
}
