package com.example.Sprachraume.UserData.service;


import com.example.Sprachraume.Exceptions.Exception.*;
import com.example.Sprachraume.Languages.entity.LearningLanguage;
import com.example.Sprachraume.Languages.entity.NativeLanguages;
import com.example.Sprachraume.Languages.repository.LearningLanguageRepository;
import com.example.Sprachraume.Languages.repository.NativeLanguagesRepository;
import com.example.Sprachraume.Mapping.Mapper;
import com.example.Sprachraume.Notification.entity.Notification;
import com.example.Sprachraume.Notification.repository.NotificationRepository;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.repository.ParticipantRepository;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Role.repository.RoleRepository;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import com.example.Sprachraume.UserData.entity.DTO.UserFullResponseDto;
import com.example.Sprachraume.UserData.entity.DTO.UserRequestDto;
import com.example.Sprachraume.UserData.entity.DTO.UserUpdateRequestDto;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final ModelMapper modelMapper;
    private final NotificationRepository notificationRepository;

    public UserFullResponseDto registerNewUser(UserRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Request cannot be empty");
        }
        if (!isValidEmail(requestDto.getEmail())) {
            throw new EmailIsNotValidException("Incorrect email format");
        }

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new EmailIsUsingException("Email is already in use");
        }

        if (!isValidPassword(requestDto.getPassword())) {
            throw new PasswordIsNotValidException("The password must contain at least one capital letter, one number and one special character");
        }
        UserData userData = new UserData();
        userData.setEmail(requestDto.getEmail());
        userData.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        userData.setStatus(true);
        userData.setRating(5D);

        String roleTitle = requestDto.getRole();
        if (!roleTitle.equals("ROLE_USER") && !roleTitle.equals("ROLE_LIBRARY")) {
            throw new InvalidRoleException("Invalid role: " + roleTitle);
        }

        Role role = roleRepository.findByTitle(roleTitle);
        if (role == null) {
            role = new Role();
            role.setTitle(roleTitle);
            roleRepository.save(role);
        }
        userData.setRoles(Collections.singleton(role));
        userRepository.save(userData);
        UserFullResponseDto userFullResponseDto = modelMapper.map(userData, UserFullResponseDto.class);
        return userFullResponseDto;
    }

    public List<UserFullResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(Mapper::userToFullResponseDto).collect(Collectors.toList());
    }


    public UserFullResponseDto gitUserById(Long id) {
        UserData userData = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", id)));
        return Mapper.userToFullResponseDto(userData);
    }

    public UserFullResponseDto updateUser(UserUpdateRequestDto requestDto) {
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
        userData.setAvatar(requestDto.getAvatar());
        userData.setBirthdayDate(requestDto.getBirthdayDate());
        userData.setInternalCurrency(requestDto.getInternalCurrency());
        userRepository.save(userData);
        UserFullResponseDto userFullResponseDto = Mapper.userToFullResponseDto(userData);
        return userFullResponseDto;
    }

    public UserFullResponseDto blockUser(Long adminId, Long userId) {
        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));
        if (!user.getStatus()) {
            throw new AlreadyUsedException(String.format("User with id %s already block", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            user.setStatus(false);
            userRepository.save(user);
        } else {
            throw new InvalidRoleException("Only the administrator can block the user");
        }

        return Mapper.userToFullResponseDto(user);
    }

    public UserFullResponseDto unlockUser(Long adminId, Long userId) {
        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));
        if (user.getStatus()) {
            throw new AlreadyUsedException(String.format("User with id %s already unlock", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            user.setStatus(true);
            userRepository.save(user);

        } else {
            throw new InvalidRoleException("Only the administrator can unlock the user");
        }

        return Mapper.userToFullResponseDto(user);
    }

    public UserFullResponseDto appointAnAdministrator(Long adminId, Long userId) {

        UserData admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", adminId)));
        UserData user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with %s ID found", userId)));

        if (!user.getStatus()) {
            throw new UserIsBlockingException("You cannot assign a blocked user admin, first unlock the user");
        } else if (user.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::equals)) {
            throw new AlreadyUsedException(String.format("User with id %s already admin", userId));
        } else if (admin.getRoles().stream().map(Role::getTitle).anyMatch("ROLE_ADMIN"::contains)) {
            Role role = roleRepository.findByTitle("ROLE_ADMIN");
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new InvalidRoleException("Only the administrator can appoint an the admin");
        }

        return Mapper.userToFullResponseDto(user);

    }

    @Transactional
    public String deleteAccount(Long id) {
        UserData user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id %s not found", id)));

        learningLanguageRepository.deleteByUser(user);
        nativeLanguagesRepository.deleteByUser(user);
        participantRepository.deleteByUser(user);
        roomRepository.deleteByCreator(user);
        notificationRepository.deleteByUserId(id);
        userRepository.delete(user);
        return String.format("User C ID %S successfully deleted", id);
    }


    public List<UserFullResponseDto> getAllBlockUsers(Boolean status) {

        return userRepository.findAllByStatus(status).stream().map(Mapper::userToFullResponseDto).collect(Collectors.toList());
    }

    public List<UserFullResponseDto> getUsersByRatingBetween(Double rating) {
        double max = rating + 1;
        return userRepository.findAllByRatingBetween(rating, max).stream().map(Mapper::userToFullResponseDto).collect(Collectors.toList());
    }

    public List<UserFullResponseDto> getUsersByRating(Double rating) {
        double max = rating + 1;
        return userRepository.findAllByRatingBetween(rating, max).stream().map(Mapper::userToFullResponseDto).collect(Collectors.toList());
    }


    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String uploadDir = "uploads/avatars/";
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String filename = UUID.randomUUID() + extension;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setAvatar(filename);
        userRepository.save(user);

        return filename;

    }

    // TODO  удаление аватара


    public ResponseEntity<Resource> getAvatarByUserId(Long userId) throws IOException {
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String avatarPath = user.getAvatar();

        if (avatarPath == null || avatarPath.isBlank()) {
            throw new UserNotFoundException("avatar not found");
        }

        String filename = Paths.get(avatarPath).getFileName().toString();
        Path filePath = Paths.get("uploads/avatars").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new UserNotFoundException("sdsada");
        }

        String contentType = Files.probeContentType(filePath);
        MediaType mediaType = contentType != null
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }


    public Resource loadAvatarResource(String fileName) throws IOException {
        Path filePath = Paths.get("uploads/avatars").resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("Avatar not found: " + fileName);
        }

        return resource;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User %s not found", email)));
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

    public List<UserFullResponseDto> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword).stream().map(Mapper::userToFullResponseDto).collect(Collectors.toList());
    }

    private boolean userIsAdmin(UserData user) {
        for (Role role : user.getRoles()) {
            if ("ADMIN".equals(role.getTitle())) {
                return true;
            }
        }
        return false;
    }

}
