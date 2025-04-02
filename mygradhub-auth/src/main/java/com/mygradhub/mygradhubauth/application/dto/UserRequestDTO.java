package com.mygradhub.mygradhubauth.application.dto;

import com.mygradhub.mygradhubauth.domain.model.UserRole;
import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class UserRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private UserRole role;

    private String profilePhoto;
}
