package com.mygradhub.mygradhubauth.service;

import com.mygradhub.mygradhubauth.domain.exception.BusinessRuleException;
import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.model.UserRole;
import com.mygradhub.mygradhubauth.domain.repository.UserRepository;
import com.mygradhub.mygradhubauth.domain.service.UserServiceImpl;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    public static final String ENCRYPTED_PASSWORD = "123Hash*";

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private User savedUser;
    private User updatedUser;
    private final Long userId = 1L;
    private final String email = "user@gmail.com";
    private final String username = "user123";
    private final String password = "123Senha*";

    @BeforeEach
    void setUp() {
        user = new User(
                username,
                email,
                password,
                UserRole.USER,
                "url...");
        savedUser = new User(
                userId,
                username,
                email,
                ENCRYPTED_PASSWORD,
                UserRole.USER,
                "url...");
        updatedUser = new User(
                userId,
                "newUsername",
                "usernewemail@gmail.com",
                ENCRYPTED_PASSWORD,
                UserRole.USER,
                "url...");
    }

    @Test
    void createUserShouldEcryptPassword() {
        when(encoder.encode(password)).thenReturn(ENCRYPTED_PASSWORD);
        when(repository.existsByUsername(user.getUsername())).thenReturn(false);
        when(repository.save(user)).thenReturn(savedUser);

        User createdUser = service.create(user);

        assertThat(createdUser.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
        assertThat(createdUser.getId()).isEqualTo(userId);
        verify(repository).save(user);
    }

    @Test
    void createUserWithExistingUsernameShouldThrowBusinessRuleException() {
        when(encoder.encode(password)).thenReturn(ENCRYPTED_PASSWORD);
        when(repository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() -> service.create(user))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(AppConstants.USERNAME_ALREADY_EXISTS);

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void findByIdShouldReturnUserWhenUserExists() {
        when(repository.findById(userId)).thenReturn(Optional.of(savedUser));
        User findedUser = service.findById(userId);
        assertThat(findedUser).isEqualTo(savedUser);
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_BY_ID + userId);
    }

    @Test
    void findByEmailShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByEmail(email))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_BY_EMAIL + email);
    }

    @Test
    void findByEmailShouldReturnUserWhenUserExists() {
        when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));
        User findedUser = service.findByEmail(email);
        assertThat(findedUser).isEqualTo(savedUser);
    }

    @Test
    void findAllShouldReturnAllUsers() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<User> usersList = service.findAll();

        assertThat(usersList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoUsersExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_BY_ID + userId);
    }

    @Test
    void updateShouldUpdateUserFieldsWhenUserExists() {
        String newUsername = "newUsername";
        String newEmail = "usernewemail@gmail.com";

        when(repository.findById(userId)).thenReturn(Optional.of(savedUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);

        User result = service.update(userId, updatedUser);

        assertThat(result.getUsername()).isEqualTo(newUsername);
        assertThat(result.getEmail()).isEqualTo(newEmail);
        verify(repository).save(updatedUser);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(userId, user))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_TO_UPDATE_BY_ID + userId);
    }

    @Test
    void deleteByIdShouldReturnTrueWhenUserIsDeleted() {
        when(repository.existsById(userId)).thenReturn(false);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        boolean isDeleted = service.deleteById(userId);

        assertThat(isDeleted).isTrue();
        verify(repository).deleteById(userId);
    }

    @Test
    void deleteByIdShouldReturnFalseWhenUserIsDeleted() {
        when(repository.existsById(userId)).thenReturn(true);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        boolean isDeleted = service.deleteById(userId);

        assertThat(isDeleted).isFalse();
    }

    @Test
    void deleteByIdShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_TO_DELETE_BY_ID + userId);
    }

    @Test
    void validateUsernameShouldThrowExceptionWhenUsernameExists() {
        when(repository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() -> service.validateUsername(username))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(AppConstants.USERNAME_ALREADY_EXISTS);
    }

    @Test
    void validateUsernameShouldNotThrowExceptionWhenUsernameIsUnique() {
        when(repository.existsByUsername(username)).thenReturn(false);

        assertThatNoException().isThrownBy(() -> service.validateUsername(username));
    }

}
