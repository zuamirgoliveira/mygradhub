package com.mygradhub.mygradhubauth.application.service;

import com.mygradhub.mygradhubauth.application.dto.AuthRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserResponseDTO;
import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserAppService {

    private final UserService service;

    public UserAppService(UserService service) {
        this.service = service;
    }

    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        User user = new User(userRequestDTO.getUsername(),
                userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                userRequestDTO.getRole(),
                userRequestDTO.getProfilePhoto());

        User savedUser = service.create(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getProfilePhoto());
    }

    public List<UserResponseDTO> findAll() {
        List<User> usersList =  service.findAll();
        return convertSafely(usersList);
    }

    public UserResponseDTO findById(Long id) {
        User user = service.findById(id);
        return new UserResponseDTO(id,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getProfilePhoto());
    }

    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        User updatedUser = service.update(id,
                new User(id,
                        userRequestDTO.getUsername(),
                        userRequestDTO.getEmail(),
                        userRequestDTO.getPassword(),
                        userRequestDTO.getRole(),
                        userRequestDTO.getProfilePhoto()));
        return new UserResponseDTO(id,
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getProfilePhoto());
    }

    public List<UserResponseDTO> convertSafely(List<User> users) {
        return Optional.ofNullable(users)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getProfilePhoto()
                ))
                .toList();
    }
}
