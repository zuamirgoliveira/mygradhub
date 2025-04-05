package com.mygradhub.mygradhubauth.domain.service;

import com.mygradhub.mygradhubauth.domain.exception.BusinessRuleException;
import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.repository.UserRepository;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.repository = userRepository;
        this.encoder = encoder;
    }


    @Override
    public User create(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        validateUsername(user.getUsername());
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_BY_ID + "{}", id);
                    return new EntityNotFoundException(AppConstants.USER_NOT_FOUND_BY_ID + id);
                });
    }

    @Override
    public User update(Long id, User updatedUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_TO_UPDATE_BY_ID + "{}", id);
                    return new EntityNotFoundException(AppConstants.USER_NOT_FOUND_TO_UPDATE_BY_ID + id);
                });

        fillUpdateUser(user, updatedUser);
        return repository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_BY_EMAIL + "{}", email);
                    return new EntityNotFoundException(AppConstants.USER_NOT_FOUND_BY_EMAIL + email);
                });
    }

    @Override
    public boolean deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_TO_DELETE_BY_ID + "{}", id);
                    return new EntityNotFoundException(AppConstants.USER_NOT_FOUND_TO_DELETE_BY_ID + id);
                });
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    @Override
    public void validateUsername(String username) {
        if(repository.existsByUsername(username)) {
            throw new BusinessRuleException(AppConstants.USERNAME_ALREADY_EXISTS);
        }
    }

    private void fillUpdateUser(User user, User updatedUser) {
        if (updatedUser != null) {
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getRole() != null) {
                user.setRole(updatedUser.getRole());
            }
            if (updatedUser.getProfilePhoto() != null) {
                user.setProfilePhoto(updatedUser.getProfilePhoto());
            }
        }
    }

}
