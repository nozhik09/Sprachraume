package com.example.Sprachraume.Rooms.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewRoomDTORequest {
    private String topic;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean status;
    private Long age;
    private String language;
    private Long minQuantity;
    private Long maxQuantity;
}
