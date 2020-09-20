package com.bernardoms.cavapi.unit.repository;

import com.bernardoms.cavapi.model.CavAvailability;
import com.bernardoms.cavapi.repository.CavAvailabilityRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CavAvailabilityRepositoryImplTest {
    @Mock
    private Resource resource;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CavAvailabilityRepositoryImpl availabilityRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void should_return_availability() throws IOException {
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
        when(objectMapper.readValue(any(InputStream.class), eq(CavAvailability.class))).thenReturn(mapper.readValue(new File("src/test/resources/calendar.json"), CavAvailability.class));
        var cavAvailability = availabilityRepository.get();
        assertEquals("{\"2019-07-17\":{\"cav\":{\"Botafogo\":{\"visit\":{\"10\":{},\"11\":{\"car\":1},\"12\":{},\"13\":{},\"14\":{\"car\":7},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{},\"11\":{\"car\":7},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}},\"Norte Shopping\":{\"visit\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{},\"11\":{\"car\":2},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}},\"Barra da Tijuca\":{\"visit\":{\"10\":{},\"11\":{\"car\":3},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{\"car\":3},\"11\":{\"car\":4},\"12\":{\"car\":5},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}}}},\"2019-07-18\":{\"cav\":{\"Botafogo\":{\"visit\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}},\"Norte Shopping\":{\"visit\":{\"10\":{\"car\":2},\"11\":{},\"12\":{},\"13\":{},\"14\":{\"car\":2},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}},\"Barra da Tijuca\":{\"visit\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}},\"inspection\":{\"10\":{},\"11\":{},\"12\":{},\"13\":{},\"14\":{},\"15\":{},\"16\":{},\"17\":{}}}}}}", cavAvailability.getDate().toString());
    }
}
