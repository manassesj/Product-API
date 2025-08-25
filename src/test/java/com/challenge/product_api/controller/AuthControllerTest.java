package com.challenge.product_api.controller;

import com.challenge.product_api.model.User;
import com.challenge.product_api.security.JwtUtil;
import com.challenge.product_api.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("encodedPassword")
                .roles("USER")
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken("testuser")).thenReturn("fake-jwt-token");

        String token = authController.login(request);

        assertEquals("fake-jwt-token", token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).generateToken("testuser");
    }

    @Test
    void testLoginFailure() {
        AuthRequest request = new AuthRequest();
        request.setUsername("invalidUser");
        request.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        try {
            authController.login(request);
        } catch (RuntimeException e) {
            assertEquals("Invalid credentials", e.getMessage());
        }

        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testRegisterSuccess() {
        AuthRequest request = new AuthRequest();
        request.setUsername("newuser");
        request.setPassword("newpassword");

        User mockUser = new User();
        mockUser.setUsername("newuser");
        mockUser.setPassword("encodedPassword");

        when(userService.register(anyString(), anyString())).thenReturn(mockUser);

        String response = authController.register(request);

        assertEquals("User registered successfully", response);
        verify(userService, times(1)).register("newuser", "newpassword");
    }
}
