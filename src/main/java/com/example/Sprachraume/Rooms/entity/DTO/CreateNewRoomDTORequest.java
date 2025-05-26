package com.example.Sprachraume.Rooms.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewRoomDTORequest {
    private String topic;
    private OffsetDateTime startTime;
    private OffsetDateTime  endTime;
    private boolean status;
    private Long age;
    private String language;
    private String languageLvl;
    private String category;
    private Boolean privateRoom;
    private Long minQuantity;
    private Long maxQuantity;
    private Long quantityParticipant;
}
