package com.quiz.quizapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "supersecretkey1234567890supersecretkey"; // mind. 32 Byte
    private static final long EXPIRATION_TIME = 5 * 86400000; // 1 Tag


    public JwtService() {}

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }


    private boolean isExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
