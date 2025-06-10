package com.example.Sprachraume.Rooms.entity.DTO;

import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFullDTO {

    private Long id;
    private String topic;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long duration;
    private String language;
    private String languageLvl;
    private Long age;
    private Boolean privateRoom;
    private Long minQuantity;
    private Long maxQuantity;
    private Long quantityParticipant;
    private Boolean status;
    private String roomUrl;
    private String categoryName;
    private CreatorRoomDto creatorName;
    private List<ParticipantDTO> participants;
}
