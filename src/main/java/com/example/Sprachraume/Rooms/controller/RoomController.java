package com.example.Sprachraume.Rooms.controller;


import com.example.Sprachraume.Exceptions.ApiExceptionHanding;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import com.example.Sprachraume.Rooms.entity.DTO.CreateNewRoomDTORequest;
import com.example.Sprachraume.Rooms.entity.DTO.OnlineUsersResponseDTO;
import com.example.Sprachraume.Rooms.entity.DTO.RoomFullDTO;
import com.example.Sprachraume.Rooms.entity.DTO.RoomParticipationDTO;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @Operation(summary = "Create a new room", description = "Allows a user to create a new room with specified parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room successfully created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Validation failed (missing fields or incorrect times)", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "409", description = "Max participant limit exceeded", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PostMapping
    public RoomFullDTO createNewRoom(@RequestParam Long userId, @RequestBody CreateNewRoomDTORequest room) {
        return roomService.createdNewRoom(userId, room);
    }


    @Operation(summary = "User requests to join a room", description = "User sends a join request to a specific room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Join request successfully sent", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User or room not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "409", description = "Already requested or already a participant", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PostMapping("/participant/invite/sendInvitation")
    public ParticipantDTO requestToJoinRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.requestToJoinRoom(userId, roomId);
    }

    @Operation(summary = "Accept invitation to a room", description = "User accepts an invitation to a specific room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation successfully accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Participant or room not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "409", description = "Invitation already accepted or declined", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PutMapping("/participant/invite/accept")
    public ParticipantDTO acceptInvitationByUser(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.acceptInvitationByUser(userId, roomId);
    }

    @Operation(summary = "Decline invitation to a room", description = "User declines an invitation to a specific room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation successfully declined", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Participant not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class))),
            @ApiResponse(responseCode = "409", description = "Invitation already accepted or declined", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })

    @PutMapping("/participant/invite/decline")
    public ParticipantDTO declineInvitationByUser(@RequestParam Long userId,Long roomId) {
        return roomService.declineInvitationByUser(userId,roomId);
    }

    @Operation(summary = "Get rooms where user accepted the invitation", description = "Получите все комнаты, где пользователь принял приглашение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted rooms retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/participant/invite/getAccept")
    public List<RoomFullDTO> getAcceptedRoomsByUser(@RequestParam Long userId) {
        return roomService.getAcceptedRoomsByUser(userId);
    }

    @Operation(summary = "Get pending room join requests sent by user", description = "Получите комнаты, где пользователь отправил запрос на присоединениеt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending invitations retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("participant/invite/getPending")
    public List<RoomFullDTO> getPendingInvitationsByUser(@RequestParam Long userId) {
        return roomService.getPendingInvitationsByUser(userId);
    }

    @Operation(summary = "Get invitations received by user", description = "Получите все приглашения на комнату, полученные пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending invitations received", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/participant/invite/received")
    public List<RoomFullDTO> getPendingInvitationsReceivedByUser(Long userId) {
        return roomService.getPendingInvitationsReceivedByUser(userId);
    }

    @Operation(summary = "Extend room end time", description = "Продлить время окончания определенной комнаты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room time extended", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PutMapping("/extendTime")
    public RoomFullDTO extendTime(@RequestParam Long roomId, @RequestBody OffsetDateTime endTime) {
        return roomService.extendTime(roomId, endTime);
    }

    @Operation(summary = "Invite user to room", description = "Администратор приглашает пользователя в определенную комнату")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User invited", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User or Room not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PostMapping("/adminRoom/invite")
    public ParticipantDTO inviteUserToRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.inviteUserToRoom(userId, roomId);
    }

    @Operation(summary = "Get pending invitations sent by admin", description = "Получите все ожидающие приглашения, отправленные Room Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending invites retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Room not found or access denied", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/adminRoom/checkPendingInvite")
    public List<ParticipantDTO> getPendingInviteByAdmin(@RequestParam Long creatorId, @RequestParam Long roomId) {
        return roomService.getPendingInviteByAdmin(creatorId, roomId);
    }

    @Operation(summary = "Get pending join requests from users", description = "Получите запросы пользователя присоединиться к комнате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Requests retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Room not found or access denied", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/adminRoom/invite/participant")
    public List<ParticipantDTO> getPendingRequestsSentByUsers(Long creatorId, Long roomId) {
        return roomService.getPendingRequestsSentByUsers(creatorId, roomId);
    }

    @Operation(summary = "Get accepted invitations", description = "Администратор получает список пользователей, которые приняли его приглашения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted invites retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Room not found or access denied", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/adminRoom/invite/accept")
    public List<ParticipantDTO> getAcceptedInviteByAdmin(Long creatorId, Long roomId) {
        return roomService.getAcceptedInviteByAdmin(creatorId, roomId);
    }

    @Operation(summary = "Accept join request", description = "Администратор принимает запрос пользователя присоединиться к комнате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request accepted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Participant not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PutMapping("/adminRoom/accept")
    public ParticipantDTO acceptRequestByAdmin(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.acceptRequestByAdmin(userId,roomId);
    }

    @Operation(summary = "Decline join request", description = "Администратор отказывает от запроса пользователя присоединиться к комнате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request declined", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Participant not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @PutMapping("/adminRoom/decline")
    public ParticipantDTO declineRequestByAdmin(@RequestParam Long userId,@RequestParam Long roomId) {
        return roomService.declineRequestByAdmin(userId,roomId);
    }

    @GetMapping("/adminRoom/allRoom")
    public List<RoomFullDTO> findAllRoomByCreator(@RequestParam Long userId){
        return roomService.findAllRoomsByCreator(userId);
    }

    @Operation(summary = "Все созданыекомнаты", description = "Получить список всех доступных комнат")
    @GetMapping("/allRoom")
    public List<RoomFullDTO> getAllRoom() {
        return roomService.getAllRoom();
    }

    @Operation(summary = "Отфильтровать комнаты по нескольким параметрам(Язык,статус комнаты,минимальный возраст, категория", description = "Получить список комнат по фильтрам")
    @GetMapping("/filter")
    public List<RoomFullDTO> filterRooms(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Long minAge,
            @RequestParam(required = false) String category
    ) {
        return roomService.filterRooms(language, status, minAge,category);
    }

    @PostMapping("/online")
    public ResponseEntity<OnlineUsersResponseDTO> plusOnline(@RequestParam Long userId, @RequestParam Long roomId) {
        return ResponseEntity.ok(roomService.plusOnline(userId, roomId));
    }


    @DeleteMapping("/online")
    public ResponseEntity<OnlineUsersResponseDTO> minusOnline(@RequestParam Long userId, @RequestParam Long roomId) {
        return ResponseEntity.ok(roomService.minusOnline(userId, roomId));
    }

    @Operation(summary = "показать все комнаты в которых участвовал или будет пользователь")
    @GetMapping("/roomStatus")
    public List<RoomParticipationDTO> getAllRoomByUser(@RequestParam Long userId){
        return roomService.getAllRoomByUser(userId);
    }

    @Operation(summary = "Get a room by ID", description = "Retrieve a specific room by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content(schema = @Schema(implementation = ApiExceptionHanding.class)))
    })
    @GetMapping("/id")
    public RoomFullDTO getRoom(@RequestParam Long roomId) {
        return roomService.getRoom(roomId);
    }


    @GetMapping("/active")
    public Page<RoomFullDTO> getAllRoomByStatus(@RequestParam boolean status,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        return roomService.getAllRoomByStatus(status, page, size);
    }



    // @GetMapping("/allRoom")
//    public List<Room> getAllParticipantRoom(@RequestParam Long participantId) {
//        return roomService.getAllParticipantRoom(participantId);
//    }
}
