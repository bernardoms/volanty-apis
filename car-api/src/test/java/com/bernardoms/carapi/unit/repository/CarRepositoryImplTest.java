package com.bernardoms.carapi.unit.repository;

import com.bernardoms.carapi.model.Car;
import com.bernardoms.carapi.repository.CarRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarRepositoryImplTest {
    @InjectMocks
    private CarRepositoryImpl carRepository;

    @Mock
    private Resource resource;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void should_return_optional_of_car() throws IOException {
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

        Car[] cars = new Car[2];
        cars[0] = car1;
        cars[1] = car2;

        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
        when(objectMapper.readValue(any(InputStream.class), eq(Car[].class))).thenReturn(cars);

        var optionalCar = carRepository.getById(1);

        assertEquals(Optional.of(car1), optionalCar);
    }

    @Test
    void should_return_all_cars() throws IOException {
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

        var cars = new Car[2];
        cars[0] = car1;
        cars[1] = car2;

        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
        when(objectMapper.readValue(any(InputStream.class), eq(Car[].class))).thenReturn(cars);

        var result = carRepository.getAll();

        assertEquals(1, result.get(0).getId());
        assertEquals("VW", result.get(0).getBrand());
        assertEquals("Botafogo", result.get(0).getCav());
        assertEquals("Golf", result.get(0).getModel());
        assertEquals(2, result.get(1).getId());
        assertEquals("GM", result.get(1).getBrand());
        assertEquals("Botafogo", result.get(1).getCav());
        assertEquals("Cruze", result.get(1).getModel());

    }
}
