package com.culturaapp.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un implemento cultural (instrumento, vestuario, equipo de
 * sonido, etc.) que puede ser prestado a los grupos artisticos.
 */
@Document(collection = "implementos")
public class Implemento {

    @Id
    private String id;

    private String nombre;
    private String tipo;
    private String estado; // "DISPONIBLE" o "PRESTADO"
    private String descripcion;
    private String codigo;     // ej. "IMP-022", se autogenera si no se envía
    private String ubicacion;  // ej. "Área de Cultura"
    private String condicion;  // ej. "Bueno", "Regular", "Malo"

    public Implemento() {
    }

    public Implemento(String nombre, String tipo, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = "DISPONIBLE";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
}
