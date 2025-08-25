package com.challenge.product_api.security;

import io.jsonwebtoken  .Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {


    private final Dotenv dotenv = Dotenv.load();
    private final String SECRET_KEY = System.getenv("JWT_SECRET");
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
