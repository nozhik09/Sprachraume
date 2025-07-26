package com.example.Sprachraume.Mapping;

import com.example.Sprachraume.Languages.entity.DTO.Response.LanguageResponseDTO;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Rooms.entity.DTO.RoomFullDTO;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.DTO.UserFullResponseDto;
import com.example.Sprachraume.UserData.entity.UserData;

import java.util.HashSet;
import java.util.stream.Collectors;

public class Mapper {
    public static UserFullResponseDto userToFullResponseDto(UserData user) {
        UserFullResponseDto dto = new UserFullResponseDto();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSurname(user.getSurname());
        dto.setBirthdayDate(user.getBirthdayDate());
        dto.setAvatar(user.getAvatar());
        dto.setRating(user.getRating());
        dto.setInternalCurrency(user.getInternalCurrency());
        dto.setStatus(user.getStatus());
        dto.setRoles(new HashSet<>(user.getRoles()));
        dto.setNativeLanguages(user.getNativeLanguages().stream().map(Mapper::mapToNativeLanguage).collect(Collectors.toSet()));
        dto.setLearningLanguages(user.getLearningLanguages().stream().map(Mapper::mapToLearningLanguage).collect(Collectors.toSet()));
        dto.setCreatedRooms(user.getCreatedRooms().stream().map(Room::getId).collect(Collectors.toSet()));

        return dto;
    }



    public static RoomFullDTO mapCreatedRooms(Room room, UserData user) {
        RoomFullDTO dto = new RoomFullDTO();
        dto.setId(room.getId());
        dto.setTopic(room.getTopic());
        dto.setStartTime(room.getStartTime());
        dto.setEndTime(room.getEndTime());
        dto.setDuration(room.getDuration());
        dto.setLanguage(room.getLanguage());
        dto.setLanguageLvl(room.getLanguageLvl());
        dto.setAge(room.getAge());
        dto.setPrivateRoom(room.getPrivateRoom());
        dto.setMinQuantity(room.getMinQuantity());
        dto.setMaxQuantity(room.getMaxQuantity());
        dto.setQuantityParticipant(room.getQuantityParticipant());
        dto.setStatus(room.getStatus());
        dto.setRoomUrl(room.getRoomUrl());
        dto.setCategoryName(room.getCategory() != null ? room.getCategory().getName() : null);
        dto.setCreator(room.getCreator().getId());
        dto.setParticipants(room.getParticipants().stream().map(Participant::getId).collect(Collectors.toSet()));
        return dto;

    }

    public static ParticipantDTO mapParticipant(Participant participant) {
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setParticipantType(participant.getParticipantType());
        participantDTO.setStatus(participant.getStatus());
        participantDTO.setUserId(participant.getUser().getId());
        participantDTO.setRoomId(participant.getRoom().getId());


        return participantDTO;
    }


    public static RoomFullDTO mapToRooms(Room room) {
        RoomFullDTO dto = new RoomFullDTO();
        dto.setId(room.getId());
        dto.setTopic(room.getTopic());
        dto.setStartTime(room.getStartTime());
        dto.setEndTime(room.getEndTime());
        dto.setDuration(room.getDuration());
        dto.setLanguage(room.getLanguage());
        dto.setLanguageLvl(room.getLanguageLvl());
        dto.setAge(room.getAge());
        dto.setPrivateRoom(room.getPrivateRoom());
        dto.setMinQuantity(room.getMinQuantity());
        dto.setMaxQuantity(room.getMaxQuantity());
        dto.setQuantityParticipant(room.getQuantityParticipant());
        dto.setStatus(room.getStatus());
        dto.setRoomUrl(room.getRoomUrl());
        dto.setCreator(room.getCreator().getId());
        dto.setCategoryName(room.getCategory() != null ? room.getCategory().getName() : null);
        dto.setParticipants(room.getParticipants().stream().map(Participant::getId).collect(Collectors.toSet()));
        return dto;
    }

    public static LanguageResponseDTO mapToNativeLanguage(NativeLanguages languages){
        LanguageResponseDTO languageResponseDTO = new LanguageResponseDTO();

        languageResponseDTO.setId(languages.getId());
        languageResponseDTO.setLanguageId(languages.getLanguage().getId());
        languageResponseDTO.setUserId(languages.getUser().getId());
        languageResponseDTO.setSkillLvl(languages.getSkillLevel());
        return languageResponseDTO;
    }

    public static LanguageResponseDTO mapToLearningLanguage(LearningLanguage languages){
        LanguageResponseDTO languageResponseDTO = new LanguageResponseDTO();
        languageResponseDTO.setId(languages.getId());
        languageResponseDTO.setLanguageId(languages.getLanguage().getId());
        languageResponseDTO.setUserId(languages.getUser().getId());
        languageResponseDTO.setSkillLvl(languages.getSkillLevel());
        return languageResponseDTO;
    }





}