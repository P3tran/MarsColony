package com.pefthymiou.mars.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.pefthymiou.mars.MarsApplication;
import com.pefthymiou.mars.dsl.MarsDsl;
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
import java.util.Map;

import static com.pefthymiou.mars.dsl.Parser.jsonWith;
import static com.pefthymiou.mars.dsl.UnitDsl.ITUnitBuilder.aUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MarsApplication.class, WebSecurityConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateUnitFeature {

    private static final UnitDsl.ITUnit UNIT = aUnit().build();
    private static final Map<String, Object> UPDATED_FIELDS = ImmutableMap.of("region", "new region", "rating", 5);
    private static final Unit UPDATED_UNIT = new Unit(
            UNIT.getId(),
            UNIT.getTitle(),
            "new region",
            UNIT.getDescription(),
            UNIT.getCancellationPolicy(),
            UNIT.getPrice(),
            5,
            UNIT.getImageUrl(),
            UNIT.getTimezone()
    );

    @Value("${api.basepath}")
    private String apiBasepath;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MarsDsl marsDsl;

    private MockMvc mockMvc;
    private String id;
    private String location;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).alwaysDo(print()).build();
    }

    @Test
    public void updateAnExistingUnitAsAdmin() throws Exception {
        givenAUnit();

        whenAdminPatchesTheUnit();

        shouldBeAbleToGetTheUpdatedUnit();
    }

    @Test
    public void notAllowedToUpdateAnExistingUnitAsColonist() throws Exception {
        givenAUnit();

        colonistShouldNotBeAllowedToPatchTheUnit();
    }

    private void givenAUnit() throws Exception {
        MvcResult response = marsDsl.createAUnit(UNIT, mockMvc);
        id = marsDsl.extractIdFrom(response);
    }

    private void whenAdminPatchesTheUnit() throws Exception {
        MvcResult patchResponse = mockMvc.perform(patch(apiBasepath + "/unit/" + id)
                .header("Authorization", marsDsl.createAdminToken())
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(jsonWith(UPDATED_FIELDS)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"))
                .andReturn();

        location = patchResponse.getResponse().getHeader("Location");
    }

    private void shouldBeAbleToGetTheUpdatedUnit() throws Exception {
        MvcResult getResponse = mockMvc.perform(get(location)
                .header("Authorization", marsDsl.createAdminToken()))
                .andExpect(status().isOk())
                .andReturn();

        UnitDsl.ITUnit responseUnit = new ObjectMapper().readValue(getResponse.getResponse().getContentAsString(), UnitDsl.ITUnit.class);
        assertThat(responseUnit).isEqualToIgnoringGivenFields(UPDATED_UNIT, "id");
    }

    private void colonistShouldNotBeAllowedToPatchTheUnit() throws Exception {
        mockMvc.perform(patch(apiBasepath + "/unit/" + id)
                .header("Authorization", marsDsl.createColonistToken())
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(jsonWith(UPDATED_FIELDS)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
