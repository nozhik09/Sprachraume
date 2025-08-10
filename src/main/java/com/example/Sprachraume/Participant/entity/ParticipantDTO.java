package com.example.Sprachraume.Participant.entity;


import com.example.Sprachraume.Rooms.entity.DTO.RoomFullDTO;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.DTO.UserFullResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    private Long id;
    private Long userId;
    private ParticipantStatus status;
    private ParticipantType participantType;
    private Long roomId;

}
