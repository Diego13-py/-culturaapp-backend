package com.culturaapp.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Representa el registro de un prestamo de un implemento cultural a un
 * responsable (monitor o estudiante del grupo artistico).
 */
@Document(collection = "prestamos")
public class Prestamo {

    @Id
    private String id;

    private String implementoId;
    private String responsable;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaLimite;    // fecha esperada de devolucion
    private LocalDateTime fechaDevolucion;
    private String estado; // "ACTIVO", "VENCIDO" o "DEVUELTO"

    public Prestamo() {
    }

    public Prestamo(String implementoId, String responsable, LocalDateTime fechaLimite) {
        this.implementoId = implementoId;
        this.responsable = responsable;
        this.fechaPrestamo = LocalDateTime.now();
        this.fechaLimite = fechaLimite;
        this.estado = "ACTIVO";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImplementoId() {
        return implementoId;
    }

    public void setImplementoId(String implementoId) {
        this.implementoId = implementoId;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
