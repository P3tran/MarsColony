package com.pefthymiou.mars.security;

import com.pefthymiou.mars.security.configuration.SecurityParams;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthenticatorSecurityContextHolder authenticatorSecurityContextHolder;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenGenerator jwtTokenGenerator, AuthenticatorSecurityContextHolder authenticatorSecurityContextHolder) {
        super(authenticationManager);
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.authenticatorSecurityContextHolder = authenticatorSecurityContextHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(SecurityParams.AUTHORIZATION_HEADER);

        if (token == null || !token.startsWith(SecurityParams.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = extractAuthenticationFrom(token);
        authenticatorSecurityContextHolder.setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken extractAuthenticationFrom(String token) {
        String user = jwtTokenGenerator.extractUserFrom(token);
        List<SimpleGrantedAuthority> authorities = jwtTokenGenerator.extractAuthoritiesFrom(token);

        if (user != null)
            return new UsernamePasswordAuthenticationToken(user, null, authorities);

        return null;
    }
}
