package com.challenge.product_api.service;

import com.challenge.product_api.model.User;
import com.challenge.product_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPass")
                .role("ROLE_USER")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                service.loadUserByUsername("missingUser"));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testRegister_Success() {
        String rawPassword = "mypassword";
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        User savedUser = User.builder().id(1L).username("newuser").password(encodedPassword).build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = service.register("newuser", rawPassword);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        User existingUser = new User();
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(RuntimeException.class, () ->
                service.register("existingUser", "pass"));

        assertEquals("Username already exists", exception.getMessage());
    }
}
