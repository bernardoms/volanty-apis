package com.bernardoms.carapi.service;

import com.bernardoms.carapi.exception.CarNotFoundException;
import com.bernardoms.carapi.model.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {
    Car get(long carId) throws IOException, CarNotFoundException;
    List<Car> get() throws IOException;
}
