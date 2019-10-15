package com.pefthymiou.mars.acceptance;

import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.MarsDsl;
import com.pefthymiou.mars.security.configuration.WebSecurityConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static com.pefthymiou.mars.dsl.Data.BAD_CREDENTIALS;
import static com.pefthymiou.mars.dsl.Data.CREDENTIALS;
import static com.pefthymiou.mars.dsl.Parser.jsonWith;
import static com.pefthymiou.mars.dsl.UserDsl.ITUserBuilder.aUser;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MarsApplication.class, WebSecurityConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginFeature {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MarsDsl marsDsl;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).alwaysDo(print()).build();
    }

    @Test
    public void performLogin() throws Exception {
        givenAnExistingUser();

        theUserShouldBeAbleToLogin();
    }

    @Test
    public void notPerformLoginWithBadCredentials() throws Exception {
        givenAnExistingUser();

        theUserShouldNotBeAbleToLoginWithBadCredentials();
    }

    private void givenAnExistingUser() {
        marsDsl.create(
                aUser()
                        .withUsername(CREDENTIALS.getUsername())
                        .withPassword(bCryptPasswordEncoder.encode(CREDENTIALS.getPassword()))
                        .build()
        );
    }

    private void theUserShouldBeAbleToLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(jsonWith(CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    private void theUserShouldNotBeAbleToLoginWithBadCredentials() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(jsonWith(BAD_CREDENTIALS)))
                .andExpect(status().isUnauthorized());
    }
}
