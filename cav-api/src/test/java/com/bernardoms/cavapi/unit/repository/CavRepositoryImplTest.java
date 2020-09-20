package com.bernardoms.cavapi.unit.repository;

import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.repository.CavRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CavRepositoryImplTest {
    @Mock
    private ObjectMapper mapper;
    @Mock
    private Resource resource;
    @InjectMocks
    private CavRepositoryImpl cavRepository;

    @Test
    void should_return_all_cavs() throws IOException {
        var cav1 =   Cav.builder().id(1)
                .name("Botafogo")
                .build();

        var cav2 =   Cav.builder().id(2)
                .name("Barra Shopping")
                .build();
        Cav[] cavs = new Cav[2];
        cavs[0] = cav1;
        cavs[1] = cav2;

        when(mapper.readValue(any(InputStream.class), eq(Cav[].class))).thenReturn(cavs);
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
        var result = cavRepository.getAll();
        assertEquals(cav1.getName(), result.get(0).getName());
        assertEquals(cav1.getId(), result.get(0).getId());
        assertEquals(cav2.getName(), result.get(1).getName());
        assertEquals(cav2.getId(), result.get(1).getId());
    }

    @Test
    void should_return_a_cav_by_id() throws IOException {
        var cav1 =   Cav.builder().id(1)
                .name("Botafogo")
                .build();

        var cav2 =   Cav.builder().id(2)
                .name("Barra Shopping")
                .build();
        Cav[] cavs = new Cav[2];
        cavs[0] = cav1;
        cavs[1] = cav2;

        when(mapper.readValue(any(InputStream.class), eq(Cav[].class))).thenReturn(cavs);
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));

        var cav = cavRepository.getById(1);
        assertTrue(cav.isPresent());
    }
}
