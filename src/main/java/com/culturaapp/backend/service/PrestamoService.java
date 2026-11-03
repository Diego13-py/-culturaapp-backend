package com.culturaapp.backend.service;

import com.culturaapp.backend.exception.ApiException;
import com.culturaapp.backend.model.Implemento;
import com.culturaapp.backend.model.Prestamo;
import com.culturaapp.backend.repository.PrestamoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrestamoService {

    // Si no se indica fecha limite al crear el prestamo, se asumen 3 dias
    private static final long DIAS_PRESTAMO_POR_DEFECTO = 3;

    private final PrestamoRepository prestamoRepository;
    private final ImplementoService implementoService;

    public PrestamoService(PrestamoRepository prestamoRepository, ImplementoService implementoService) {
        this.prestamoRepository = prestamoRepository;
        this.implementoService = implementoService;
    }

    public List<Prestamo> listar(String estado) {
        actualizarVencidos();
        if (estado != null && !estado.isBlank()) {
            return prestamoRepository.findByEstado(estado.toUpperCase());
        }
        return prestamoRepository.findAll();
    }

    /**
     * Revisa los prestamos ACTIVOS cuya fecha limite ya paso y los marca
     * como VENCIDO. Se ejecuta cada vez que se listan prestamos, asi el
     * estado mostrado al cliente movil siempre esta actualizado (tal como
     * se ve en la pantalla "Préstamos del encargado" de la maqueta).
     */
    private void actualizarVencidos() {
        List<Prestamo> atrasados = prestamoRepository
                .findByEstadoAndFechaLimiteBefore("ACTIVO", LocalDateTime.now());
        for (Prestamo prestamo : atrasados) {
            prestamo.setEstado("VENCIDO");
            prestamoRepository.save(prestamo);
        }
    }

    /**
     * Regla de negocio principal del proyecto: no se puede prestar un
     * implemento que ya se encuentre prestado.
     */
    public Prestamo crear(Prestamo datos) {
        if (datos.getImplementoId() == null || datos.getImplementoId().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El id del implemento es obligatorio");
        }
        if (datos.getResponsable() == null || datos.getResponsable().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El responsable es obligatorio");
        }

        // Si el implemento no existe, obtener() lanza 404 automaticamente
        Implemento implemento = implementoService.obtener(datos.getImplementoId());

        if ("PRESTADO".equals(implemento.getEstado())) {
            throw new ApiException(HttpStatus.CONFLICT, "El implemento ya se encuentra prestado");
        }

        // Si no envian fecha limite, se calcula automaticamente (hoy + 3 dias)
        LocalDateTime fechaLimite = datos.getFechaLimite() != null
                ? datos.getFechaLimite()
                : LocalDateTime.now().plusDays(DIAS_PRESTAMO_POR_DEFECTO);

        Prestamo prestamo = new Prestamo(datos.getImplementoId(), datos.getResponsable(), fechaLimite);
        implementoService.cambiarEstado(implemento.getId(), "PRESTADO");

        return prestamoRepository.save(prestamo);
    }

    public Prestamo devolver(String id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Prestamo no encontrado"));

        // Un prestamo VENCIDO tambien se puede devolver con normalidad;
        // solo uno ya DEVUELTO no se puede volver a devolver.
        if ("DEVUELTO".equals(prestamo.getEstado())) {
            throw new ApiException(HttpStatus.CONFLICT, "Este prestamo ya fue devuelto anteriormente");
        }

        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucion(LocalDateTime.now());
        implementoService.cambiarEstado(prestamo.getImplementoId(), "DISPONIBLE");

        return prestamoRepository.save(prestamo);
    }
}
