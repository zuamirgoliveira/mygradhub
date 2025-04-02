package com.mygradhub.mygradhubauth.application.controller;

import com.mygradhub.mygradhubauth.application.dto.AuthRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserRequestDTO;
import com.mygradhub.mygradhubauth.application.dto.UserResponseDTO;
import com.mygradhub.mygradhubauth.application.service.UserAppService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final UserAppService userAppService;

    public AuthController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!!!");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userAppService.create(userRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        UserResponseDTO authenticatedUser = userAppService.login(authRequestDTO);
        return ResponseEntity.ok(authenticatedUser);
    }
}
