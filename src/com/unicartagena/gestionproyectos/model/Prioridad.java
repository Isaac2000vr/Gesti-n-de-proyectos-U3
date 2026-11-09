package com.unicartagena.gestionproyectos.model;

import java.util.Locale;

/**
 * Niveles de prioridad de una tarea.
 *
 * <p>El valor numérico permite ordenar de mayor a menor prioridad:
 * CRITICA (4), ALTA (3), MEDIA (2) y BAJA (1).</p>
 */
public enum Prioridad {

    CRITICA(4),
    ALTA(3),
    MEDIA(2),
    BAJA(1);

    private final int nivel;

    Prioridad(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    /**
     * Convierte un texto ingresado por el usuario en una prioridad.
     * Acepta el nombre o el número de la opción.
     */
    public static Prioridad desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La prioridad no puede estar vacía.");
        }

        String valor = texto.trim().toUpperCase(Locale.ROOT)
                .replace("Í", "I")
                .replace("Á", "A")
                .replace("É", "E");

        switch (valor) {
            case "1":
            case "CRITICA":
                return CRITICA;
            case "2":
            case "ALTA":
                return ALTA;
            case "3":
            case "MEDIA":
                return MEDIA;
            case "4":
            case "BAJA":
                return BAJA;
            default:
                throw new IllegalArgumentException(
                        "Prioridad inválida: " + texto + ". Use CRITICA, ALTA, MEDIA o BAJA.");
        }
    }
}
