package com.pefthymiou.mars.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.MarsDsl;
import com.pefthymiou.mars.dsl.UnitDsl;
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

import static com.pefthymiou.mars.dsl.UnitDsl.ITUnitBuilder.aUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MarsApplication.class, WebSecurityConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrieveUnitFeature {

    private static final UnitDsl.ITUnit UNIT = aUnit().build();

    @Value("${api.basepath}")
    private String apiBasepath;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MarsDsl marsDsl;

    private MockMvc mockMvc;
    private String location;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).alwaysDo(print()).build();
    }

    @Test
    public void retrieveAUnitAsColonist() throws Exception {
        givenAUnit();

        aColonistShouldBeAbleToRetrieveTheUnit();
    }

    private void givenAUnit() throws Exception {
        MvcResult response = marsDsl.createAUnit(UNIT, mockMvc);
        location = marsDsl.extractLocationFrom(response);
    }

    private void aColonistShouldBeAbleToRetrieveTheUnit() throws Exception {
        MvcResult response = mockMvc.perform(get(location)
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        UnitDsl.ITUnit responseUnit = new ObjectMapper().readValue(response.getResponse().getContentAsString(), UnitDsl.ITUnit.class);
        assertThat(responseUnit).isEqualToIgnoringGivenFields(UNIT, "id");
    }
}
