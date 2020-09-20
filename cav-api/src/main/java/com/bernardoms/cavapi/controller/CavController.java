package com.bernardoms.cavapi.controller;

import com.bernardoms.cavapi.exception.CarNotOnRequestedCavException;
import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.exception.NoAvailabilitiesException;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;
import com.bernardoms.cavapi.service.CarService;
import com.bernardoms.cavapi.service.CavAvailabilityService;
import com.bernardoms.cavapi.service.CavService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/cav")
@RequiredArgsConstructor
public class CavController {
    private final CavAvailabilityService cavAvailabilityService;
    private final CavService cavService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cav> getCavs() throws IOException {
       return cavService.getAll();
    }

    @GetMapping("/{cavId}")
    @ResponseStatus(HttpStatus.OK)
    public CavAvailability getCavs(@PathVariable long cavId) throws IOException, CavNotFoundException {
        return cavAvailabilityService.getAllAvailabilityByCavId(cavId);
    }

    @PostMapping("/{cavId}/visit")
    @ResponseStatus(HttpStatus.OK)
    public void postAvailabilityVisit(@PathVariable long cavId, @RequestBody @Validated CavAvailabilityRequest cavAvailabilityRequest) throws IOException, NoAvailabilitiesException, CavNotFoundException, CarNotOnRequestedCavException {
        cavAvailabilityService.postAvailability(cavAvailabilityRequest,cavId, "visit");
    }

    @PostMapping("/{cavId}/inspection")
    @ResponseStatus(HttpStatus.OK)
    public void postAvailabilityInspection(@PathVariable long cavId, @RequestBody @Validated CavAvailabilityRequest cavAvailabilityRequest) throws IOException, NoAvailabilitiesException, CavNotFoundException, CarNotOnRequestedCavException {
        cavAvailabilityService.postAvailability(cavAvailabilityRequest,cavId, "inspection");
    }
}
