package com.mygradhub.mygradhubauth.application.controller;

import com.mygradhub.mygradhubauth.application.dto.UserResponseDTO;
import com.mygradhub.mygradhubauth.application.service.UserAppService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserAppService userAppService;

    public UserController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userAppService.findAll());
    }

}
