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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private String email = "user@gmail.com";

    @BeforeEach
    void setUp() {
        user = new User(
                "user123",
                "user@gmail.com",
                "123Senha*",
                UserRole.USER,
                "url...");
        savedUser = new User(
                Long.parseLong("1"),
                "user123",
                "user@gmail.com",
                ENCRYPTED_PASSWORD,
                UserRole.USER,
                "url...");
        updatedUser = new User(
                Long.parseLong("1"),
                "newUsername",
                "usernewemail@gmail.com",
                ENCRYPTED_PASSWORD,
                UserRole.USER,
                "url...");
    }

    @Test
    void createUserShouldEcryptPassword() {
        when(encoder.encode(user.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(repository.existsByUsername(user.getUsername())).thenReturn(false);
        when(repository.save(user)).thenReturn(savedUser);

        User createdUser = service.create(user);

        assertThat(createdUser.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
        assertThat(createdUser.getId()).isEqualTo(userId);
        verify(repository).save(user);
    }

    @Test
    void createUserWithExistingUsernameShouldThrowBusinessRuleException() {
        when(encoder.encode(user.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(repository.existsByUsername(user.getUsername())).thenReturn(true);

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
        int size = 10;
        List<User> users = generateUserCopies(savedUser, size);
        when(repository.findAll()).thenReturn(users);
        List<User> usersList = service.findAll();

        assertThat(usersList).hasSize(size);
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

        when(repository.findById(userId)).thenReturn(Optional.of(savedUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);

        User result = service.update(userId, updatedUser);

        assertThat(result.getUsername()).isEqualTo("newUsername");
        assertThat(result.getEmail()).isEqualTo("usernewemail@gmail.com");
        verify(repository).save(updatedUser);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(userId, new User()))
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
        when(repository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> service.validateUsername(user.getUsername()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(AppConstants.USERNAME_ALREADY_EXISTS);
    }

    @Test
    void validateUsernameShouldNotThrowExceptionWhenUsernameIsUnique() {
        when(repository.existsByUsername(user.getUsername())).thenReturn(false);
        assertThatNoException().isThrownBy(() -> service.validateUsername(user.getUsername()));
    }

    public List<User> generateUserCopies(User originalUser, int quantity) {
        return IntStream.rangeClosed(1, quantity)
                .mapToObj(i -> {
                    User copy = new User();
                    copy.setId(originalUser.getId() + i);
                    copy.setUsername(originalUser.getUsername() + i);
                    return copy;
                })
                .collect(Collectors.toList());
    }
}
