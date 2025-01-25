package com.example.Sprachraume.Languages.service;
import com.example.Sprachraume.Exceptions.Exception.EmailIsUsingException;
import com.example.Sprachraume.Exceptions.Exception.LanguageIsAdded;
import com.example.Sprachraume.Exceptions.Exception.UserNotFoundException;
import com.example.Sprachraume.Languages.entity.DTO.AddLanguageDTO;
import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.repository.LanguagesRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguagesService {
    private final UserRepository userRepository;
    private final LanguagesRepository languagesRepository;

    public Languages addNativeLanguages(AddLanguageDTO addLanguageDTO) {
        if (addLanguageDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        UserData userData = userRepository.findById(addLanguageDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", addLanguageDTO.getUserId())));

        Languages languages = languagesRepository.findByName(addLanguageDTO.getLanguageName()).orElseGet(() -> {
            if (addLanguageDTO.getLanguageName() == null || addLanguageDTO.getLanguageName().isEmpty()) {
                throw new IllegalArgumentException("Language name must not be null or empty for a new language");
            }
            Languages newLanguage = new Languages();
            newLanguage.setName(addLanguageDTO.getLanguageName());
            return languagesRepository.save(newLanguage);
        });

        if (userData.getNativeLanguages().contains(languages)) {
            throw new LanguageIsAdded(
                    String.format("Language with ID %s is already added for user %s",
                            addLanguageDTO.getLanguageName(), addLanguageDTO.getUserId()));
        }
        userData.getNativeLanguages().add(languages);
        userRepository.save(userData);

        return languages;
    }


    public List<Languages> getAllLanguage(){
        return languagesRepository.findAll();
    }





}
