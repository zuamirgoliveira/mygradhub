package com.mygradhub.mygradhubauth.domain.service;

import com.mygradhub.mygradhubauth.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User create(User user);
    List<User> findAll();
    User findById(Long id);
    User update(Long id, User updatedUser);
    User findByEmail(String email);
    boolean deleteById(Long id);
    void validateUsername(String username);

}
