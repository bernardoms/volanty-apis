package com.bernardoms.cavapi.repository;

import com.bernardoms.cavapi.model.Cav;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CavRepository {
    List<Cav> getAll() throws IOException;
    Optional<Cav> getById(long id) throws IOException;
}
