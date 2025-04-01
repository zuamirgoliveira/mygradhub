package com.mygradhub.mygradhubauth.application.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @GetMapping("/signup")
    public ResponseEntity<String> signup() {
        return ResponseEntity.ok("Hello World!");
    }
}
