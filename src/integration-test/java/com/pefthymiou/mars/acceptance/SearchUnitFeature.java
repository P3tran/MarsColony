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
import java.util.List;

import static com.pefthymiou.mars.dsl.UnitDsl.*;
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
public class SearchUnitFeature {

    private static final ITUnit UNIT_1 = aUnit().withTitle("First").withRegion("One").build();
    private static final ITUnit UNIT_2 = aUnit().withTitle("Second").withRegion("Two").build();
    private static final ITUnit UNIT_3 = aUnit().withTitle("Third").withRegion("Three").build();
    private static final ITUnit UNIT_4 = aUnit().withTitle("Fourth").withRegion("Four").build();
    private static final ITUnit UNIT_5 = aUnit().withTitle("Fifth").withRegion("Five").build();
    private static final ITUnit UNIT_6 = aUnit().withTitle("Sixth").withRegion("Six").build();
    private static final ITUnit UNIT_7 = aUnit().withTitle("Seventh").withRegion("Seven").build();

    @Value("${api.basepath}")
    private String apiBasepath;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MarsDsl marsDsl;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).alwaysDo(print()).build();
    }

    @Test
    public void searchByTitleAsColonist() throws Exception {
        givenSevenUnits();

        shouldBeAbleToFindOneMatching("first");
    }

    @Test
    public void searchByRegionAsColonist() throws Exception {
        givenSevenUnits();

        shouldBeAbleToFindOneMatching("four");
    }

    @Test
    public void searchByTitleAndRegionAsColonist() throws Exception {
        givenSevenUnits();

        shouldBeAbleToFindTwoMatching("se");
    }

    private void givenSevenUnits() throws Exception {
        marsDsl.createAUnit(UNIT_1, mockMvc);
        marsDsl.createAUnit(UNIT_2, mockMvc);
        marsDsl.createAUnit(UNIT_3, mockMvc);
        marsDsl.createAUnit(UNIT_4, mockMvc);
        marsDsl.createAUnit(UNIT_5, mockMvc);
        marsDsl.createAUnit(UNIT_6, mockMvc);
        marsDsl.createAUnit(UNIT_7, mockMvc);
    }

    private void shouldBeAbleToFindOneMatching(String query) throws Exception {
        MvcResult response = mockMvc.perform(get(apiBasepath + "/units?q=" + query)
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        List<ITUnit> responseUnits = new ObjectMapper().readValue(response.getResponse().getContentAsString(), List.class);
        assertThat(responseUnits.size()).isEqualTo(1);
    }

    private void shouldBeAbleToFindTwoMatching(String query) throws Exception {
        MvcResult response = mockMvc.perform(get(apiBasepath + "/units?q=" + query)
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        List<ITUnit> responseUnits = new ObjectMapper().readValue(response.getResponse().getContentAsString(), List.class);
        assertThat(responseUnits.size()).isEqualTo(2);
    }
}
