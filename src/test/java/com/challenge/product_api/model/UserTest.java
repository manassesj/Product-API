package com.challenge.product_api.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserDetailsImplementation() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .role("ROLE_USER")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ROLE_USER", user.getRole());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        user.setId(2L);
        user.setUsername("admin");
        user.setPassword("adminpass");
        user.setRole("ROLE_ADMIN");

        assertEquals(2L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("adminpass", user.getPassword());
        assertEquals("ROLE_ADMIN", user.getRole());
    }
}
