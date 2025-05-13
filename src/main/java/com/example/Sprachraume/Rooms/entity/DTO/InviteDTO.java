package com.example.Sprachraume.Rooms.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteDTO {
    private Long userId;
    private Long roomId;
}
