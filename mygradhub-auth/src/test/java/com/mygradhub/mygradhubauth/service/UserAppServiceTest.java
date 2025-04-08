package com.mygradhub.mygradhubauth.service;

import com.mygradhub.mygradhubauth.application.dto.UserRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserResponseDTO;
import com.mygradhub.mygradhubauth.application.service.UserAppService;
import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.model.UserRole;
import com.mygradhub.mygradhubauth.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserAppServiceTest {

    private UserService userService;
    private UserAppService userAppService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userAppService = new UserAppService(userService);
    }

    @Test
    void shouldCreateUser() {
        UserRequestDTO request = new UserRequestDTO("john", "john@example.com", "pass", "USER", "photo.png");
        User savedUser = new User(1L, "john", "john@example.com", "pass", UserRole.USER, "photo.png");

        when(userService.create(any(User.class))).thenReturn(savedUser);

        UserResponseDTO response = userAppService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("john");
        verify(userService).create(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                new User(1L, "alice", "alice@example.com", "pass", UserRole.ADMIN, null),
                new User(2L, "bob", "bob@example.com", "pass", UserRole.USER, "photo.jpg")
        );

        when(userService.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userAppService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserResponseDTO::getUsername).containsExactly("alice", "bob");
    }

    @Test
    void shouldFindUserById() {
        User user = new User(1L, "john", "john@example.com", "pass", UserRole.USER, "photo.png");

        when(userService.findById(1L)).thenReturn(user);

        UserResponseDTO response = userAppService.findById(1L);

        assertThat(response.getUsername()).isEqualTo("john");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void shouldUpdateUser() {
        UserRequestDTO request = new UserRequestDTO("john", "john@newmail.com", "newpass", "USER", "newphoto.png");
        User updated = new User(1L, "john", "john@newmail.com", "newpass", UserRole.USER, "newphoto.png");

        when(userService.update(eq(1L), any(User.class))).thenReturn(updated);

        UserResponseDTO response = userAppService.update(1L, request);

        assertThat(response.getEmail()).isEqualTo("john@newmail.com");
        assertThat(response.getProfilePhoto()).isEqualTo("newphoto.png");
    }

    @Test
    void shouldDeleteUserById() {
        doNothing().when(userService).deleteById(1L);

        userAppService.deleteById(1L);

        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNullInConvertSafely() {
        List<UserResponseDTO> result = userAppService.convertSafely(null);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldIgnoreNullUsersInConvertSafely() {
        List<User> input = new java.util.ArrayList<>();
        input.add(null);
        input.add(new User(1L, "john", "email", "pass", UserRole.USER, "photo"));

        List<UserResponseDTO> result = userAppService.convertSafely(input);
        assertThat(result).hasSize(1);
    }

}
