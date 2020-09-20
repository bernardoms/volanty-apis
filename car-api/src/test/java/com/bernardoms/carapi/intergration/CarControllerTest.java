package com.bernardoms.carapi.intergration;

import com.bernardoms.carapi.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String URL_PATH = "/v1/cars";

    @Test
    void should_return_ok_when_find_existing_car_by_id() throws Exception {
        var car = Car.builder().id(1)
                .brand("VW")
                .cav("Botafogo")
                .model("Golf")
                .build();

        mockMvc.perform(get(URL_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("brand", is("VW")))
                .andExpect(jsonPath("cav", is("Botafogo")))
                .andExpect(jsonPath("model", is("Golf")));
    }

    @Test
    void should_return_not_found_when_car_id_dont_exist() throws Exception {
        mockMvc.perform(get(URL_PATH + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_ok_with_all_cars() throws Exception {
        mockMvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
