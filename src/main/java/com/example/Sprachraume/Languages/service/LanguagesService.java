package com.example.Sprachraume.Languages.service;

import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Languages.entity.DTO.AddLanguageDTO;
import com.example.Sprachraume.Languages.entity.DTO.AddLearningLanguageDTO;
import com.example.Sprachraume.Languages.entity.DTO.FindUserByNativeAndLearningDTO;
import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Languages.repository.LanguagesRepository;
import com.example.Sprachraume.Languages.repository.LearningLanguageRepository;
import com.example.Sprachraume.Languages.repository.NativeLanguagesRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguagesService {
    private final UserRepository userRepository;
    private final LanguagesRepository languagesRepository;
    private final LearningLanguageRepository learningLanguageRepository;
private final NativeLanguagesRepository nativeLanguagesRepository;

    public NativeLanguages addNativeLanguages(AddLanguageDTO addLanguageDTO) {
        if (addLanguageDTO.getUserId() == null) {
            throw new NullOrEmpty("User ID must not be null");
        }
        UserData userData = userRepository.findById(addLanguageDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", addLanguageDTO.getUserId())));

        Languages languages = languagesRepository.findByName(addLanguageDTO.getLanguageName()).orElseGet(() -> {
            if (addLanguageDTO.getLanguageName() == null || addLanguageDTO.getLanguageName().isEmpty()) {
                throw new NullOrEmpty("Language name must not be null or empty for a new language");
            }
            throw new UserNotFoundException("Such a language does not exist");
        });
        if (userData.getNativeLanguages().stream()
                .anyMatch(ll -> ll.getLanguage().getId().equals(languages.getId()))) {
            throw new LanguageIsAdded(
                    String.format("Language %s is already added for user %s",
                            addLanguageDTO.getLanguageName(), addLanguageDTO.getUserId()));
        }
        NativeLanguages nativeLanguages = new NativeLanguages();
        nativeLanguages.setLanguage(languages);
        nativeLanguages.setUser(userData);
        nativeLanguages.setSkillLevel("Native");
        nativeLanguagesRepository.save(nativeLanguages);
        userData.getNativeLanguages().add(nativeLanguages);
        userRepository.save(userData);

        return nativeLanguages;
    }

    public LearningLanguage addLearningLanguage(AddLearningLanguageDTO addLearningLanguageDTO) {
        UserData userData = userRepository.findById(addLearningLanguageDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", addLearningLanguageDTO.getUserId())));

        Languages languages = languagesRepository.findByName(addLearningLanguageDTO.getLanguageName())
                .orElseThrow(() -> new UserNotFoundException("Such a language does not exist"));

        if (userData.getLearningLanguages().stream()
                .anyMatch(ll -> ll.getLanguage().getId().equals(languages.getId()))) {
            throw new LanguageIsAdded(
                    String.format("Language %s is already added for user %s",
                            addLearningLanguageDTO.getLanguageName(), addLearningLanguageDTO.getUserId()));
        }

        LearningLanguage learningLanguage = new LearningLanguage();
        learningLanguage.setUser(userData);
        learningLanguage.setLanguage(languages);
        learningLanguage.setSkillLevel(addLearningLanguageDTO.getSkillLevel());

        learningLanguageRepository.save(learningLanguage);
        userData.getLearningLanguages().add(learningLanguage);
        userRepository.save(userData);

        return learningLanguage;
    }




    // TODO сделать уровень владения языком ENUM или выпадающим списком

    public List<Languages> getAllLanguage() {
        return languagesRepository.findAll();
    }

    public List<UserData> getAllUserByNativeLanguages(String languageName) {
        return userRepository.findByNativeLanguages(languageName);
    }

    public List<UserData> getAllUserByLearningLanguage(String languageName) {
        Languages language = languagesRepository.findByName(languageName)
                .orElseThrow(() -> new UserNotFoundException("Язык не найден: " + languageName));

        List<LearningLanguage> learningLanguages = learningLanguageRepository.findByLanguage(language);

        return learningLanguages.stream()
                .map(LearningLanguage::getUser)
                .collect(Collectors.toList());
    }

    public List<UserData> getAllUserByLearningAndNativeLanguages(FindUserByNativeAndLearningDTO findUserByNativeAndLearningDTO) {
        Set<UserData> nativeUsers = nativeLanguagesRepository.findByLanguage_Name(findUserByNativeAndLearningDTO.getNativeLanguage())
                .stream().map(NativeLanguages::getUser).collect(Collectors.toSet());

        Set<UserData> learningUsers = learningLanguageRepository.findByLanguage_Name(findUserByNativeAndLearningDTO.getLearningLanguage())
                .stream().map(LearningLanguage::getUser).collect(Collectors.toSet());


        return nativeUsers.stream()
                .filter(learningUsers::contains)
                .collect(Collectors.toList());
    }


}


















