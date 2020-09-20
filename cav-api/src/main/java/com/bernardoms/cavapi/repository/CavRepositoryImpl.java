package com.bernardoms.cavapi.repository;

import com.bernardoms.cavapi.model.Cav;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CavRepositoryImpl implements CavRepository {
    private final Resource cavFile;
    private final ObjectMapper objectMapper;

    @Override
    public List<Cav> getAll() throws IOException {
        return Arrays.stream(objectMapper.readValue(cavFile.getInputStream(), Cav[].class)).collect(Collectors.toList());
    }

    @Override
    public Optional<Cav> getById(long id) throws IOException {
        return Arrays.stream(objectMapper.readValue(cavFile.getInputStream(), Cav[].class))
                .filter(c -> c.getId() == id).findFirst();
    }
}
