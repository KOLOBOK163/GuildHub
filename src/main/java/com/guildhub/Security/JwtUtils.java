package com.guildhub.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;


@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token_expiration}")
    private int expiration;

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .claim("user_id", userPrincipal.getUser().getId())
                .claim("role", new ArrayList<>(userPrincipal.getUser().getRoles()))
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsernameFromJwtToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
