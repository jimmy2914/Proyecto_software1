package com.proyecto.workflow.model;

public class ProcesoDTO {
    private String id;
    private String nombre;

    public ProcesoDTO(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}