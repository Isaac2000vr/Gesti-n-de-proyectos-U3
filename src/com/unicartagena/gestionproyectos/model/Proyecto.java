package com.unicartagena.gestionproyectos.model;

import java.util.Locale;

/**
 * Proyecto al que pertenece una tarea.
 *
 * <p>El código es el identificador principal. Se almacena dentro de un
 * {@code Set<Proyecto>} para impedir proyectos duplicados.</p>
 */
public class Proyecto {

    private String codigo;
    private String nombre;
    private String area;

    public Proyecto(String codigo, String nombre, String area) {
        setCodigo(codigo);
        setNombre(nombre);
        setArea(area);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del proyecto no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("El área del proyecto no puede estar vacía.");
        }
        this.area = area.trim();
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Proyecto)) {
            return false;
        }
        Proyecto otro = (Proyecto) objeto;
        return codigo.equalsIgnoreCase(otro.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toUpperCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (Área: " + area + ")";
    }
}
