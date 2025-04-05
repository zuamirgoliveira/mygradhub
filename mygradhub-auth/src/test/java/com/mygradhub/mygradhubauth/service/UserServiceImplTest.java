package com.mygradhub.mygradhubauth.service;

import com.mygradhub.mygradhubauth.domain.exception.BusinessRuleException;
import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.model.UserRole;
import com.mygradhub.mygradhubauth.domain.repository.UserRepository;
import com.mygradhub.mygradhubauth.domain.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void createUser_shouldEcryptPassword() {
        User user = createUser();
        String encryptedPassword = "123Hash*";

        when(encoder.encode(user.getPassword())).thenReturn(encryptedPassword);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userServiceImpl.create(user);

        verify(encoder, times(1)).encode("123Senha*");
        assertEquals(encryptedPassword, savedUser.getPassword());
    }

    @Test
    void create_WithExistingUsername_ShouldThrowException() {
        User user = createUser();
        String encryptedPassword = "123Hash*";

        when(encoder.encode(user.getPassword())).thenReturn(encryptedPassword);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> userServiceImpl.create(user));

        verify(userRepository, never()).save(any(User.class));
    }

    private static User createUser() {
        return new User(
                Long.parseLong("1"),
                "user123",
                "user@gmail.com",
                "123Senha*",
                UserRole.USER,
                "url...");
    }

}
