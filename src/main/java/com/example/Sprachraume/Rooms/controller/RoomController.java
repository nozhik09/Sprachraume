package com.example.Sprachraume.Rooms.controller;


import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Rooms.DTO.InviteDTO;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping
    public Room createNewRoom(@RequestParam Long userId, @RequestBody Room room) {
        return roomService.createdNewRoom(userId, room);
    }


    @PostMapping("/participant/invite/sendInvitation")
    public Participant requestToJoinRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.requestToJoinRoom(userId, roomId);
    }


    @PutMapping("/participant/invite/accept")
    public Participant acceptInvitationByUser(@RequestParam Long participantId, @RequestParam Long roomId) {
        return roomService.acceptInvitationByUser(participantId, roomId);
    }

    @PutMapping("/participant/invite/decline")
    public Participant declineInvitationByUser(@RequestParam Long participantId) {
        return roomService.declineInvitationByUser(participantId);
    }

    @GetMapping("/participant/invite/getAccept")
    public List<Room> getAcceptedRoomsByUser(@RequestParam Long userId) {
        return roomService.getAcceptedRoomsByUser(userId);
    }

    @GetMapping("participant/invite/getPending")
    public List<Room> getPendingInvitationsByUser(@RequestParam Long userId) {
        return roomService.getPendingInvitationsByUser(userId);
    }

    @GetMapping("/participant/invite/received")
    public List<Room> getPendingInvitationsReceivedByUser(Long userId){
        return roomService.getPendingInvitationsReceivedByUser(userId);
    }

    @GetMapping("/id")
    public Room getRoom(@RequestParam Long roomId) {
        return roomService.getRoom(roomId);
    }


    @PutMapping("/extendTime")
    public Room extendTime(@RequestParam Long roomId, @RequestBody LocalDateTime endTime) {
        return roomService.extendTime(roomId, endTime);
    }


    @PostMapping("/adminRoom/invite")
    public Participant inviteUserToRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        return roomService.inviteUserToRoom(userId,roomId);
    }

    @GetMapping("/adminRoom/checkPendingInvite")
    public List<Participant> getPendingInviteByAdmin(@RequestParam Long creatorId, @RequestParam Long roomId) {
        return roomService.getPendingInviteByAdmin(creatorId, roomId);
    }
    
    
    @GetMapping("/adminRoom/invite/participant")
    public List<Participant> getPendingRequestsSentByUsers(Long creatorId, Long roomId){
        return roomService.getPendingRequestsSentByUsers(creatorId,roomId);
    }

    @GetMapping("/adminRoom/invite/accept")
    public List<Participant> getAcceptedInviteByAdmin(Long creatorId, Long roomId){
        return roomService.getAcceptedInviteByAdmin(creatorId, roomId);
    }



    @PutMapping("/adminRoom/accept")
    public Participant acceptRequestByAdmin(@RequestParam Long participantId) {
        return roomService.acceptRequestByAdmin(participantId);
    }

    @PutMapping("/adminRoom/decline")
    public Participant declineRequestByAdmin(@RequestParam Long participantId) {
        return roomService.declineRequestByAdmin(participantId);
    }
    
    


    @GetMapping("/filter")
    public List<Room> filterRooms(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Long minAge
    ) {
        return roomService.filterRooms(language, status, minAge);
    }

    // @GetMapping("/allRoom")
//    public List<Room> getAllParticipantRoom(@RequestParam Long participantId) {
//        return roomService.getAllParticipantRoom(participantId);
//    }
}
