package com.example.Sprachraume.Rooms.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDTO {
    private Long id;
    private String username;
    private String avatarUrl;
}