package com.example.Sprachraume.Rooms.service;


import com.example.Sprachraume.Category.entity.Category;
import com.example.Sprachraume.Category.CategoryRepository;
import com.example.Sprachraume.DailyRoomService.DailyRoomService;
import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.entity.ParticipantType;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Rooms.entity.DTO.*;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ModelMapper modelMapper;

    private final DailyRoomService dailyRoomService;
    private final CategoryRepository categoryRepository;


    public RoomFullDTO createdNewRoom(Long id, CreateNewRoomDTORequest room) {
        if (room.getMaxQuantity() == null) {
            throw new NullOrEmptyException("Max Quantity mast be not null");
        }
        if (room.getTopic() == null || room.getTopic().isEmpty()) {
            throw new NullOrEmptyException("Topic is required");
        }
        if (room.getLanguage() == null || room.getLanguage().isEmpty()) {
            throw new NullOrEmptyException("Language is required");
        }
        if (room.getStartTime() == null) {
            throw new NullOrEmptyException("Start time is required");
        }
        if (room.getEndTime().isBefore(room.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (room.getPrivateRoom() == null) {
            throw new NullOrEmptyException("Status mast be not null");
        }
        if (room.getLanguageLvl() == null) {
            throw new NullOrEmptyException("LanguageLvl mast be not null");
        }
        Category category = categoryRepository.findByName(room.getCategory()).orElseThrow(() -> new UserNotFoundException("The selected category does not exist"));

        UserData userData = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not Found"));
        if (userData.getRating() < 3D) {
            throw new UserHaveLowRatingException("You have low rating");
        }
        Room newRoom = new Room();
        newRoom.setParticipants(new HashSet<>());
        newRoom.setCreator(userData);
        newRoom.setStatus(true);
        newRoom.setMinQuantity(4L);
        newRoom.setTopic(room.getTopic());

        if (room.getAge()==null){
            newRoom.setAge(0L);
        }else {
            newRoom.setAge(room.getAge());
        }

        newRoom.setLanguage(room.getLanguage());
        newRoom.setStartTime(room.getStartTime());
        newRoom.setEndTime(room.getEndTime());
        newRoom.setCategory(category);
        newRoom.setLanguageLvl(room.getLanguageLvl());
        newRoom.setPrivateRoom(room.getPrivateRoom());
        newRoom.setQuantityParticipant(1L);
        roomRepository.save(newRoom);
        Participant participant = new Participant();
        participant.setUser(userData);
        participant.setRoom(newRoom);
        participant.setStatus(ParticipantStatus.ACCEPTED);
        participant.setParticipantType(ParticipantType.CREATOR);
        newRoom.getParticipants().add(participant);
        long durationInMinutes = Duration.between(room.getStartTime(), room.getEndTime()).toMinutes();
        newRoom.setDuration(durationInMinutes);

        if (room.getMaxQuantity() > 25) {
            throw new MaxQuantityException("Max Quantity 25 participant");
        } else {
            newRoom.setMaxQuantity(room.getMaxQuantity());
        }
        boolean isActive = room.getEndTime().isAfter(OffsetDateTime.now());
        newRoom.setStatus(isActive);
        String dailyRoomUrl = dailyRoomService.createDailyRoom();
        newRoom.setRoomUrl(dailyRoomUrl);
        participantRepository.save(participant);
        Room savedRoom = roomRepository.save(newRoom);
        return mapRoomToFullDTO(savedRoom);
    }

    // админ комнаты приглашает юзера
    public Participant inviteUserToRoom(Long userId, Long roomID) {
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new RoomNotFoundException("There is no such room"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getBirthdayDate() == null) {
            throw new UserBirthdayNotSetException("User does not have a birthday set");
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
                throw new InvitationAlreadyRespondedException("User is already invited or participating");
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
            throw new InvitationAlreadyRespondedException("Invitation already responded to");
        }
        participant.setStatus(ParticipantStatus.ACCEPTED);
        room.getParticipants().add(participant);
        room.setQuantityParticipant(room.getQuantityParticipant() + 1);
        roomRepository.save(room);

        return participantRepository.save(participant);
    }

    // Админ отправил приглашение, пользователь отклоняет
    public Participant declineInvitationByUser(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("Participant not found with ID: " + participantId));


        participant.setStatus(ParticipantStatus.DECLINED);
        return participantRepository.save(participant);
    }


    // пользователь отправляем запрос на то что бы вступить в комнату
    public Participant requestToJoinRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (calculateAge(user.getBirthdayDate(),LocalDate.now())<room.getAge()){
            throw new UserTooYoungException("Вы не прошли проверку на возраст");

        }

        Optional<Participant> existingRequest = participantRepository.findByRoomAndUser(room, user);
        if (existingRequest.isPresent()) {
            throw new AlreadyUsedException("You have already requested to join this room or are already a participant");
        }


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

        Room room = roomRepository.findRoomByParticipants(participant);
        room.setQuantityParticipant(room.getQuantityParticipant() + 1L);
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


    public List<Room> filterRooms(String language, Boolean status, Long minAge, String category) {
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

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("age"), category));
        }


        return roomRepository.findAll(spec);
    }


    public OnlineUsersResponseDTO plusOnline(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (room.getRoomOnlineUsers().contains(userData)) {
            throw new AlreadyUsedException("Пользователь уже онлайн в комнате");
        }

        int userAge = calculateAge(userData.getBirthdayDate(), LocalDate.now());

        if (room.getAge() != null && userAge < room.getAge()) {
            throw new UserTooYoungException("You are not going through the age");
        }

        room.getRoomOnlineUsers().add(userData);
        roomRepository.save(room);
        List<OnlineUserDTO> onlineUserDTOs = room.getRoomOnlineUsers().stream()
                .map(u -> new OnlineUserDTO(u.getId(), u.getUsername(), u.getAvatar()))
                .toList();

        return new OnlineUsersResponseDTO(onlineUserDTOs, onlineUserDTOs.size());
    }


    public OnlineUsersResponseDTO minusOnline(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        room.getRoomOnlineUsers().remove(userData);
        roomRepository.save(room);

        List<OnlineUserDTO> onlineUserDTOs = room.getRoomOnlineUsers().stream()
                .map(u -> new OnlineUserDTO(u.getId(), u.getUsername(), u.getAvatar()))
                .toList();

        return new OnlineUsersResponseDTO(onlineUserDTOs, onlineUserDTOs.size());
    }

    public List<RoomParticipationDTO> getAllRoomByUser(Long userId) {
        List<Participant> participants = participantRepository.findByUserId(userId);

        return participants.stream()
                .map(p -> {
                    Room room = p.getRoom();
                    RoomShortDTO dto = new RoomShortDTO(
                            room.getId(),
                            room.getTopic(),
                            room.getStartTime(),
                            room.getEndTime(),
                            room.getDuration(),
                            room.getCategory().getName(),
                            room.getLanguageLvl(),
                            room.getQuantityParticipant(),
                            room.getStatus(),
                            room.getAge(),
                            room.getLanguage()
                    );
                    return new RoomParticipationDTO(dto, p.getStatus(), p.getParticipantType());
                })
                .toList();
    }


    public Room extendTime(Long roomId, OffsetDateTime endTime) {
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

    public List<Room> findAllRoomsByCreator(Long userId){
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return roomRepository.findRoomsByCreator(userData);
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


    public RoomFullDTO mapRoomToFullDTO(Room room) {
        List<ParticipantDTO> participantDTOs = room.getParticipants().stream()
                .map(p -> new ParticipantDTO(
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getStatus(),
                        p.getParticipantType()
                ))
                .toList();
       CreatorRoomDto creatorRoomDto = modelMapper.map(room.getCreator(),CreatorRoomDto.class);

        return new RoomFullDTO(
                room.getId(),
                room.getTopic(),
                room.getStartTime(),
                room.getEndTime(),
                room.getDuration(),
                room.getLanguage(),
                room.getLanguageLvl(),
                room.getAge(),
                room.getPrivateRoom(),
                room.getMinQuantity(),
                room.getMaxQuantity(),
                room.getQuantityParticipant(),
                room.getStatus(),
                room.getRoomUrl(),
                room.getCategory().getName(),
                creatorRoomDto,
                participantDTOs
        );
    }


}
