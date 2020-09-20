package com.bernardoms.carapi.repository;

import com.bernardoms.carapi.model.Car;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Optional<Car> getById(long carId) throws IOException;
    List<Car> getAll() throws IOException;
}
