package com.bernardoms.cavapi.service;

import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.model.Cav;
import com.bernardoms.cavapi.repository.CavRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CavServiceImpl implements CavService {
    private final CavRepository cavRepository;

    @Override
    public List<Cav> getAll() throws IOException {
        return cavRepository.getAll();
    }

    @Override
    public Cav getById(long id) throws IOException, CavNotFoundException {
        return cavRepository.getById(id).orElseThrow(() -> new CavNotFoundException("cav with id " + id + " not found"));
    }
}
