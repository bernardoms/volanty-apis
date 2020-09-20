package com.bernardoms.cavapi.service;

import com.bernardoms.cavapi.exception.CavNotFoundException;
import com.bernardoms.cavapi.model.Cav;

import java.io.IOException;
import java.util.List;

public interface CavService {
    List<Cav> getAll() throws IOException;
    Cav getById(long id) throws IOException, CavNotFoundException;
}
