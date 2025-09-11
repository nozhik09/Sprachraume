package com.example.Sprachraume.Rooms.entity.DTO;

import com.example.Sprachraume.Participant.entity.ParticipantDTO;
import com.example.Sprachraume.UserData.entity.DTO.UserFullResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;


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
    private CreatorInRoomDTO creator;
//    private Long creator;
//    private String creatorNickName;
//    private String creatorName;
//    private String creatorSurname;
//    private String creatorAvatar;
//    private double creatorRating;
    private Set<Long> participants;
    private Long countOnlineUser;
}
