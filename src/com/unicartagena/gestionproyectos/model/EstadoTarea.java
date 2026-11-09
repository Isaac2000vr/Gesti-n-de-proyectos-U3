package com.unicartagena.gestionproyectos.model;

import java.util.Locale;

/**
 * Estados posibles de una tarea dentro del sistema.
 */
public enum EstadoTarea {

    PENDIENTE,
    EN_PROCESO,
    PROCESADA,
    CANCELADA;

    public boolean estaPendiente() {
        return this == PENDIENTE;
    }

    /**
     * Convierte un texto ingresado por el usuario en un estado.
     * Acepta el nombre o el número de la opción.
     */
    public static EstadoTarea desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío.");
        }

        String valor = texto.trim().toUpperCase(Locale.ROOT)
                .replace("Í", "I")
                .replace("Á", "A")
                .replace("É", "E");

        switch (valor) {
            case "1":
            case "PENDIENTE":
                return PENDIENTE;
            case "2":
            case "EN_PROCESO":
            case "EN PROCESO":
                return EN_PROCESO;
            case "3":
            case "PROCESADA":
            case "PROCESADO":
                return PROCESADA;
            case "4":
            case "CANCELADA":
            case "CANCELADO":
                return CANCELADA;
            default:
                throw new IllegalArgumentException(
                        "Estado inválido: " + texto
                                + ". Use PENDIENTE, EN_PROCESO, PROCESADA o CANCELADA.");
        }
    }
}
