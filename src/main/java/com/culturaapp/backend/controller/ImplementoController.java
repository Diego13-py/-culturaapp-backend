package com.culturaapp.backend.controller;

import com.culturaapp.backend.model.Implemento;
import com.culturaapp.backend.service.ImplementoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/implementos")
public class ImplementoController {

    private final ImplementoService implementoService;

    public ImplementoController(ImplementoService implementoService) {
        this.implementoService = implementoService;
    }

    // GET /api/implementos
    // GET /api/implementos?nombre=microfono  (busqueda por nombre)
    @GetMapping
    public List<Implemento> listar(@RequestParam(required = false) String nombre) {
        return implementoService.listar(nombre);
    }

    // GET /api/implementos/{id}
    @GetMapping("/{id}")
    public Implemento obtener(@PathVariable String id) {
        return implementoService.obtener(id);
    }

    // POST /api/implementos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Implemento crear(@RequestBody Implemento implemento) {
        return implementoService.crear(implemento);
    }

    // PUT /api/implementos/{id}
    @PutMapping("/{id}")
    public Implemento actualizar(@PathVariable String id, @RequestBody Implemento implemento) {
        return implementoService.actualizar(id, implemento);
    }

    // DELETE /api/implementos/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        implementoService.eliminar(id);
    }
}
