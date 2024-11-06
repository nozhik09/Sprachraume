package com.example.Sprachraume.Rooms.controller;


import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping
    public Room createNewRoom(@RequestParam Long userId, @RequestBody Room room){
      return   roomService.createdNewRoom(userId,room);
    }

    @PostMapping("/{roomId}/invite")
    public Participant inviteUserToRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        return roomService.inviteUserToRoom(roomId, userId);
    }

    @PostMapping("/participant/{participantId}/accept")
    public Participant acceptInvitation(@PathVariable Long participantId) {
        return roomService.acceptInvitation(participantId);
    }

    @PostMapping("/participant/{participantId}/decline")
    public Participant declineInvitation(@PathVariable Long participantId) {
        return roomService.declineInvitation(participantId);
    }




}
