package com.example.Sprachraume.UserData.service;


import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Role.repository.RoleRepository;
import com.example.Sprachraume.UserData.Exceptions.Exception.EmailIsNotValid;
import com.example.Sprachraume.UserData.Exceptions.Exception.EmailIsUsingException;
import com.example.Sprachraume.UserData.Exceptions.Exception.InvalidRoleException;
import com.example.Sprachraume.UserData.Exceptions.Exception.PasswordIsNotValid;
import com.example.Sprachraume.UserData.entity.DTO.UserRequestDto;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public UserData registerNewUser(UserRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Request cannot be empty");
        }
        if (!isValidEmail(requestDto.getEmail())) {
            throw new EmailIsNotValid("Incorrect email format");
        }

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new EmailIsUsingException("Email is already in use");
        }

        if (!isValidPassword(requestDto.getPassword())) {
            throw new PasswordIsNotValid("The password must contain at least one capital letter, one number and one special character");
        }
        UserData userData = new UserData();
        userData.setEmail(requestDto.getEmail());
        //TODO Add bCryptPasswordEncoder
        userData.setPassword(requestDto.getPassword());
        userData.setStatus(true);

        String roleTitle = requestDto.getRole();
        if (!roleTitle.equals("ROLE_USER") && !roleTitle.equals("ROLE_LIBRARY")) {
            throw new InvalidRoleException("Недопустимая роль: " + roleTitle);
        }

        Role role = roleRepository.findByTitle(roleTitle);
        if (role == null) {
            role = new Role();
            role.setTitle(roleTitle);
            roleRepository.save(role);
        }

        userData.setRoles(Collections.singleton(role));
        userRepository.save(userData);

        // TODO Возвращать Response??
        return userData;
    }





    private boolean isValidEmail(String email) {
        // Регулярное выражение для проверки корректности email
        String emailRegex = "^(?!.*\\.\\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!_*])(?!.*\\s).{8,32}$";
        return password.matches(passwordRegex);
    }

}
