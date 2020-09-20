package com.bernardoms.cavapi.unit.controller;

import com.bernardoms.cavapi.controller.CavController;
import com.bernardoms.cavapi.controller.ExceptionController;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;
import com.bernardoms.cavapi.service.CarService;
import com.bernardoms.cavapi.service.CavAvailabilityService;
import com.bernardoms.cavapi.service.CavService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class CavControllerTest {
    @Mock
    private CavService cavService;
    @Mock
    private CavAvailabilityService cavAvailabilityService;
    private static final String URL_PATH = "/v1/cav";
    private MockMvc mockMvc;
    @InjectMocks
    private CavController cavController;
    @Mock
    private CarService carService;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mockMvc = standaloneSetup(cavController)
                .setControllerAdvice(ExceptionController.class)
                .build();
    }

    @Test
    void should_return_ok_with_list_of_cav() throws Exception {
        var cav1 = Cav.builder().id(1).name("Botafogo").build();
        var cav2 = Cav.builder().id(2).name("Norte Shopping").build();
        when(cavService.getAll()).thenReturn(Arrays.asList(cav1, cav2));

        mockMvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(1)))
                .andExpect(jsonPath("[0].name", is("Botafogo")))
                .andExpect(jsonPath("[1].id", is(2)))
                .andExpect(jsonPath("[1].name", is("Norte Shopping")));
    }

    @Test
    void should_return_ok_with_cav_availability() throws Exception {
        var cavAvailability = mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class);
        when(cavAvailabilityService.getAllAvailabilityByCavId(1)).thenReturn(cavAvailability);
        mockMvc.perform(get(URL_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("date.2019-07-17.cav.Botafogo").exists());
    }

    @Test
    void should_return_ok_when_register_a_visit_for_visit() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().carId(
                1L).hour("10").date(LocalDate.of(2020,10,10)).build();
        mockMvc.perform(post(URL_PATH + "/1/visit").content(mapper.writeValueAsString(cavAvailabilityRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_ok_when_register_a_visit_for_inspection() throws Exception {
        var cavAvailabilityRequest = CavAvailabilityRequest.builder().carId(1L).hour("10").date(LocalDate.of(2020,10,10)).build();
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
