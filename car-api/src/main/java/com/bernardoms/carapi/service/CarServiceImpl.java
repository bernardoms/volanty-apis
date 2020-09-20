package com.bernardoms.carapi.service;

import com.bernardoms.carapi.exception.CarNotFoundException;
import com.bernardoms.carapi.model.Car;
import com.bernardoms.carapi.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car get(long carId) throws IOException, CarNotFoundException {
        return carRepository.getById(carId).orElseThrow(() -> new CarNotFoundException("Car with id " + carId + " not found!"));
    }

    @Override
    public List<Car> get() throws IOException {
        return carRepository.getAll();
    }
}
