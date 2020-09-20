package com.bernardoms.carapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private long id;
    private String brand;
    private String model;
    private String cav;
}
