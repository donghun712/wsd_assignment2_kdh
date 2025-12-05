package com.example.bookstore.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.github.cdimascio.dotenv.Dotenv;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExp;
    private final long refreshTokenExp;

    public JwtUtil() {

        // ★★★ 여기에서 .env 파일 읽는다 ★★★
        Dotenv dotenv = Dotenv.load();

        String secret = dotenv.get("JWT_SECRET");
        long accessExp = Long.parseLong(dotenv.get("JWT_ACCESS_TOKEN_EXP"));
        long refreshExp = Long.parseLong(dotenv.get("JWT_REFRESH_TOKEN_EXP"));

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExp = accessExp * 1000;   // 초 → ms
        this.refreshTokenExp = refreshExp * 1000; // 초 → ms
    }

    public String generateAccessToken(Long userId, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExp);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Long userId, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExp);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);   // 서명 검증 + Claims 파싱
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
}
