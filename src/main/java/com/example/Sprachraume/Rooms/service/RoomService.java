package com.example.Sprachraume.Rooms.service;


import com.example.Sprachraume.DailyRoomService.DailyRoomService;
import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.entity.ParticipantType;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Rooms.entity.CreateNewRoomDTORequest;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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


    public Room createdNewRoom(Long id, CreateNewRoomDTORequest room) {
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
        }
        boolean isActive = room.getEndTime().isAfter(LocalDateTime.now());
        newRoom.setStatus(isActive);
        newRoom.setParticipants(new HashSet<>());
        String dailyRoomUrl = dailyRoomService.createDailyRoom();
        newRoom.setRoomUrl(dailyRoomUrl);
        return roomRepository.save(newRoom);
    }

    // админ комнаты приглашает юзера
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

        Optional<Participant> optionalParticipant = participantRepository.findByRoomAndUser(room, user);

        if (optionalParticipant.isPresent()) {
            Participant participant = optionalParticipant.get();

            if (participant.getStatus() == ParticipantStatus.DECLINED) {
                participant.setStatus(ParticipantStatus.PENDING);
                participant.setParticipantType(ParticipantType.INVITED_BY_CREATOR);
                return participantRepository.save(participant);
            } else {
                throw new InvitationAlreadyResponded("User is already invited or participating");
            }
        }

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(user);
        participant.setStatus(ParticipantStatus.PENDING);
        participant.setParticipantType(ParticipantType.INVITED_BY_CREATOR);
        return participantRepository.save(participant);
    }


    //Пользователь принимает приглашение от адммина комнаты
    public Participant acceptInvitationByUser(Long participantId, Long roomId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Participant not found with ID: " + participantId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        if (participant.getStatus() != ParticipantStatus.PENDING) {
            throw new InvitationAlreadyResponded("Invitation already responded to");
        }
        //TODO сделать экзепшин что если на это время уже назанична встреча то он не может принять

        participant.setStatus(ParticipantStatus.ACCEPTED);
        room.getParticipants().add(participant);
        roomRepository.save(room);

        return participantRepository.save(participant);
    }

    // Админ отправил приглашение, пользователь отклоняет
    public Participant declineInvitationByUser(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Participant not found with ID: " + participantId));

        if (participant.getStatus() != ParticipantStatus.PENDING) {
            throw new InvitationAlreadyResponded("Invitation already responded to");
        }

        participant.setStatus(ParticipantStatus.DECLINED);
        return participantRepository.save(participant);
    }


    // пользователь отправляем запрос на то что бы вступить в комнату
    public Participant requestToJoinRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Optional<Participant> existingRequest = participantRepository.findByRoomAndUser(room, user);
        if (existingRequest.isPresent()) {
            throw new AlreadyUsed("You have already requested to join this room or are already a participant");
        }
        //TODO сделать экзепшин что если на это время уже назанична встреча то он не может принять
        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(user);
        participant.setStatus(ParticipantStatus.PENDING);
        participant.setParticipantType(ParticipantType.REQUESTED_BY_USER);
        return participantRepository.save(participant);
    }


    // админ комнаты смотрит приглашения пользователей которых он пригласил
    public List<Participant> getPendingInviteByAdmin(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }

        return participantRepository.findAllByRoomAndParticipantTypeAndStatus(room, ParticipantType.INVITED_BY_CREATOR, ParticipantStatus.PENDING);
    }


    // админ комнаты смотрит приглашения пользователей которыe приняли пришлашение
    public List<Participant> getAcceptedInviteByAdmin(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }

        return participantRepository.findAllByRoomAndStatus(room, ParticipantStatus.ACCEPTED);
    }

     //Админ смотрит заявки на вступление ОТ Юзера
    public List<Participant> getPendingRequestsSentByUsers(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }

        return participantRepository.findAllByRoomAndParticipantTypeAndStatus(
                room,
                ParticipantType.REQUESTED_BY_USER,
                ParticipantStatus.PENDING
        );
    }


    // админ подтверждает пришлашение
    public Participant acceptRequestByAdmin(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Request not found"));

        participant.setStatus(ParticipantStatus.ACCEPTED);
        return participantRepository.save(participant);
    }


    // админ отклоняет пришлашение
    public Participant declineRequestByAdmin(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Request not found"));

        participant.setStatus(ParticipantStatus.DECLINED);
        return participantRepository.save(participant);
    }


    //Юзер смотрит все пришлашение которые он принял
    public List<Room> getAcceptedRoomsByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Participant> acceptedParticipants = participantRepository.findAllByUserAndStatus(user, ParticipantStatus.ACCEPTED);

        return acceptedParticipants.stream()
                .map(Participant::getRoom)
                .toList();
    }


    //Юзер смотрит все пришлашение которые он отправл
    public List<Room> getPendingInvitationsByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Participant> pendingParticipants = participantRepository.findAllByUserAndParticipantTypeAndStatus(user, ParticipantType.REQUESTED_BY_USER, ParticipantStatus.PENDING);

        return pendingParticipants.stream()
                .map(Participant::getRoom)
                .toList();
    }


    //Юзер смотрит приглщения которые ЕМУ отправили
    public List<Room> getPendingInvitationsReceivedByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Participant> pendingParticipants = participantRepository.findAllByUserAndParticipantTypeAndStatus(
                user,
                ParticipantType.INVITED_BY_CREATOR,
                ParticipantStatus.PENDING
        );

        return pendingParticipants.stream()
                .map(Participant::getRoom)
                .toList();
    }


    public List<Room> filterRooms(String language, Boolean status, Long minAge) {
        Specification<Room> spec = Specification.where(null);

        if (language != null && !language.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("language"), language));
        }

        if (status != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("status"), status));
        }

        if (minAge != null) {
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("age"), minAge));
        }

        return roomRepository.findAll(spec);
    }


    public Room extendTime(Long roomId, LocalDateTime endTime) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));

        if (room.getStatus()) {
            room.setEndTime(endTime);
        }
        return room;

    }


    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Комната не найдена"));
    }

    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }


//    public List<Room> getAllParticipantRoom(Long participantId) {
//        return roomRepository.findRoomByParticipantsId(participantId);
//    }


    private int calculateAge(LocalDate birthday, LocalDate currentDate) {
        if ((birthday != null) && (currentDate != null)) {
            return Period.between(birthday, currentDate).getYears();
        } else {
            return 0;
        }
    }
    //TODO сделать фильтры по языку по статусу по юзеру по возрасту


}
