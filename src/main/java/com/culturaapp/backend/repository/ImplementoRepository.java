package com.culturaapp.backend.repository;

import com.culturaapp.backend.model.Implemento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImplementoRepository extends MongoRepository<Implemento, String> {

    // Spring Data genera la consulta automaticamente a partir del nombre del metodo
    List<Implemento> findByNombreContainingIgnoreCase(String nombre);
}
