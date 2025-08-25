package com.challenge.product_api.service;

import com.challenge.product_api.model.User;
import com.challenge.product_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.debug("User '{}' loaded from database", username); // debug, não aparece em produção
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .roles("USER")
                            .build();
                })
                .orElseThrow(() -> {
                    log.warn("User '{}' not found", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.error("Attempt to register with an existing username '{}'", username);
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        log.info("New user registered successfully: '{}'", username);
        return savedUser;
    }
}
