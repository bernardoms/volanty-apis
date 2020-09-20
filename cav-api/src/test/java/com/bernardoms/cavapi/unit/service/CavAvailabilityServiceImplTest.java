package com.bernardoms.cavapi.unit.service;

import com.bernardoms.cavapi.exception.CarNotOnRequestedCavException;
import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.exception.NoAvailabilitiesException;
import com.bernardoms.cavapi.model.Car;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;
import com.bernardoms.cavapi.repository.CavAvailabilityRepository;
import com.bernardoms.cavapi.service.CarService;
import com.bernardoms.cavapi.service.CavAvailabilityService;
import com.bernardoms.cavapi.service.CavAvailabilityServiceImpl;
import com.bernardoms.cavapi.service.CavService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CavAvailabilityServiceImplTest {
    @Mock
    private CavAvailabilityRepository cavAvailabilityRepository;
    @Mock
    private CavService cavService;
    @Mock
    private CarService carService;
    @InjectMocks
    private CavAvailabilityServiceImpl cavAvailabilityService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void should_return_availabilities_for_a_given_cav() throws IOException, CavNotFoundException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        var cavAvailability = mapper.readValue(new File("src/test/resources/calendar_for_cav1_availabilitys.json"), CavAvailability.class);
        when(cavService.getById(1L)).thenReturn(cav);
        when(cavAvailabilityRepository.get()).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        var allAvailabilityByCavId = cavAvailabilityService.getAllAvailabilityByCavId(1L);
        assertEquals(cavAvailability.getDate(), allAvailabilityByCavId.getDate());
    }

    @Test
    void should_post_availability_with_success() throws IOException, CavNotFoundException, CarNotOnRequestedCavException, NoAvailabilitiesException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        var car = Car.builder().model("Golf").cav("Botafogo").brand("VW").id(1L).build();
        var cavAvailabilityRequest = CavAvailabilityRequest.builder()
                .date(LocalDate.of(2019, 7, 17))
                .carId(1L)
                .hour("11")
                .build();
        when(cavAvailabilityRepository.get()).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        when(cavService.getById(1L)).thenReturn(cav);
        when(carService.getCar(1L)).thenReturn(car);
        cavAvailabilityService.postAvailability(cavAvailabilityRequest, 1, "visit");
        cavAvailabilityService.postAvailability(cavAvailabilityRequest, 1, "inspection");
    }

    @Test
    void should_throw_no_availabilities_date_exception() throws NoAvailabilitiesException, CarNotOnRequestedCavException, CavNotFoundException, IOException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        var cavAvailabilityRequest = CavAvailabilityRequest.builder()
                .date(LocalDate.of(2099, 7, 17))
                .carId(1L)
                .hour("11")
                .build();

        when(cavAvailabilityRepository.get()).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        when(cavService.getById(1L)).thenReturn(cav);

        var exception = assertThrows(NoAvailabilitiesException.class, () -> {
            cavAvailabilityService.postAvailability(cavAvailabilityRequest, 1, "visit");
        });

        assertEquals("no availability for date 2099-07-17", exception.getMessage());
    }

    @Test
    void should_throw_no_availabilities_hour_exception() throws NoAvailabilitiesException, CarNotOnRequestedCavException, CavNotFoundException, IOException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        var car = Car.builder().model("Golf").cav("Botafogo").brand("VW").id(1L).build();
        var cavAvailabilityRequest = CavAvailabilityRequest.builder()
                .date(LocalDate.of(2019, 7, 17))
                .carId(1L)
                .hour("21")

                .build();
        when(carService.getCar(1L)).thenReturn(car);
        when(cavAvailabilityRepository.get()).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        when(cavService.getById(1L)).thenReturn(cav);

        var exception = assertThrows(NoAvailabilitiesException.class, () -> {
            cavAvailabilityService.postAvailability(cavAvailabilityRequest, 1, "visit");
        });

        assertEquals("no availability for hour 21", exception.getMessage());
    }

    @Test
    void should_throw_car_not_on_requested_cav_exception() throws NoAvailabilitiesException, CarNotOnRequestedCavException, CavNotFoundException, IOException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        var car = Car.builder().model("Golf").cav("Barra Shopping").brand("VW").id(1L).build();
        var cavAvailabilityRequest = CavAvailabilityRequest.builder()
                .date(LocalDate.of(2019, 7, 17))
                .carId(1L)
                .hour("11")
                .build();

        when(cavAvailabilityRepository.get()).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        when(cavService.getById(1L)).thenReturn(cav);
        when(carService.getCar(1L)).thenReturn(car);

        var exception = assertThrows(CarNotOnRequestedCavException.class, () -> {
            cavAvailabilityService.postAvailability(cavAvailabilityRequest, 1, "visit");
        });

        assertEquals("car with id 1is not on the requested cav", exception.getMessage());
    }
}
