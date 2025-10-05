package com.demo.demo.controller;

import com.demo.demo.config.JwtUtil;
import com.demo.demo.modal.LoginRequest;
import com.demo.demo.modal.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // Perform user authentication (e.g., check username and password)
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            return new LoginResponse(JwtUtil.generateToken(loginRequest.getUsername()));
        }
        throw new IllegalArgumentException("Invalid username or password");
    }
}

