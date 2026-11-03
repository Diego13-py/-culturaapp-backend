package com.culturaapp.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepcion que lleva asociado el codigo HTTP que debe devolverse al
 * cliente movil (400, 404, 409, etc). Se lanza desde los servicios cuando
 * se detecta una violacion de una regla o un recurso no encontrado.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
