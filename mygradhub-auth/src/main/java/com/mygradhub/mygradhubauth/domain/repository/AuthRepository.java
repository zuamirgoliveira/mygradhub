package com.mygradhub.mygradhubauth.domain.repository;

import com.mygradhub.mygradhubauth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findByUsername(String username);

}
