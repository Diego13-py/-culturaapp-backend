package com.culturaapp.backend.repository;

import com.culturaapp.backend.model.Prestamo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoRepository extends MongoRepository<Prestamo, String> {

    List<Prestamo> findByEstado(String estado);

    List<Prestamo> findByImplementoId(String implementoId);

    // Usado para detectar prestamos activos cuya fecha limite ya paso
    List<Prestamo> findByEstadoAndFechaLimiteBefore(String estado, LocalDateTime fecha);
}
