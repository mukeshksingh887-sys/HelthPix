package com.auth_service.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mySuperSecretJwtKeyForHospitalManagementSystem2026";

//    private Key getKey() {
//        return Keys.hmacShaKeyFor(
//                SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Access Token (15 min)
    public String generateAccessToken(String userId, String email, String role) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("userId",userId)
                .claim("role",role)
                .claim("email",email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token (7 days)
    public String generateRefreshToken(String email, String userId, String role) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("role",role)
                .claim("email",email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // Extract Username
    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    // Check Token Expired
//    public boolean isTokenExpired(String token) {
//
//        Date expiration = Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//
//        return expiration.before(new Date());
//    }



    public Claims extractClaims(String token) {
//        return Jwts.parser()
//                .verifyWith((SecretKey) getKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();

        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
        return  claims;
    }





//    @Override
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

//    @Override
    public boolean validateToken(String token) {

        try {

            Claims claims = extractClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (ExpiredJwtException ex) {

            return false;

        } catch (MalformedJwtException ex) {

            return false;

        } catch (UnsupportedJwtException ex) {

            return false;

        } catch (IllegalArgumentException ex) {

            return false;

        } catch (JwtException ex) {

            return false;
        }
    }

}
