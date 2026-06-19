package com.bluesoft.pawmarket.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje){
        super(mensaje);
    }

    public ResourceNotFoundException(String recurso, Long id) {
        super("No se encontró " + recurso + " con id: " + id);
    }
}
