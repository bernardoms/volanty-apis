package com.bernardoms.cavapi.service;

import com.bernardoms.cavapi.config.CarClientConfig;
import com.bernardoms.cavapi.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final RestTemplate restTemplate;
    private final CarClientConfig carClientConfig;

    @Override
    public Car getCar(Long carId) {
        return restTemplate.getForObject(carClientConfig.getEndpoint() + "/cars/" + carId, Car.class);
    }
}
