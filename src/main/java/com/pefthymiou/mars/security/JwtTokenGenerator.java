package com.pefthymiou.mars.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pefthymiou.mars.security.configuration.SecurityParams;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.pefthymiou.mars.security.configuration.SecurityParams.*;

@Component
public class JwtTokenGenerator {

    private static final String EMPTY = "";

    public String tokenFor(Authentication authResult) {
        return JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withIssuedAt(Date.from(Instant.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.EXPIRATION_TIME))
                .withArrayClaim(ROLES_KEY, authoritiesFrom(authResult))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String extractUserFrom(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, EMPTY))
                .getSubject();
    }

    public List<SimpleGrantedAuthority> extractAuthoritiesFrom(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, EMPTY))
                .getClaims()
                .get(ROLES_KEY)
                .asList(SimpleGrantedAuthority.class);
    }

    private String[] authoritiesFrom(Authentication authResult) {
        return ((User) authResult.getPrincipal())
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }
}
