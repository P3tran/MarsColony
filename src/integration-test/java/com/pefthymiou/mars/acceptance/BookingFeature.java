package com.pefthymiou.mars.acceptance;

import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.BookingDsl;
import com.pefthymiou.mars.dsl.MarsDsl;
import com.pefthymiou.mars.dsl.UnitDsl;
import com.pefthymiou.mars.dsl.UserDsl;
import com.pefthymiou.mars.security.configuration.WebSecurityConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;


import static com.pefthymiou.mars.dsl.BookingDsl.ITBookingBuilder.aBooking;
import static com.pefthymiou.mars.dsl.Parser.jsonWith;
import static com.pefthymiou.mars.dsl.UnitDsl.ITUnitBuilder.aUnit;
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
public class BookingFeature {

    private static final UnitDsl.ITUnit UNIT = aUnit().build();
    private static final UserDsl.ITUser USER = aUser().build();

    @Value("${api.basepath}")
    private String apiBasepath;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MarsDsl marsDsl;

    private MockMvc mockMvc;
    private int id;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).alwaysDo(print()).build();
    }

    @Test
    public void booksAUnitAsColonist() throws Exception {
        givenAUserAndAUnit();

        aColonistShouldBeAbleToBookTheUnit();
    }

    private void givenAUserAndAUnit() throws Exception {
        marsDsl.create(USER);
        MvcResult response = marsDsl.createAUnit(UNIT, mockMvc);
        id = Integer.parseInt(marsDsl.extractIdFrom(response));
    }

    private void aColonistShouldBeAbleToBookTheUnit() throws Exception {
        BookingDsl.ITBooking booking = aBooking()
                .withUserId(USER.getId())
                .withUnitId(id)
                .withCheckIn("2020-05-05")
                .withCheckOut("2020-05-07")
                .build();

        mockMvc.perform(post(apiBasepath + "/booking")
                .header("Authorization", marsDsl.createColonistToken())
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(jsonWith(booking)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
