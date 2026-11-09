package com.unicartagena.gestionproyectos.model;

import java.util.Locale;

/**
 * Persona responsable de ejecutar una tarea.
 *
 * <p>El documento es el identificador principal. Se almacena dentro de un
 * {@code Set<Responsable>} para impedir responsables duplicados.</p>
 */
public class Responsable {

    private String documento;
    private String nombre;
    private String cargo;

    public Responsable(String documento, String nombre, String cargo) {
        setDocumento(documento);
        setNombre(nombre);
        setCargo(cargo);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento del responsable no puede estar vacío.");
        }
        this.documento = documento.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del responsable no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("El cargo del responsable no puede estar vacío.");
        }
        this.cargo = cargo.trim();
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Responsable)) {
            return false;
        }
        Responsable otro = (Responsable) objeto;
        return documento.equalsIgnoreCase(otro.documento);
    }

    @Override
    public int hashCode() {
        return documento.toUpperCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return documento + " - " + nombre + " (" + cargo + ")";
    }
}
