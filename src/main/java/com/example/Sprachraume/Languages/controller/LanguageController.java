package com.example.Sprachraume.Languages.controller;


import com.example.Sprachraume.Languages.entity.DTO.AddLanguageDTO;
import com.example.Sprachraume.Languages.entity.DTO.AddLearningLanguageDTO;
import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.service.LanguagesService;
import com.example.Sprachraume.UserData.entity.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController {
    private final LanguagesService languagesService;


    @PostMapping("/native")
    public Languages addNativeLanguage(@RequestBody AddLanguageDTO addLanguageDTO) {
        return languagesService.addNativeLanguages(addLanguageDTO);
    }

    @GetMapping
    public List<Languages> getAllNativeLanguage() {
        return languagesService.getAllLanguage();
    }


    @GetMapping("/nativeLanguage")
    public List<UserData> getUserByNativeLanguage(@RequestParam String languageName) {
        return languagesService.getAllUserByNativeLanguages(languageName);
    }

    @PostMapping("/addLearningLanguage")
    public LearningLanguage addLearningLanguage(@RequestBody AddLearningLanguageDTO addLearningLanguageDTO){
       return languagesService.addLearningLanguage(addLearningLanguageDTO);
    }


}
