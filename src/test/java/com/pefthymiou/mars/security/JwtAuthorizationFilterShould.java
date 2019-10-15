package com.pefthymiou.mars.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class JwtAuthorizationFilterShould {

    private static final String TOKEN = "Bearer 12345";
    private static final String USERNAME = "bob";

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private AuthenticatorSecurityContextHolder authenticatorSecurityContextHolder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenGenerator jwtTokenGenerator;
    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> captor;

    @Before
    public void setup() {
        initMocks(this);
        jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, jwtTokenGenerator, authenticatorSecurityContextHolder);
    }

    @Test
    public void notSetAuthenticationWhenAuthorizationTokenIsNull() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthorizationFilter.doFilterInternal(request, response, chain);

        verify(authenticatorSecurityContextHolder, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(chain).doFilter(request, response);
    }

    @Test
    public void notSetAuthenticationWhenAuthorizationTokenDoesNotHaveAToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("abc");

        jwtAuthorizationFilter.doFilterInternal(request, response, chain);

        verify(authenticatorSecurityContextHolder, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(chain).doFilter(request, response);
    }

    @Test
    public void setAuthenticationToNullWhenUserByAuthorizationTokenIsNull() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(TOKEN);
        when(jwtTokenGenerator.extractUserFrom(TOKEN)).thenReturn(null);

        jwtAuthorizationFilter.doFilterInternal(request, response, chain);

        verify(authenticatorSecurityContextHolder).setAuthentication(null);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void setAuthenticationToUserExtractedFromAuthorizationToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(TOKEN);
        when(jwtTokenGenerator.extractUserFrom(TOKEN)).thenReturn(USERNAME);
        when(jwtTokenGenerator.extractAuthoritiesFrom(TOKEN)).thenReturn(asList(new SimpleGrantedAuthority("ADMIN")));

        jwtAuthorizationFilter.doFilterInternal(request, response, chain);

        verify(authenticatorSecurityContextHolder).setAuthentication(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo(USERNAME);
        assertThat(captor.getValue().getAuthorities()).containsExactly(new SimpleGrantedAuthority("ADMIN"));
        verify(chain).doFilter(request, response);
    }
}
