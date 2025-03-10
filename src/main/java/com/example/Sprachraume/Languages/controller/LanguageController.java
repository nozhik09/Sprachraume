package com.example.Sprachraume.Languages.controller;

import com.example.Sprachraume.Exceptions.ApiExceptionHanding;
import com.example.Sprachraume.Languages.entity.DTO.AddLanguageDTO;
import com.example.Sprachraume.Languages.entity.DTO.AddLearningLanguageDTO;
import com.example.Sprachraume.Languages.entity.DTO.FindUserByNativeAndLearningDTO;
import com.example.Sprachraume.Languages.entity.Languages;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Languages.service.LanguagesService;
import com.example.Sprachraume.UserData.entity.UserData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController {
    private final LanguagesService languagesService;

    @Operation(summary = "Add a native language to user", description = "Assigns a native language to the user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Native language added successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User or Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PostMapping("/addNativeLanguage")
    public NativeLanguages addNativeLanguage(@RequestBody AddLanguageDTO addLanguageDTO) {
        return languagesService.addNativeLanguages(addLanguageDTO);
    }


    @Operation(summary = "Add a learning language to user", description = "Assigns a learning language to the user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning language added successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User or Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PostMapping("/addLearningLanguage")
    public LearningLanguage addLearningLanguage(@RequestBody AddLearningLanguageDTO addLearningLanguageDTO) {
        return languagesService.addLearningLanguage(addLearningLanguageDTO);
    }


    @Operation(summary = "Get all available languages", description = "Returns a list of all languages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of languages retrieved successfully",
                    content = @Content(mediaType = "application/json"))})
    @GetMapping
    public List<Languages> getAllNativeLanguage() {
        return languagesService.getAllLanguage();
    }

    @Operation(summary = "Get users by native language", description = "Returns a list of users who have the specified native language.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))})
    @GetMapping("/nativeLanguage")
    public List<UserData> getUserByNativeLanguage(@RequestParam String languageName) {
        return languagesService.getAllUserByNativeLanguages(languageName);
    }


    @Operation(summary = "Get users by learning language", description = "Returns a list of users who are learning the specified language.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/learningLanguages")
    public List<UserData> getUserByLearningLanguage(@RequestParam String languages) {
        return languagesService.getAllUserByLearningLanguage(languages);
    }

    @Operation(summary = "Get users by native and learning language", description = "Returns a list of users who have the specified native and learning language.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/learningnative")
    public List<UserData> getUserByLearningAndNativeLanguage(@RequestBody FindUserByNativeAndLearningDTO findUserByNativeAndLearningDTO) {
        return languagesService.getAllUserByLearningAndNativeLanguages(findUserByNativeAndLearningDTO);
    }

    @Operation(summary = "Delete a learning language from user", description = "Removes a learning language from the user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Learning language removed successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User or Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @DeleteMapping("deleteLearning")
    public UserData deleteLearningLanguage(@RequestParam Long userId, @RequestParam Long languagesId) {
        return languagesService.deleteLearningLanguage(userId, languagesId);
    }

    @Operation(summary = "Delete a native language from user", description = "Removes a native language from the user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Native language removed successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "User or Language not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @DeleteMapping("deleteNative")
    public UserData deleteNativeLanguage(@RequestParam Long userId, @RequestParam Long languagesId) {
        return languagesService.deleteNativeLanguage(userId, languagesId);
    }
}
