package com.mygradhub.mygradhubauth.application.controller;

import com.mygradhub.mygradhubauth.application.dto.AuthRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.AuthResponseDTO;
import com.mygradhub.mygradhubauth.application.dto.UserRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserResponseDTO;
import com.mygradhub.mygradhubauth.application.service.UserAppService;
import com.mygradhub.mygradhubauth.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserAppService userAppService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserAppService userAppService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userAppService = userAppService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "signup", security = {})
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Validated @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userAppService.create(userRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @Operation(summary = "login", security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Validated @RequestBody AuthRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
