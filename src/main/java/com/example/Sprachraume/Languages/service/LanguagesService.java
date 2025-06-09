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
            throw new NullOrEmptyException("User ID must not be null");
        }
        UserData userData = userRepository.findById(addLanguageDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with ID %s not found", addLanguageDTO.getUserId())));

        Languages languages = languagesRepository.findByName(addLanguageDTO.getLanguageName()).orElseGet(() -> {
            if (addLanguageDTO.getLanguageName() == null || addLanguageDTO.getLanguageName().isEmpty()) {
                throw new NullOrEmptyException("Language name must not be null or empty for a new language");
            }
            throw new LanguageNotFoundException("Such a language does not exist");
        });
        if (userData.getNativeLanguages().stream()
                .anyMatch(ll -> ll.getLanguage().getId().equals(languages.getId()))) {
            throw new LanguageIsAddedException(
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
                .orElseThrow(() -> new LanguageNotFoundException("Such a language does not exist"));

        if (userData.getLearningLanguages().stream()
                .anyMatch(ll -> ll.getLanguage().getId().equals(languages.getId()))) {
            throw new LanguageIsAddedException(
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




    public UserData deleteLearningLanguage(Long userId, Long languagesId){
        if (userId==null || languagesId==null){
            throw new IllegalArgumentException("User ID or Languages ID must not be null");
        }

        UserData userData = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
        Languages languages = languagesRepository.findById(languagesId).orElseThrow(()->new LanguageNotFoundException("Language not found"));
        LearningLanguage learningLanguage = learningLanguageRepository.findByUserAndLanguage(userData,languages).orElseThrow(()
        -> new AlreadyUsedException("User is not learning this language"));


        userData.getLearningLanguages().remove(learningLanguage);
        learningLanguageRepository.delete(learningLanguage);
        userRepository.save(userData);
        return userData;

    }


    public UserData deleteNativeLanguage(Long userId, Long languagesId){
        if (userId==null || languagesId==null){
            throw new IllegalArgumentException("User ID or Languages ID must not be null");
        }

        UserData userData = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));
        Languages languages = languagesRepository.findById(languagesId).orElseThrow(()->new LanguageNotFoundException("Language not found"));
        NativeLanguages nativeLanguages = nativeLanguagesRepository.findByUserAndLanguage(userData,languages).orElseThrow(()
                -> new AlreadyUsedException("User is not learning this language"));


        userData.getNativeLanguages().remove(nativeLanguages);
        nativeLanguagesRepository.delete(nativeLanguages);
        userRepository.save(userData);
        return userData;
    }


}


















