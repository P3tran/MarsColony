package com.pefthymiou.mars.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pefthymiou.mars.base.builders.AppUserBuilder;
import com.pefthymiou.mars.user.infrastructure.db.AppUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JwtAuthenticationFilterShould {

    private static final String USERNAME = "bob";
    private static final String PASSWORD = "123";
    private static final AppUser APP_USER = AppUserBuilder.aUser().withUsername(USERNAME).withPassword(PASSWORD).build();
    private static final String TOKEN = "Bearer 12345";

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> captor;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenGenerator jwtTokenGenerator;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ServletInputStream servletInputStream;
    @Mock
    Authentication authResult;
    @Mock
    FilterChain chain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setup() {
        initMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenGenerator, objectMapper);
    }

    @Test
    public void attemptAuthenticationWithUserCredentials() throws IOException {
        when(request.getInputStream()).thenReturn(servletInputStream);
        when(objectMapper.readValue(servletInputStream, AppUser.class)).thenReturn(APP_USER);

        jwtAuthenticationFilter.attemptAuthentication(request, response);

        verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo(USERNAME);
        assertThat(captor.getValue().getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    public void addHeaderOnSuccessfulAuthentication() throws IOException, ServletException {
        when(jwtTokenGenerator.tokenFor(authResult)).thenReturn(TOKEN);

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authResult);

        verify(response).addHeader("Authorization", "Bearer " + TOKEN);
    }
}
