package com.example.Sprachraume.security.sec_service;


import com.example.Sprachraume.Exceptions.Exception.InvalidTokenException;
import com.example.Sprachraume.Exceptions.Exception.TokenExpiredException;
import com.example.Sprachraume.Role.Role;
import com.example.Sprachraume.Role.repository.RoleRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenService {

    private SecretKey accessSecretKey;
    private  SecretKey refreshSecretKey;
    private RoleRepository roleRepository;
    private final int ACCESS_TOKEN_EXPIRES_TIME = 7;
    private final int REFRESH_TOKEN_EXPIRES_TIME = 30;



    public TokenService(@Value("${key.access}") String accessSecretPhrase,
                        @Value("${key.refresh}") String refreshSecretKey,
                        @Autowired RoleRepository roleRepository) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretPhrase));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.roleRepository = roleRepository;
    }
    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessSecretKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshSecretKey);
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaimsFromToken(accessToken, accessSecretKey);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaimsFromToken(refreshToken, refreshSecretKey);
    }



    public String generateAccessToken(UserData userData) {

        return Jwts.builder()
                .subject(userData.getEmail())
                .expiration(calcExpiryDate(ACCESS_TOKEN_EXPIRES_TIME))
                .signWith(accessSecretKey)
                .claim("roles", userData.getAuthorities())
                .compact();
    }

    public String generateRefreshToken(UserData userData) {
        return Jwts.builder()
                .subject(userData.getUsername())
                .expiration(calcExpiryDate(REFRESH_TOKEN_EXPIRES_TIME))
                .signWith(refreshSecretKey)
                .compact();


    }

    public AuthInfo mapClaimsToAuthInfo(Claims claims) {

        String email = claims.getSubject();
        List<LinkedHashMap<String, String>> roles;
        roles = (List<LinkedHashMap<String, String>>) claims.get("roles");

        Set<Role> roleSet = new HashSet<>();
        for (LinkedHashMap<String, String> roleMap : roles) {
            String roleTitle = roleMap.get("authority");
            Role role = roleRepository.findByTitle(roleTitle);
            if (role != null) {
                roleSet.add(role);
            }
        }



        return new AuthInfo(email, roleSet);
    }

    public boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser().verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                throw new TokenExpiredException("Token has expired");
            } else {
                throw new InvalidTokenException("Invalid token: " + e.getMessage());
            }
        }
    }

    private Claims getClaimsFromToken(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date calcExpiryDate(int days) {
        LocalDateTime now = LocalDateTime.now();

        Instant instant = now.plusDays(days)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Date.from(instant);
    }

}
