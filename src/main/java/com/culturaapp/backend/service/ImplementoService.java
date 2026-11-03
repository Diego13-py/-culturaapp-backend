package com.culturaapp.backend.service;

import com.culturaapp.backend.exception.ApiException;
import com.culturaapp.backend.model.Implemento;
import com.culturaapp.backend.repository.ImplementoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImplementoService {

    private final ImplementoRepository implementoRepository;

    public ImplementoService(ImplementoRepository implementoRepository) {
        this.implementoRepository = implementoRepository;
    }

    public List<Implemento> listar(String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return implementoRepository.findByNombreContainingIgnoreCase(nombre);
        }
        return implementoRepository.findAll();
    }

    public Implemento obtener(String id) {
        return implementoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Implemento no encontrado"));
    }

    public Implemento crear(Implemento implemento) {
        if (implemento.getNombre() == null || implemento.getNombre().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El nombre del implemento es obligatorio");
        }
        if (implemento.getTipo() == null || implemento.getTipo().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El tipo del implemento es obligatorio");
        }
        // Un implemento siempre se crea disponible
        implemento.setEstado("DISPONIBLE");

        // Autogenerar codigo si no lo enviaron, ej. "IMP-023"
        if (implemento.getCodigo() == null || implemento.getCodigo().isBlank()) {
            long siguiente = implementoRepository.count() + 1;
            implemento.setCodigo(String.format("IMP-%03d", siguiente));
        }

        // Ubicacion y condicion por defecto, igual que en la maqueta de Figma
        if (implemento.getUbicacion() == null || implemento.getUbicacion().isBlank()) {
            implemento.setUbicacion("Área de Cultura");
        }
        if (implemento.getCondicion() == null || implemento.getCondicion().isBlank()) {
            implemento.setCondicion("Bueno");
        }

        return implementoRepository.save(implemento);
    }

    public Implemento actualizar(String id, Implemento datos) {
        Implemento existente = obtener(id);

        if (datos.getNombre() != null && !datos.getNombre().isBlank()) {
            existente.setNombre(datos.getNombre());
        }
        if (datos.getTipo() != null && !datos.getTipo().isBlank()) {
            existente.setTipo(datos.getTipo());
        }
        if (datos.getDescripcion() != null) {
            existente.setDescripcion(datos.getDescripcion());
        }
        if (datos.getUbicacion() != null && !datos.getUbicacion().isBlank()) {
            existente.setUbicacion(datos.getUbicacion());
        }
        if (datos.getCondicion() != null && !datos.getCondicion().isBlank()) {
            existente.setCondicion(datos.getCondicion());
        }
        // El codigo y el estado NO se actualizan aqui: el codigo es fijo desde
        // la creacion y el estado solo lo cambia la logica de prestamos/devoluciones

        return implementoRepository.save(existente);
    }

    public void eliminar(String id) {
        Implemento existente = obtener(id);
        implementoRepository.delete(existente);
    }

    /**
     * Usado internamente por PrestamoService para mover el implemento
     * entre los estados DISPONIBLE y PRESTADO.
     */
    public void cambiarEstado(String id, String nuevoEstado) {
        Implemento implemento = obtener(id);
        implemento.setEstado(nuevoEstado);
        implementoRepository.save(implemento);
    }
}
