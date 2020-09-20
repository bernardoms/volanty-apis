package com.bernardoms.cavapi.service;

import com.bernardoms.cavapi.exception.CarNotOnRequestedCavException;
import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.exception.NoAvailabilitiesException;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.model.CavAvailabilityRequest;
import com.bernardoms.cavapi.repository.CavAvailabilityRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CavAvailabilityServiceImpl implements CavAvailabilityService {
    private final CavAvailabilityRepository cavAvailabilityRepository;
    private final CavService cavService;
    private final CarService carService;

    @Override
    public CavAvailability getAllAvailabilityByCavId(long cavId) throws IOException, CavNotFoundException {
        var cav = cavService.getById(cavId);
        var cavAvailability = cavAvailabilityRepository.get();
        var elements = cavAvailability.getDate().elements();

        while (elements.hasNext()) {
            JsonNode date = elements.next();
            ObjectNode locations = (ObjectNode) date.get("cav");
            locations = locations.retain(cav.getName());

            removeNotAvailableProcedures(locations, "visit", cav.getName());
            removeNotAvailableProcedures(locations, "inspection", cav.getName());
        }
        return cavAvailability;
    }

    public void postAvailability(CavAvailabilityRequest cavAvailabilityRequest, long cavId, String type) throws IOException, NoAvailabilitiesException, CavNotFoundException, CarNotOnRequestedCavException {
        var cavAvailability = cavAvailabilityRepository.get();
        var cav = cavService.getById(cavId);
        var date = cavAvailabilityRequest.getDate().format(DateTimeFormatter.ISO_DATE);
        var hour = cavAvailabilityRequest.getHour();
        if (cavAvailability.getDate().get(date) == null) {
            throw new NoAvailabilitiesException("no availability for date " + date);
        }
        if (!isCarOnSameRequestedCavAvailability(cavAvailabilityRequest, cav)) {
            throw new CarNotOnRequestedCavException("car with id " + cavAvailabilityRequest.getCarId() + "is not on the requested cav");
        }
        var elements = (ObjectNode) cavAvailability.getDate().get(date).get("cav").get(cav.getName()).path(type);
        JsonNode newNode;
        if (elements.get(hour) == null || elements.get(hour).get("car") == null) {
            throw new NoAvailabilitiesException("no availability for hour " + hour);
        } else if (elements.get(hour).get("car").intValue() - 1 == 0) {
            newNode = JsonNodeFactory.instance.objectNode();
        } else {
            newNode = new ObjectNode(JsonNodeFactory.instance, Map.of("car", new IntNode(elements.get(hour).get("car").intValue() - 1)));
        }
        elements.set(hour, newNode);
        cavAvailabilityRepository.save(cavAvailability);
    }

    private boolean isCarOnSameRequestedCavAvailability(CavAvailabilityRequest cavAvailabilityRequest, Cav cav) {
        var car = carService.getCar(cavAvailabilityRequest.getCarId());
        return cav.getName().equals(car.getCav());
    }

    private void removeNotAvailableProcedures(ObjectNode node, String type, String location) {
        Iterator<JsonNode> visitElement = node.get(location).get(type).elements();
        while (visitElement.hasNext()) {
            JsonNode visit = visitElement.next();
            JsonNode car = visit.get("car");
            if (car == null) {
                visitElement.remove();
            }
        }
    }
}
