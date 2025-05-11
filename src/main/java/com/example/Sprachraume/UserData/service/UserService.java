package com.example.Sprachraume.UserData.service;


import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Languages.repository.LearningLanguageRepository;
import com.example.Sprachraume.Languages.repository.NativeLanguagesRepository;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Role.repository.RoleRepository;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final NativeLanguagesRepository nativeLanguagesRepository;
    private final LearningLanguageRepository learningLanguageRepository;


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
        userData.setRating(5D);

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
        return userData;
    }

    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }


    public UserData gitUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", id)));
    }

    public UserData updateUser(UserUpdateRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Request cannot be empty");
        }
        if (requestDto.getId() == null) {
            throw new IllegalArgumentException("Request cannot be empty");
        }

        UserData userData = userRepository.findById(requestDto.getId()).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", requestDto.getId())));
        if (requestDto.getBirthdayDate() != null) {
            LocalDate minimumDate = LocalDate.now().minusYears(8);
            if (requestDto.getBirthdayDate().isAfter(minimumDate)) {
                throw new IllegalArgumentException("User must be at least 8 years old");
            }
        }
        userData.setNickname(requestDto.getNickName());
        userData.setName(requestDto.getName());
        userData.setSurname(requestDto.getSurname());
        userData.setFoto(requestDto.getFoto());
        userData.setBirthdayDate(requestDto.getBirthdayDate());
        userData.setInternalCurrency(requestDto.getInternalCurrency());
        userRepository.save(userData);
        return userData;
    }

    public UserData blockUser(Long adminId, Long userId) {
        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));
        if (!user.getStatus()) {
            throw new AlreadyUsed(String.format("User with id %s already block", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            user.setStatus(false);
            userRepository.save(user);
        } else {
            throw new InvalidRoleException("Only the administrator can block the user");
        }

        return user;
    }

    public UserData unlockUser(Long adminId, Long userId) {
        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));
        if (user.getStatus()) {
            throw new AlreadyUsed(String.format("User with id %s already unlock", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            user.setStatus(true);
            userRepository.save(user);

        } else {
            throw new InvalidRoleException("Only the administrator can unlock the user");
        }

        return user;
    }

    public UserData appointAnAdministrator(Long adminId, Long userId) {

        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));

        if (!user.getStatus()) {
            throw new UserIsBlocking("You cannot assign a blocked user admin, first unlock the user");
        } else if (user.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            throw new AlreadyUsed(String.format("User with id %s already admin", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::contains)) {
            Role role = roleRepository.findByTitle("ROLE_ADMIN");
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new InvalidRoleException("Only the administrator can appoint an the admin");
        }

        return user;

    }


    public String deleteAccount(Long id) {
        UserData user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id %s not found", id)));
        List<NativeLanguages> nativeLanguagesList = nativeLanguagesRepository.findAllByUser(user);
        if (!nativeLanguagesList.isEmpty()) {
            nativeLanguagesRepository.deleteAll(nativeLanguagesList);
            user.getNativeLanguages().clear();
        }
        List<LearningLanguage> learningLanguageList = learningLanguageRepository.findAllByUser(user);
        if (!learningLanguageList.isEmpty()) {
            learningLanguageRepository.deleteAll(learningLanguageList);
            user.getLearningLanguages().clear();
        }
        List<Room> roomList = roomRepository.findAllByCreator(user);
        if (!roomList.isEmpty()) {
            roomRepository.deleteAll(roomList);
            user.getCreatedRooms().clear();
        }
        List<Participant> participantList = participantRepository.findByUser(user);
        if (!participantList.isEmpty()) {
            participantRepository.deleteAll(participantList);
        }
        userRepository.delete(user);
        return String.format("User C ID %S successfully deleted", id);
    }


    public List<UserData> getAllBlockUsers(Boolean status) {
        return userRepository.findAllByStatus(status);
    }

    public List<UserData> getUsersByRatingBetween(Double rating) {
        double max = rating +1;
        return userRepository.findAllByRatingBetween(rating,max);
    }

    public List<UserData> getUsersByRating(Double rating) {
        double max = rating +1;
        return userRepository.findAllByRatingBetween(rating,max);
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

    public List<UserData> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    //TODO ++++++Поииск по имени по фамилии никнейм или имейл


}
