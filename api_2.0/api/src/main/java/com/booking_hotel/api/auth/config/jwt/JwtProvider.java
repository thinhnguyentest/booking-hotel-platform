package com.booking_hotel.api.auth.config.jwt;

import com.booking_hotel.api.role.entity.Role;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class JwtProvider {
    public static SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(JwtConstant.SECRET_KEY);
        return new SecretKeySpec(decodedKey, "HmacSHA512");
    }

    public static String generateToken(String username, Set<Role> roles) {
        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        String roleUsers = popolateAuthorities(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 80900919))
                .claim("username", username)
                .claim("authorities", roleUsers)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
        return jwt;
    }

    private static String popolateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        List<String> authoritiesList = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            authoritiesList.add(authority.getAuthority());
        }
        return new Gson().toJson(authoritiesList);
    }


    public static String getUserNameByToken(String token) {
        try {
            System.out.println(token);
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(JwtConstant.SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            // Get subject's value from token
            String subject = claimsJws.getBody().get("username").toString();
            if (subject == null || subject.isEmpty()) {
                return null;
            }

            return subject;
        } catch (JwtException e) {
            return null;
        }
    }

}
