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

    @PostMapping("/invite")
    public Participant inviteUserToRoom(@RequestBody InviteDTO inviteDTO) {
        return roomService.inviteUserToRoom(inviteDTO.getUserId(), inviteDTO.getRoomId());
    }

    @PostMapping("/participant/accept")
    public Participant acceptInvitation(@RequestParam Long participantId, @RequestParam Long roomId) {
        return roomService.acceptInvitation(participantId, roomId);
    }

    @PostMapping("/participant/decline")
    public Participant declineInvitation(@RequestParam Long participantId) {
        return roomService.declineInvitation(participantId);
    }

    @GetMapping("/id")
    public Room getRoom(@RequestParam Long roomId) {
        return roomService.getRoom(roomId);
    }

    @GetMapping("/allRoom")
    public List<Room> getAllParticipantRoom(@RequestParam Long participantId) {
        return roomService.getAllParticipantRoom(participantId);
    }


    @PutMapping("/extendTime")
    public Room extendTime(@RequestParam Long roomId, @RequestBody LocalDateTime endTime) {
        return roomService.extendTime(roomId, endTime);
    }


}
