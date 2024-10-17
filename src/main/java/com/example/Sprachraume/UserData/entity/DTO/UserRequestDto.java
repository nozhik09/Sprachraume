package com.example.Sprachraume.UserData.entity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {


    private String email;

    private String password;

    private String role;
}
