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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    public static final String ENCRYPTED_PASSWORD = "123Hash*";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private User savedUser;

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
    }

    @Test
    void createUserShouldEcryptPassword() {
        when(encoder.encode(user.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(savedUser);

        User createdUser = userServiceImpl.create(user);

        assertThat(createdUser.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
        assertEquals(createdUser.getId(), savedUser.getId());
        verify(userRepository).save(user);
    }

    @Test
    void createUserWithExistingUsernameShouldThrowBusinessRuleException() {
        when(encoder.encode(user.getPassword())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userServiceImpl.create(user))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(AppConstants.USERNAME_ALREADY_EXISTS);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByIdShouldReturnUserWhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(savedUser));
        User findedUser = userServiceImpl.findById(user.getId());
        assertThat(findedUser).isEqualTo(savedUser);
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userServiceImpl.findById(user.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(AppConstants.USER_NOT_FOUND_BY_ID + user.getId());
    }



}
