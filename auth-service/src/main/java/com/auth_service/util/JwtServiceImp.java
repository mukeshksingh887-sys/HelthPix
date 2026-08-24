package com.auth_service.util;

import com.auth_service.entity.Permission;
import com.auth_service.entity.Role;
import com.auth_service.entity.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Service
public class JwtServiceImp {


    private final Key key;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtServiceImp(
            @Value("${jwt.secret}")
            String secret,
            @Value("${jwt.access-token-expiration}")
            long accessExpiration,
            @Value("${jwt.refresh-token-expiration}")
            long refreshExpiration){

        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }




    public String generateAccessToken(UserAccount user) {

        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add("ROLE_" + role.getName());
        }



        Set<String> permissionSet = new HashSet<>();
        for (Role role : user.getRoles()) {
            if (role.getPermissions() != null) {
                for (Permission permission : role.getPermissions()) {
                    permissionSet.add(permission.getName());
                }
            }
        }
        List<String> permissions = new ArrayList<>(permissionSet);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessExpiration
                        )
                )
                .signWith(key)
                .compact();
    }


 // genrateRefreshToken
    public String generateRefreshToken(UserAccount user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + refreshExpiration
                        )
                )
                .signWith(key)
                .compact();
    }

// extract Claims
    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    //extractUsername
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }


    // check validation
    public boolean isValid(String token) {

        try {
            Claims claims = extractClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (JwtException |
                 IllegalArgumentException e) {

            return false;
        }
    }

}
