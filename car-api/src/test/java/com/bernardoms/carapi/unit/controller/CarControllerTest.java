package com.bernardoms.carapi.unit.controller;

import com.bernardoms.carapi.controller.CarController;
import com.bernardoms.carapi.controller.ExceptionController;
import com.bernardoms.carapi.exception.CarNotFoundException;
import com.bernardoms.carapi.model.Car;
import com.bernardoms.carapi.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String URL_PATH = "/v1/cars";

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(carController)
                .setControllerAdvice(ExceptionController.class)
                .build();
    }

    @Test
    void should_return_ok_when_find_existing_car_by_id() throws Exception {
        var car = Car.builder().id(1)
                .brand("VW")
                .cav("Botafogo")
                .model("Golf")
                .build();

        when(carService.get(1)).thenReturn(car);

        mockMvc.perform(get(URL_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("brand", is("VW")))
                .andExpect(jsonPath("cav", is("Botafogo")))
                .andExpect(jsonPath("model", is("Golf")));
    }

    @Test
    void should_return_not_found_when_car_id_dont_exist() throws Exception {
        when(carService.get(2)).thenThrow(new CarNotFoundException("Car with id 2 not found!"));
        mockMvc.perform(get(URL_PATH + "/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_ok_with_all_cars() throws Exception {
        var car1 =   Car.builder().id(1)
                .brand("VW")
                .cav("Botafogo")
                .model("Golf")
                .build();

        var car2 =   Car.builder().id(2)
                .brand("GM")
                .cav("Botafogo")
                .model("Cruze")
                .build();


        when(carService.get()).thenReturn(Arrays.asList(car1, car2));
        mockMvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(1)))
                .andExpect(jsonPath("[0].brand", is("VW")))
                .andExpect(jsonPath("[0].cav", is("Botafogo")))
                .andExpect(jsonPath("[0].model", is("Golf")))
                .andExpect(jsonPath("[1].id", is(2)))
                .andExpect(jsonPath("[1].brand", is("GM")))
                .andExpect(jsonPath("[1].cav", is("Botafogo")))
                .andExpect(jsonPath("[1].model", is("Cruze")));
    }

    @Test
    void should_return_ok_with_no_cars_with_the_database_is_empty() throws Exception {
        mockMvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").doesNotExist());
    }
}
