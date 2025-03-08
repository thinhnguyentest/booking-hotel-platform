package com.booking_hotel.api.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {
    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());


    public static String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("customer");
        String roles = popolateAuthorities(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+80900919))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
        return jwt;
    }

    public static String getEmailFromToken(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();

        String email = String.valueOf(claims.get("email"));
        return email;
    }

    private static String popolateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority grantedAuthority : authorities) {
            auth.add(grantedAuthority.getAuthority());
        }
        return String.join(",", auth);
    }

    public static String validateResetToken(String token) {
        try {
            // Giải mã token và kiểm tra tính hợp lệ
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(JwtConstant.SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            // Trả về email từ chủ đề của token
            return claimsJws.getBody().getSubject();
        } catch (JwtException e) {
            // Token không hợp lệ hoặc đã hết hạn
            return null;
        }
    }
}
