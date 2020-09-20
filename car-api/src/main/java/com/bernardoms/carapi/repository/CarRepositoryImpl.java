package com.bernardoms.carapi.repository;

import com.bernardoms.carapi.model.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {
    private final Resource resourceFile;

    private final ObjectMapper objectMapper;

    @Override
    @Cacheable(cacheNames = "car")
    public Optional<Car> getById(long carId) throws IOException {
        var cars = objectMapper.readValue(resourceFile.getInputStream(), Car[].class);
        return Arrays.stream(cars)
                .filter(c -> c.getId() == carId)
                .findFirst();
    }

    @Override
    @Cacheable(cacheNames = "cars")
    public List<Car> getAll() throws IOException {
        var cars = objectMapper.readValue(resourceFile.getInputStream(), Car[].class);
        return Arrays.stream(cars).collect(Collectors.toList());
    }
}
