package com.mygradhub.mygradhubauth.application.dto;

import com.mygradhub.mygradhubauth.domain.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank
    private String username;

    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*\\d).{8,}$",
            message = "Password must be minimum 8 characters"
    )
    private String password;

    private UserRole role;

    private String profilePhoto;

    public UserRequestDTO(String john, String mail, String pass, String user, String image) {
    }
}
