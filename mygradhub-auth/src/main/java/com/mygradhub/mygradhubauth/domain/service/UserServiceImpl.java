package com.mygradhub.mygradhubauth.domain.service;

import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.repository.UserRepository;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }


    @Override
    public User create(User user) {
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
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.USER_NOT_FOUND_BY_ID + id);
                });
    }

    @Override
    public User update(Long id, User updatedUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_TO_UPDATE_BY_ID + "{}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.USER_NOT_FOUND_TO_UPDATE_BY_ID + id);
                });

        fillUpdateUser(user, updatedUser);
        return repository.save(user);
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

    @Override
    public User findByUsername(String username, String password) {
        User savedUser = repository.findByUsername(username)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USERNAME_NOT_FOUND + "{}", username);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.USERNAME_NOT_FOUND + username);
                });
        return savedUser;
    }

//    private void authenticate(String password, User savedUser) {
//        if (!encoder.matches(password, savedUser.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, AppConstants.UNAUTHORIZED);
//        }
//    }

    @Override
    public User findByEmail(String email) {
        User savedUser = repository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_BY_EMAIL + "{}", email);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.USER_NOT_FOUND_BY_EMAIL + email);
                });
//        authenticate(password, savedUser);
        return savedUser;
    }

    @Override
    public boolean deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(AppConstants.USER_NOT_FOUND_TO_DELETE_BY_ID + "{}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, AppConstants.USER_NOT_FOUND_TO_DELETE_BY_ID + id);
                });
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    @Override
    public void validateUsername(String username) {
        if(repository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppConstants.USERNAME_ALREADY_EXISTS);
        }
    }

}
