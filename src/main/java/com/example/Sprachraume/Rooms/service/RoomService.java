package com.example.Sprachraume.Rooms.service;


import com.example.Sprachraume.DailyRoomService.DailyRoomService;
import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    private final DailyRoomService dailyRoomService;


    public Room createdNewRoom(Long id, Room room) {
        if (room.getTopic() == null || room.getTopic().isEmpty()) {
            throw new IllegalArgumentException("Topic is required");
        }
        if (room.getLanguage() == null || room.getLanguage().isEmpty()) {
            throw new IllegalArgumentException("Language is required");
        }
        if (room.getStartTime() == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        if (room.getEndTime().isBefore(room.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        UserData userData = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not Found"));
        Room newRoom = new Room();
        newRoom.setCreator(userData);
        newRoom.setStatus(true);
        newRoom.setMinQuantity(4L);
        newRoom.setTopic(room.getTopic());
        newRoom.setAge(room.getAge());
        newRoom.setLanguage(room.getLanguage());
        newRoom.setStartTime(room.getStartTime());
        newRoom.setEndTime(room.getEndTime());
        long durationInMinutes = Duration.between(room.getStartTime(), room.getEndTime()).toMinutes();
        newRoom.setDuration(durationInMinutes);

        if (room.getMaxQuantity() > 25) {
            throw new IllegalArgumentException("Max Quantity 25 participant");
        } else {
            newRoom.setMaxQuantity(room.getMaxQuantity());
        }//TODO +++++Добавить приглашенных пользователей
        //TODO +++++если время комнаты закончилась, комната перекидывалась в архив
        boolean isActive = room.getEndTime().isAfter(LocalDateTime.now());
        newRoom.setStatus(isActive);
        newRoom.setParticipants(new HashSet<>());
        String dailyRoomUrl = dailyRoomService.createDailyRoom();
        newRoom.setRoomUrl(dailyRoomUrl);
        return roomRepository.save(newRoom);
    }

    public Participant inviteUserToRoom(Long userId, Long roomID) {
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new RoomNotFoundException("There is no such room"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getBirthdayDate() == null) {
            throw new UserBirthdayNotSet("User does not have a birthday set");
        }

        int userAge = calculateAge(user.getBirthdayDate(), LocalDate.now());

        if (room.getAge() != null && userAge < room.getAge()) {
            throw new UserTooYoungException("User does not meet the age requirement for this room");
        }

        //TODO +++++добавиьть проверку на возраст

        Optional<Participant> optionalParticipant = participantRepository.findByRoomAndUser(room, user);

        if (optionalParticipant.isPresent()) {
            Participant participant = optionalParticipant.get();

            if (participant.getStatus() == ParticipantStatus.DECLINED) {
                participant.setStatus(ParticipantStatus.PENDING);
                return participantRepository.save(participant);
            } else {
                throw new InvitationAlreadyResponded("User is already invited or participating");
            }
        }

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(user);
        participant.setStatus(ParticipantStatus.PENDING);
        return participantRepository.save(participant);
    }


    public Participant acceptInvitation(Long participantId, Long roomId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Participant not found with ID: " + participantId));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));
        if (participant.getStatus() != ParticipantStatus.PENDING) {
            throw new InvitationAlreadyResponded("Invitation already responded to");
        }

        participant.setStatus(ParticipantStatus.ACCEPTED);
        room.getParticipants().add(participant);
        roomRepository.save(room);
        return participantRepository.save(participant);
    }

    public Participant declineInvitation(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Participant not found with ID: " + participantId));

        if (participant.getStatus() != ParticipantStatus.PENDING) {
            throw new InvitationAlreadyResponded("Invitation already responded to");
        }

        participant.setStatus(ParticipantStatus.DECLINED);
        return participantRepository.save(participant);
    }


    public Room extendTime(Long roomId ,LocalDateTime endTime){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));

        if (room.getStatus()){
            room.setEndTime(endTime);
        }
        return room;
        //TODO ++++продилть время комнаты
    }


    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new UserNotFoundException("Комната не найдена"));
    }

    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }


    public List<Room> getAllParticipantRoom(Long participantId) {
        return roomRepository.findRoomByParticipantsId(participantId);
    }


    private int calculateAge(LocalDate birthday, LocalDate currentDate) {
        if ((birthday != null) && (currentDate != null)) {
            return Period.between(birthday, currentDate).getYears();
        } else {
            return 0;
        }
    }
    //TODO сделать фильтры по языку по статусу по юзеру по возрасту


}
