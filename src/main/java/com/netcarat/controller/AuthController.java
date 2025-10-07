package com.netcarat.controller;

import com.netcarat.config.JwtUtil;
import com.netcarat.modal.LoginRequest;
import com.netcarat.modal.LoginResponse;
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

