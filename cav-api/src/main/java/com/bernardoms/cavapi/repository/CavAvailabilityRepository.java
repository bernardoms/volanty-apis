package com.bernardoms.cavapi.repository;

import com.bernardoms.cavapi.model.CavAvailability;

import java.io.IOException;

public interface CavAvailabilityRepository {
    CavAvailability get() throws IOException;
    void save(CavAvailability cavAvailability) throws IOException;
}
