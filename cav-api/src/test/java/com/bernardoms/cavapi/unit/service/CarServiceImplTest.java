package com.bernardoms.cavapi.unit.service;

import com.bernardoms.cavapi.config.CarClientConfig;
import com.bernardoms.cavapi.model.Car;
import com.bernardoms.cavapi.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CarClientConfig carClientConfig;

    @Test
    void should_return_car() {
        var car = Car.builder().brand("VW").cav("Botafogo").id(1L).model("Golf").build();
        when(carClientConfig.getEndpoint()).thenReturn("http://test.com/v1");
        when(restTemplate.getForObject("http://test.com/v1/cars/1", Car.class)).thenReturn(car);
        var result = carService.getCar(1L);
        assertEquals(car.getCav(), result.getCav());
        assertEquals(car.getBrand(), result.getBrand());
        assertEquals(car.getModel(), result.getModel());
        assertEquals(car.getId(), result.getId());
    }
}
