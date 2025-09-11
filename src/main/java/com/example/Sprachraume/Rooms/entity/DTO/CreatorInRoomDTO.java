package com.example.Sprachraume.Rooms.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatorInRoomDTO {
    private Long creator;
    private String creatorNickName;
    private String creatorName;
    private String creatorSurname;
    private String creatorAvatar;
    private double creatorRating;
}
