package com.challenge.product_api.controller;

import com.challenge.product_api.security.JwtUtil;
import com.challenge.product_api.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userService;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        log.info("Login attempt for user '{}'", authRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user.getUsername());

            log.info("User '{}' authenticated successfully", authRequest.getUsername());
            return token;

        } catch (Exception e) {
            log.warn("Failed login attempt for user '{}': {}", authRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        log.info("Registering new user '{}'", authRequest.getUsername());

        userService.register(authRequest.getUsername(), authRequest.getPassword());

        log.info("User '{}' registered successfully", authRequest.getUsername());
        return "User registered successfully";
    }
}

class AuthRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
