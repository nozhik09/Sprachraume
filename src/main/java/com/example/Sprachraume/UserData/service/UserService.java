package com.example.Sprachraume.UserData.service;


import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Role.repository.RoleRepository;
import com.example.Sprachraume.UserData.entity.DTO.UserRequestDto;
import com.example.Sprachraume.UserData.entity.DTO.UserUpdateRequestDto;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        userData.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        userData.setStatus(true);
        userData.setRating(5L);

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

        //TODO Рейтинг появляется сразу? или Начинается с нуля и добавляется? максимальный рейтинг? минимальный ? если ниже минимума блокируется?

        userData.setRoles(Collections.singleton(role));
        userRepository.save(userData);

        // TODO Возвращать Response??
        return userData;
    }

    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }


    public UserData gitUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", id)));
    }

    public UserData updateUser(UserUpdateRequestDto requestDto) {
        UserData userData = userRepository.findById(requestDto.getId()).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", requestDto.getId())));

        userData.setNickname(requestDto.getNickName());
        userData.setName(requestDto.getName());
        userData.setSurname(requestDto.getSurname());
        userData.setFoto(requestDto.getFoto());
        userData.setBirthdayDate(requestDto.getBirthdayDate());
        userData.setInternalCurrency(requestDto.getInternalCurrency());
        userData.setLearningLanguage(requestDto.getLearningLanguage());
        userData.setNativeLanguage(requestDto.getNativeLanguage());
        userData.setSkillLevel(requestDto.getSkillLevel());
        userRepository.save(userData);
        return userData;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %s не найден", email)));
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
