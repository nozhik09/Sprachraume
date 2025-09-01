package com.example.Sprachraume.Rooms.service;


import com.example.Sprachraume.Category.CategoryRepository;
import com.example.Sprachraume.Category.entity.Category;
import com.example.Sprachraume.DailyRoomService.DailyRoomService;
import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Mapping.Mapper;
import com.example.Sprachraume.Notification.entity.Notification;
import com.example.Sprachraume.Notification.entity.NotificationCategory;
import com.example.Sprachraume.Notification.entity.NotificationContextType;
import com.example.Sprachraume.Notification.repository.NotificationRepository;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.entity.ParticipantType;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Rooms.entity.DTO.*;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import com.example.Sprachraume.UserData.entity.DTO.UserFullResponseDto;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;

    private final DailyRoomService dailyRoomService;
    private final CategoryRepository categoryRepository;

    //+++
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

        if (room.getAge() == null) {
            newRoom.setAge(0L);
        } else {
            newRoom.setAge(room.getAge());
        }
        newRoom.setLanguage(room.getLanguage());
        newRoom.setStartTime(room.getStartTime());
        newRoom.setEndTime(room.getEndTime());
        newRoom.setCategory(category);
        newRoom.setLanguageLvl(room.getLanguageLvl());
        newRoom.setPrivateRoom(room.getPrivateRoom());
        newRoom.setQuantityParticipant(1L);
        newRoom.setCountOnlineUser(0L);
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
        roomRepository.save(newRoom);
        return Mapper.mapCreatedRooms(newRoom, userData);
    }


    // админ комнаты приглашает юзера
    @Transactional
    public ParticipantDTO inviteUserToRoom(Long userId, Long roomID) {
        Room room = roomRepository.findById(roomID)
                .orElseThrow(() -> new RoomNotFoundException("There is no such room"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserData creatorRoom = room.getCreator();

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
                participantRepository.save(participant);
                return modelMapper.map(participant, ParticipantDTO.class);
            } else {
                throw new InvitationAlreadyRespondedException("User is already invited or participating");
            }
        }

        Participant participant = new Participant();
        participant.setRoom(room);
        participant.setUser(user);
        participant.setStatus(ParticipantStatus.PENDING);
        participant.setParticipantType(ParticipantType.INVITED_BY_CREATOR);
        participantRepository.save(participant);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(room.getCreator().getId());
        notification.setCategory(NotificationCategory.INVITE_TO_ROOM);
        notification.setTitle("User invite you to room " + room.getTopic());
        notification.setBody("User invite you to room");
        notification.setContextType(NotificationContextType.SENDER);
        notification.setContextId(room.getId());

        notificationRepository.save(notification);
        return modelMapper.map(participant, ParticipantDTO.class);
    }


    //Пользователь принимает приглашение от адммина комнаты
    @Transactional
    public ParticipantDTO acceptInvitationByUser(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        Participant participant = participantRepository.findParticipantByUserIdAndRoomId(userId, roomId).orElseThrow(()
                -> new UserNotFoundException("Participant not found with ID: " + userId));


        if (participant.getStatus() != ParticipantStatus.PENDING) {
            throw new InvitationAlreadyRespondedException("Invitation already responded to");
        }
        participant.setStatus(ParticipantStatus.ACCEPTED);
        room.getParticipants().add(participant);
        room.setQuantityParticipant(room.getQuantityParticipant() + 1);
        roomRepository.save(room);
        Notification notification = new Notification();
        notification.setUserId(room.getCreator().getId());
        notification.setActorId(userId);
        notification.setCategory(NotificationCategory.ACCEPT_INVITE);
        notification.setTitle("User accept your invite in room " + room.getTopic());
        notification.setBody("User accept your invite");
        notification.setContextType(NotificationContextType.RECIPIENT);
        notification.setContextId(room.getId());
        notificationRepository.save(notification);
        participantRepository.save(participant);
        return modelMapper.map(participant, ParticipantDTO.class);
    }

    // Админ отправил приглашение, пользователь отклоняет
    @Transactional
    public ParticipantDTO declineInvitationByUser(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        Participant participant = participantRepository.findParticipantByUserIdAndRoomId(userId, roomId).orElseThrow(()
                -> new UserNotFoundException("Participant not found with ID: " + userId));


        participant.setStatus(ParticipantStatus.DECLINED);
        participantRepository.save(participant);

        Notification notification = new Notification();
        notification.setUserId(room.getCreator().getId());
        notification.setActorId(userId);
        notification.setCategory(NotificationCategory.DECLINE_INVITE);
        notification.setTitle("User decline your invite in room " + room.getTopic());
        notification.setBody("User decline your invite");
        notification.setContextType(NotificationContextType.RECIPIENT);
        notification.setContextId(room.getId());
        notificationRepository.save(notification);
        return modelMapper.map(participant, ParticipantDTO.class);
    }


    // пользователь отправляем запрос на то что бы вступить в комнату
    @Transactional
    public ParticipantDTO requestToJoinRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (calculateAge(user.getBirthdayDate(), LocalDate.now()) < room.getAge()) {
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
        participantRepository.save(participant);

        Notification notification = new Notification();
        notification.setUserId(room.getCreator().getId());
        notification.setActorId(userId);
        notification.setCategory(NotificationCategory.INVITE_TO_ROOM);
        notification.setTitle("The user sent an application for participation in the meeting" + room.getTopic());
        notification.setBody("he user sent an application for participation in the meeting");
        notification.setContextType(NotificationContextType.SENDER);
        notification.setContextId(room.getId());

        notificationRepository.save(notification);

        return modelMapper.map(participant, ParticipantDTO.class);
    }


    // админ комнаты смотрит приглашения пользователей которых он пригласил
    public List<ParticipantDTO> getPendingInviteByAdmin(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }

        return participantRepository.findAllByRoomAndParticipantTypeAndStatus(room, ParticipantType.INVITED_BY_CREATOR, ParticipantStatus.PENDING)
                .stream()
                .map(Mapper::mapParticipant)
                .collect(Collectors.toList());


    }


    // админ комнаты смотрит приглашения пользователей которыe приняли пришлашение
    public List<ParticipantDTO> getAcceptedInviteByAdmin(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }

        return participantRepository.findAllByRoomAndStatus(room, ParticipantStatus.ACCEPTED).stream().map(Mapper::mapParticipant).collect(Collectors.toList());
    }

    //Админ смотрит заявки на вступление ОТ Юзера
    public List<ParticipantDTO> getPendingRequestsSentByUsers(Long creatorId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        if (!room.getCreator().getId().equals(creatorId)) {
            throw new InvalidRoleException("Only the room creator can view requests");
        }
        return participantRepository.findAllByRoomAndParticipantTypeAndStatus(
                room,
                ParticipantType.REQUESTED_BY_USER,
                ParticipantStatus.PENDING
        ).stream().map(Mapper::mapParticipant).collect(Collectors.toList());
    }


    // админ подтверждает пришлашение
    @Transactional
    public ParticipantDTO acceptRequestByAdmin(Long userId, Long roomId) {
        Participant participant = participantRepository.findParticipantByUserIdAndRoomId(userId, roomId).orElseThrow(()
                -> new UserNotFoundException("Participant not found with ID: " + userId));

        Room room = roomRepository.findRoomByParticipants(participant);
        if (participant.getStatus().equals(ParticipantStatus.ACCEPTED)){
            throw new AlreadyUsedException("Вы уже приняли заявку от пользователя");
        }

        room.setQuantityParticipant(room.getQuantityParticipant() + 1L);
        participant.setStatus(ParticipantStatus.ACCEPTED);
        participantRepository.save(participant);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(room.getCreator().getId());
        notification.setCategory(NotificationCategory.ACCEPT_INVITE);
        notification.setTitle("RoomAdmin accept your invite in room" + room.getTopic());
        notification.setBody("RoomAdmin accept your invite in room");
        notification.setContextType(NotificationContextType.SENDER);
        notification.setContextId(room.getId());
        notificationRepository.save(notification);
        return Mapper.mapParticipant(participant);
    }


    // админ отклоняет пришлашение
    @Transactional
    public ParticipantDTO declineRequestByAdmin(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        Participant participant = participantRepository.findParticipantByUserIdAndRoomId(userId, roomId).orElseThrow(()
                -> new UserNotFoundException("Participant not found with ID: " + userId));
        participant.setStatus(ParticipantStatus.DECLINED);
        participantRepository.save(participant);
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setActorId(room.getCreator().getId());
        notification.setCategory(NotificationCategory.DECLINE_INVITE);
        notification.setTitle("RoomAdmin accept your invite in room" + room.getTopic());
        notification.setBody("RoomAdmin accept your invite in room");
        notification.setContextType(NotificationContextType.SENDER);
        notification.setContextId(room.getId());
        notificationRepository.save(notification);
        return Mapper.mapParticipant(participant);
    }


    //Юзер смотрит все пришлашение которые он принял
    public List<RoomFullDTO> getAcceptedRoomsByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Room> acceptedParticipants = participantRepository.findAllByUserAndStatus(user, ParticipantStatus.ACCEPTED)
                .stream().map(Participant::getRoom).toList();

        return acceptedParticipants.stream().map(Mapper::mapToRooms).collect(Collectors.toList());

    }


    //Юзер смотрит все пришлашение которые он отправл
    public List<RoomFullDTO> getPendingInvitationsByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Room> pendingParticipants = participantRepository.findAllByUserAndParticipantTypeAndStatus(user, ParticipantType.REQUESTED_BY_USER, ParticipantStatus.PENDING)
                .stream()
                .map(Participant::getRoom)
                .toList();

        return pendingParticipants.stream().map(Mapper::mapToRooms).collect(Collectors.toList());

    }


    //Юзер смотрит приглщения которые ЕМУ отправили
    public List<RoomFullDTO> getPendingInvitationsReceivedByUser(Long userId) {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Room> pendingParticipants = participantRepository.findAllByUserAndParticipantTypeAndStatus(
                user,
                ParticipantType.INVITED_BY_CREATOR,
                ParticipantStatus.PENDING
        ).stream().map(Participant::getRoom).toList();

        return pendingParticipants.stream().map(Mapper::mapToRooms).collect(Collectors.toList());
    }


    public List<RoomFullDTO> filterRooms(String language, Boolean status, Long minAge, String category) {

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
            spec = spec.and((root, query, builder) -> {
                Join<Object, Object> categoryJoin = root.join("category");
                return builder.equal(categoryJoin.get("name"), category); // или get("id") если нужно по id
            });
        }


        return roomRepository.findAll(spec).stream().map(Mapper::mapToRooms).collect(Collectors.toList());
    }


    public OnlineUsersResponseDTO plusOnline(Long userId, Long roomId) {
        Room room = roomRepository.findRoomWithOnlineUsers(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (userData.getBirthdayDate() == null && room.getAge() != 0) {
            throw new NullOrEmptyException("Вам нужно указать свой возраст");
        }
        int userAge = calculateAge(userData.getBirthdayDate(), LocalDate.now());

        if (room.getAge() != null && userAge < room.getAge()) {
            throw new UserTooYoungException("Вы не прошли проверку на возраст");
        }

        if (!room.getRoomOnlineUsers().contains(userData)) {
            room.getRoomOnlineUsers().add(userData);
            room.setCountOnlineUser((long) room.getRoomOnlineUsers().size());
            roomRepository.save(room);
        }

        List<UserFullResponseDto> list = room.getRoomOnlineUsers().stream().map(Mapper::userToFullResponseDto)
                .collect(Collectors.toList());

        return new OnlineUsersResponseDTO(list, room.getCountOnlineUser());
    }


    public OnlineUsersResponseDTO minusOnline(Long userId, Long roomId) {
        Room room = roomRepository.findRoomWithOnlineUsers(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (room.getRoomOnlineUsers().remove(userData)) {
            room.setCountOnlineUser((long) room.getRoomOnlineUsers().size());
            roomRepository.save(room);
        }

        List<UserFullResponseDto> list = room.getRoomOnlineUsers()
                .stream()
                .map(Mapper::userToFullResponseDto)
                .collect(Collectors.toList());

        return new OnlineUsersResponseDTO(list, room.getCountOnlineUser());
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


    public RoomFullDTO extendTime(Long roomId, OffsetDateTime endTime) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Такой комнаты не существует"));

        if (room.getStatus()) {
            room.setEndTime(endTime);
        }
        return Mapper.mapToRooms(room);
    }


    public RoomFullDTO getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Комната не найдена"));
        return Mapper.mapToRooms(room);
    }

    public List<RoomFullDTO> getAllRoom() {
        return roomRepository.findAll().stream().map(Mapper::mapToRooms).collect(Collectors.toList());
    }

    public List<RoomFullDTO> findAllRoomsByCreator(Long userId) {
        UserData userData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        return roomRepository.findRoomsByCreator(userData).stream().map(Mapper::mapToRooms).collect(Collectors.toList());
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

    //TODO изменение статуса или удаление комнаты


//    public RoomFullDTO mapRoomToFullDTO(Room room) {
//        List<ParticipantDTO> participantDTOs = room.getParticipants().stream()
//                .map(p -> new ParticipantDTO(
//                        p.getUser().getId(),
//                        p.getUser().getUsername(),
//                        p.getStatus(),
//                        p.getParticipantType()
//                ))
//                .toList();
//        CreatorRoomResponseDto creatorRoomDto = modelMapper.map(room.getCreator(), CreatorRoomResponseDto.class);
//
//        return new RoomFullDTO(
//                room.getId(),
//                room.getTopic(),
//                room.getStartTime(),
//                room.getEndTime(),
//                room.getDuration(),
//                room.getLanguage(),
//                room.getLanguageLvl(),
//                room.getAge(),
//                room.getPrivateRoom(),
//                room.getMinQuantity(),
//                room.getMaxQuantity(),
//                room.getQuantityParticipant(),
//                room.getStatus(),
//                room.getRoomUrl(),
//                room.getCategory().getName(),
//                creatorRoomDto,
//                participantDTOs
//        );
//    }


}
