package com.pefthymiou.mars.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.MarsDsl;
import com.pefthymiou.mars.dsl.MarsPage;
import com.pefthymiou.mars.dsl.UnitDsl;
import com.pefthymiou.mars.security.configuration.WebSecurityConfiguration;
import com.pefthymiou.mars.unit.infrastructure.db.Unit;
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
public class BrowseUnitFeature {

    @Value("${api.basepath}")
    private String apiBasepath;

    private static final UnitDsl.ITUnit UNIT_1 = aUnit().withTitle("First").build();
    private static final UnitDsl.ITUnit UNIT_2 = aUnit().withTitle("Second").build();
    private static final UnitDsl.ITUnit UNIT_3 = aUnit().withTitle("Third").build();
    private static final UnitDsl.ITUnit UNIT_4 = aUnit().withTitle("Fourth").build();
    private static final UnitDsl.ITUnit UNIT_5 = aUnit().withTitle("Fifth").build();

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
    public void getSubsequentUnitPagesAsColonist() throws Exception {
        givenFiveUnits();

        firstPageShouldContainTwoUnits();

        secondPageShouldContainTwoUnits();

        thirdPageShouldContainThreeUnits();
    }

    private void givenFiveUnits() throws Exception {
        marsDsl.createAUnit(UNIT_1, mockMvc);
        marsDsl.createAUnit(UNIT_2, mockMvc);
        marsDsl.createAUnit(UNIT_3, mockMvc);
        marsDsl.createAUnit(UNIT_4, mockMvc);
        marsDsl.createAUnit(UNIT_5, mockMvc);
    }

    private void firstPageShouldContainTwoUnits() throws Exception {
        MvcResult firstPageResponse = mockMvc.perform(get(apiBasepath + "/units/browse?page=0&size=2")
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        MarsPage<Unit> firstPage = new ObjectMapper().readValue(firstPageResponse.getResponse().getContentAsString(), MarsPage.class);
        assertThat(firstPage.getContent().size()).isEqualTo(2);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);
        assertThat(firstPage.getTotalElements()).isEqualTo(5);
        assertThat(firstPage.isLast()).isFalse();
        assertThat(firstPage.isFirst()).isTrue();
        assertThat(firstPage.getNumber()).isEqualTo(0);
    }

    private void secondPageShouldContainTwoUnits() throws Exception {
        MvcResult secondPageResponse = mockMvc.perform(get(apiBasepath + "/units/browse?page=1&size=2")
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        MarsPage<Unit> secondPage = new ObjectMapper().readValue(secondPageResponse.getResponse().getContentAsString(), MarsPage.class);
        assertThat(secondPage.getContent().size()).isEqualTo(2);
        assertThat(secondPage.isLast()).isFalse();
        assertThat(secondPage.isFirst()).isFalse();
        assertThat(secondPage.getNumber()).isEqualTo(1);
    }

    private void thirdPageShouldContainThreeUnits() throws Exception {
        MvcResult thirdPageResponse = mockMvc.perform(get(apiBasepath + "/units/browse?page=2&size=2")
                .header("Authorization", marsDsl.createColonistToken()))
                .andExpect(status().isOk())
                .andReturn();

        MarsPage<Unit> thirdPage = new ObjectMapper().readValue(thirdPageResponse.getResponse().getContentAsString(), MarsPage.class);
        assertThat(thirdPage.getContent().size()).isEqualTo(1);
        assertThat(thirdPage.isLast()).isTrue();
        assertThat(thirdPage.isFirst()).isFalse();
        assertThat(thirdPage.getNumber()).isEqualTo(2);
    }
}
