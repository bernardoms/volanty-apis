package com.bernardoms.cavapi.unit.service;

import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.repository.CavRepository;
import com.bernardoms.cavapi.service.CavServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CavServiceImplTest {
    @Mock
    private CavRepository cavRepository;
    @InjectMocks
    private CavServiceImpl cavService;

    @Test
    void should_return_all_cavs() throws IOException {
        var cav1 = Cav.builder().name("Botafogo").id(1L).build();
        var cav2 = Cav.builder().name("Barra Shopping").id(2L).build();
        when(cavRepository.getAll()).thenReturn(Arrays.asList(cav1, cav2));
        var cavs = cavService.getAll();
        assertEquals(cav1.getName(), cavs.get(0).getName());
        assertEquals(cav1.getId(), cavs.get(0).getId());
        assertEquals(cav2.getName(), cavs.get(1).getName());
        assertEquals(cav2.getId(), cavs.get(1).getId());
    }

    @Test
    void should_return_cav_if_id_exist() throws IOException, CavNotFoundException {
        var cav = Cav.builder().name("Botafogo").id(1L).build();
        when(cavRepository.getById(1L)).thenReturn(Optional.of(cav));
        var result = cavService.getById(1L);
        assertEquals(cav.getId(), result.getId());
        assertEquals(cav.getName(), result.getName());
    }

    @Test
    void should_return_cav_not_found_exception_when_cav_id_not_exists() throws IOException {
        when(cavRepository.getById(1L)).thenReturn(Optional.empty());
        var exception = assertThrows(CavNotFoundException.class, () -> {
            cavService.getById(1);
        });
        assertEquals("cav with id 1 not found", exception.getMessage());
    }
}
