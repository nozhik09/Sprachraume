package com.example.Sprachraume.Rooms.entity.DTO;


import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.entity.ParticipantType;
import com.example.Sprachraume.Rooms.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomParticipationDTO {
    private RoomShortDTO room;
    private ParticipantStatus status;
    private ParticipantType type;
}
