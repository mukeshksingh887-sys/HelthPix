package com.api_gateway.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtService {


    private final String SECRET_KEY = "mySuperSecretJwtKeyForHospitalManagementSystem2026";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }




    public Claims extractClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {

        return Long.valueOf(
                extractClaims(token)
                        .get("userId")
                        .toString());
    }



    //    @Override
    public String extractEmail(String token) {

        return extractClaims(token).getSubject();
    }

    //    @Override
    public String extractRole(String token) {

        return extractClaims(token)
                .get("role")
                .toString();
    }

    //    @Override
    public boolean isTokenExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }


    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (Exception ex) {

            return false;
        }
    }





}
