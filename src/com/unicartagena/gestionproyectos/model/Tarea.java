package com.unicartagena.gestionproyectos.model;

import java.util.Locale;

/**
 * Entidad principal del caso 26: Sistema de gestión de proyectos.
 *
 * <p>Una tarea pertenece a un proyecto, tiene un responsable, una prioridad
 * y un estado. El código es su identificador principal.</p>
 *
 * <p>El orden natural de las tareas es por código ascendente, igual que su
 * {@link #equals(Object)}, de modo que ambas comparaciones son coherentes.
 * El orden por prioridad que necesita la cola de atención se define aparte
 * con un {@code Comparator} en la capa de servicio.</p>
 */
public class Tarea implements Comparable<Tarea> {

    private String codigo;
    private String descripcion;
    private Proyecto proyecto;
    private Responsable responsable;
    private Prioridad prioridad;
    private EstadoTarea estado;

    public Tarea(String codigo, String descripcion, Proyecto proyecto,
                 Responsable responsable, Prioridad prioridad, EstadoTarea estado) {
        setCodigo(codigo);
        setDescripcion(descripcion);
        setProyecto(proyecto);
        setResponsable(responsable);
        setPrioridad(prioridad);
        setEstado(estado);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la tarea no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía.");
        }
        this.descripcion = descripcion.trim();
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        if (proyecto == null) {
            throw new IllegalArgumentException("La tarea debe tener un proyecto.");
        }
        this.proyecto = proyecto;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        if (responsable == null) {
            throw new IllegalArgumentException("La tarea debe tener un responsable.");
        }
        this.responsable = responsable;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La tarea debe tener una prioridad.");
        }
        this.prioridad = prioridad;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        if (estado == null) {
            throw new IllegalArgumentException("La tarea debe tener un estado.");
        }
        this.estado = estado;
    }

    /**
     * Orden natural por código, coherente con {@link #equals(Object)}.
     * La prioridad se maneja con un Comparator independiente en el servicio.
     */
    @Override
    public int compareTo(Tarea otra) {
        return this.codigo.compareToIgnoreCase(otra.codigo);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Tarea)) {
            return false;
        }
        Tarea otra = (Tarea) objeto;
        return codigo.equalsIgnoreCase(otra.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toUpperCase(Locale.ROOT).hashCode();
    }

    @Override
    public String toString() {
        return "Código: " + codigo
                + " | " + descripcion
                + " | Proyecto: " + proyecto.getCodigo() + " - " + proyecto.getNombre()
                + " | Responsable: " + responsable.getDocumento() + " - " + responsable.getNombre()
                + " | Prioridad: " + prioridad
                + " | Estado: " + estado;
    }
}
