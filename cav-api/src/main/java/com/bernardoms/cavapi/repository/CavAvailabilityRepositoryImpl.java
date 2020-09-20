package com.bernardoms.cavapi.repository;

import com.bernardoms.cavapi.model.CavAvailability;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class CavAvailabilityRepositoryImpl implements CavAvailabilityRepository {
    private final Resource calendarFile;
    private CavAvailability cavAvailability;

    private final ObjectMapper objectMapper;

    @Override
    public CavAvailability get() throws IOException {
        if(cavAvailability == null) {
            cavAvailability = objectMapper.readValue(calendarFile.getInputStream(), CavAvailability.class);
        }
        return cavAvailability;
    }

    @Override
    public void save(CavAvailability cavAvailability) {
        this.cavAvailability = cavAvailability;
    }
}
