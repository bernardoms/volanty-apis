package com.bernardoms.carapi.controller;

import com.bernardoms.carapi.exception.CarNotFoundException;
import com.bernardoms.carapi.model.Car;
import com.bernardoms.carapi.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("{carId}")
    @ResponseStatus(HttpStatus.OK)
    public Car getCar(@PathVariable long carId) throws IOException, CarNotFoundException {
        return carService.get(carId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getCars() throws IOException {
        return carService.get();
    }
}
