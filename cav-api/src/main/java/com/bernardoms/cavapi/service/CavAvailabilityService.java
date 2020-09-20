package com.bernardoms.cavapi.service;

import com.bernardoms.cavapi.exception.CarNotOnRequestedCavException;
import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.exception.NoAvailabilitiesException;
import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;

import java.io.IOException;

public interface CavAvailabilityService {
    CavAvailability getAllAvailabilityByCavId(long cavId) throws IOException, CavNotFoundException;
    void postAvailability(CavAvailabilityRequest cavAvailabilityRequest, long cavId, String type) throws IOException, NoAvailabilitiesException, CavNotFoundException, CarNotOnRequestedCavException;
}
