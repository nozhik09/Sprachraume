package com.example.Sprachraume.Rooms.entity.DTO;

import com.example.Sprachraume.Category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomShortDTO {
    private Long id;
    private String topic;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long duration;
    private String category;
    private String languageLvl;
    private Long quantityParticipant;
    private Boolean status;
    private Long age;
    private String language;





}
