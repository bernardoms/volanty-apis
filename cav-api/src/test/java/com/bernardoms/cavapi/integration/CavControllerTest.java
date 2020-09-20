package com.bernardoms.cavapi.integration;

import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CavControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URL_PATH = "/v1/cav";

    @Test
    void should_return_ok_with_list_of_cav() throws Exception {
        mockMvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(1)))
                .andExpect(jsonPath("[0].name", is("Botafogo")))
                .andExpect(jsonPath("[1].id", is(2)))
                .andExpect(jsonPath("[1].name", is("Barra da Tijuca")));
    }

    @Test
    void should_return_ok_with_cav_availability() throws Exception {
        mockMvc.perform(get(URL_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("date.2019-07-17.cav.Botafogo").exists());
    }

    @Test
    void should_return_ok_when_register_a_visit_for_visit() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().carId(
                1L).hour("11").date(LocalDate.of(2019,7,17)).build();
        mockMvc.perform(post(URL_PATH + "/1/visit").content(mapper.writeValueAsString(cavAvailabilityRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void should_return_ok_when_register_a_visit_for_inspection() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().carId(1L).hour("11").date(LocalDate.of(2019,7,17)).build();
        mockMvc.perform(post(URL_PATH + "/1/inspection").content(mapper.writeValueAsString(cavAvailabilityRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_bad_request_when_missing_required_params_for_register_a_visit() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().build();
        mockMvc.perform(post(URL_PATH + "/1/visit").content(mapper.writeValueAsString(cavAvailabilityRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_request_when_missing_required_params_for_register_a_inspection() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().build();
        mockMvc.perform(post(URL_PATH + "/1/inspection").content(mapper.writeValueAsString(cavAvailabilityRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
