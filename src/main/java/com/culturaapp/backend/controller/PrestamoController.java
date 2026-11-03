package com.culturaapp.backend.controller;

import com.culturaapp.backend.model.Prestamo;
import com.culturaapp.backend.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // GET /api/prestamos
    // GET /api/prestamos?estado=activo
    @GetMapping
    public List<Prestamo> listar(@RequestParam(required = false) String estado) {
        return prestamoService.listar(estado);
    }

    // POST /api/prestamos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prestamo crear(@RequestBody Prestamo prestamo) {
        return prestamoService.crear(prestamo);
    }

    // PATCH /api/prestamos/{id}/devolver
    @PatchMapping("/{id}/devolver")
    public Prestamo devolver(@PathVariable String id) {
        return prestamoService.devolver(id);
    }
}
