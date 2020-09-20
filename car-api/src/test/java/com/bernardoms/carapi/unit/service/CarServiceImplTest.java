package com.bernardoms.carapi.unit.service;

import com.bernardoms.carapi.exception.CarNotFoundException;
import com.bernardoms.carapi.model.Car;
import com.bernardoms.carapi.repository.CarRepository;
import com.bernardoms.carapi.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void should_return_car_when_find_existing_car_by_id() throws IOException, CarNotFoundException {
        var car = Car.builder().id(1)
                .brand("VW")
                .cav("Botafogo")
                .model("Golf")
                .build();

        when(carRepository.getById(1)).thenReturn(Optional.of(car));

        var result = carService.get(1);

        assertEquals(1, result.getId());
        assertEquals("Golf", result.getModel());
        assertEquals("Botafogo", result.getCav());
        assertEquals("VW", result.getBrand());
    }

    @Test
    void should_return_car_not_found_when_id_not_exist() throws IOException, CarNotFoundException {
        when(carRepository.getById(1)).thenReturn(Optional.empty());

        var exception = assertThrows(CarNotFoundException.class, () -> {
            carService.get(1);
        });

        assertEquals("Car with id 1 not found!", exception.getMessage());
    }

    @Test
    void should_return_all_cars() throws IOException {
        var car1 = Car.builder().id(1)
                .brand("VW")
                .cav("Botafogo")
                .model("Golf")
                .build();

        var car2 = Car.builder().id(2)
                .brand("GM")
                .cav("Botafogo")
                .model("Cruze")
                .build();

        when(carRepository.getAll()).thenReturn(Arrays.asList(car1, car2));

        var cars = carService.get();

        assertEquals(1, cars.get(0).getId());
        assertEquals("VW", cars.get(0).getBrand());
        assertEquals("Botafogo", cars.get(0).getCav());
        assertEquals("Golf", cars.get(0).getModel());
        assertEquals(2, cars.get(1).getId());
        assertEquals("GM", cars.get(1).getBrand());
        assertEquals("Botafogo", cars.get(1).getCav());
        assertEquals("Cruze", cars.get(1).getModel());
    }
}
