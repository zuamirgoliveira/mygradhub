package com.mygradhub.mygradhubauth.application.dto;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class AuthRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;


    public AuthRequestDTO(String john, String pass, String mail) {
    }
}
