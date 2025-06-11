package com.example.Sprachraume.Rooms.entity.DTO;


import com.example.Sprachraume.UserData.entity.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUsersResponseDTO {
    private List<OnlineUserDTO> users;
    private Long count;

}
