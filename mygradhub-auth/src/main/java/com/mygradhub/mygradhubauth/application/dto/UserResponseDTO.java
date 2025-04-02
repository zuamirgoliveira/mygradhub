package com.mygradhub.mygradhubauth.application.dto;

import com.mygradhub.mygradhubauth.domain.model.User;
import com.mygradhub.mygradhubauth.domain.model.UserRole;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class UserResponseDTO {

    private Long id;

    private String username;

    private String email;

    private UserRole role;

    private String profilePhoto;

    public UserResponseDTO(Long id, String username, String email, UserRole role, String profilePhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profilePhoto = profilePhoto;
    }

}
