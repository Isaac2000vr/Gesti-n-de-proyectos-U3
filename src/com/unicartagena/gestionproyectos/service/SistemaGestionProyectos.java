package com.unicartagena.gestionproyectos.service;

import com.unicartagena.gestionproyectos.model.EstadoTarea;
import com.unicartagena.gestionproyectos.model.Prioridad;
import com.unicartagena.gestionproyectos.model.Proyecto;
import com.unicartagena.gestionproyectos.model.Responsable;
import com.unicartagena.gestionproyectos.model.Tarea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Lógica de negocio del sistema de gestión de proyectos.
 *
 * <p>Esta clase concentra las colecciones y las operaciones. El menú de
 * consola no manipula directamente las estructuras: solo invoca los métodos
 * públicos de este servicio.</p>
 *
 * <p>Colecciones utilizadas y su propósito:</p>
 * <ul>
 *   <li>{@code List<Tarea>} con {@link ArrayList}: registro general en orden
 *       de inserción.</li>
 *   <li>{@code Map<String, Tarea>} con {@link HashMap}: búsqueda directa por
 *       código único.</li>
 *   <li>{@code Set<Proyecto>} con {@link LinkedHashSet}: catálogo de proyectos
 *       sin duplicados, conservando el orden de registro.</li>
 *   <li>{@code Set<Responsable>} con {@link HashSet}: catálogo de responsables
 *       sin duplicados.</li>
 *   <li>{@code TreeSet<Tarea>}: tareas ordenadas automáticamente por código.</li>
 *   <li>{@code PriorityQueue<Tarea>}: tareas pendientes ordenadas por
 *       prioridad y, en caso de empate, por código.</li>
 * </ul>
 */
public class SistemaGestionProyectos {

    private final List<Tarea> tareas;
    private final Map<String, Tarea> tareasPorCodigo;
    private final Set<Proyecto> proyectos;
    private final Set<Responsable> responsables;
    private final TreeSet<Tarea> tareasOrdenadasPorCodigo;
    private final PriorityQueue<Tarea> colaPrioridad;

    public SistemaGestionProyectos() {
        this.tareas = new ArrayList<>();
        this.tareasPorCodigo = new HashMap<>();
        this.proyectos = new LinkedHashSet<>();
        this.responsables = new HashSet<>();
        this.tareasOrdenadasPorCodigo = new TreeSet<>(
                Comparator.comparing(Tarea::getCodigo, String.CASE_INSENSITIVE_ORDER));
        this.colaPrioridad = new PriorityQueue<>();
        cargarDatosDePrueba();
    }

    // ------------------------------------------------------------------
    // Datos de prueba: 15 tareas, 4 proyectos y 5 responsables
    // ------------------------------------------------------------------
    public void cargarDatosDePrueba() {
        limpiarDatos();

        Proyecto portal = new Proyecto("P01", "Portal de clientes", "Desarrollo");
        Proyecto movil = new Proyecto("P02", "Aplicación móvil", "Desarrollo");
        Proyecto nube = new Proyecto("P03", "Migración a la nube", "Infraestructura");
        Proyecto calidad = new Proyecto("P04", "Aseguramiento de calidad", "QA");
        proyectos.add(portal);
        proyectos.add(movil);
        proyectos.add(nube);
        proyectos.add(calidad);

        Responsable ana = new Responsable("1001", "Ana Torres", "Líder de proyecto");
        Responsable luis = new Responsable("1002", "Luis Gómez", "Desarrollador backend");
        Responsable marta = new Responsable("1003", "Marta Ruiz", "Analista QA");
        Responsable carlos = new Responsable("1004", "Carlos Díaz", "DevOps");
        Responsable sofia = new Responsable("1005", "Sofía Pérez", "Analista funcional");
        responsables.add(ana);
        responsables.add(luis);
        responsables.add(marta);
        responsables.add(carlos);
        responsables.add(sofia);

        agregarTarea(new Tarea("T001", "Diseñar pantalla de inicio de sesión",
                portal, ana, Prioridad.CRITICA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T002", "Implementar API de pagos",
                portal, luis, Prioridad.CRITICA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T003", "Configurar pipeline CI/CD",
                nube, carlos, Prioridad.ALTA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T004", "Ejecutar pruebas de regresión del portal",
                calidad, marta, Prioridad.ALTA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T005", "Documentar requisitos funcionales",
                movil, sofia, Prioridad.MEDIA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T006", "Optimizar consultas SQL",
                nube, luis, Prioridad.MEDIA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T007", "Actualizar manual de usuario",
                movil, sofia, Prioridad.BAJA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T008", "Revisar accesibilidad de la interfaz",
                calidad, marta, Prioridad.BAJA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T009", "Migrar base de datos a la nube",
                nube, carlos, Prioridad.ALTA, EstadoTarea.PROCESADA));
        agregarTarea(new Tarea("T010", "Diseñar reportes gerenciales",
                portal, sofia, Prioridad.MEDIA, EstadoTarea.PROCESADA));
        agregarTarea(new Tarea("T011", "Corregir error de cierre de sesión",
                portal, luis, Prioridad.CRITICA, EstadoTarea.PROCESADA));
        agregarTarea(new Tarea("T012", "Capacitar al equipo en el nuevo portal",
                movil, ana, Prioridad.BAJA, EstadoTarea.PROCESADA));
        agregarTarea(new Tarea("T013", "Validar políticas de seguridad",
                nube, carlos, Prioridad.CRITICA, EstadoTarea.PENDIENTE));
        agregarTarea(new Tarea("T014", "Ejecutar pruebas de carga",
                calidad, marta, Prioridad.MEDIA, EstadoTarea.CANCELADA));
        agregarTarea(new Tarea("T015", "Refactorizar módulo de clientes",
                portal, luis, Prioridad.ALTA, EstadoTarea.PENDIENTE));
    }

    public void limpiarDatos() {
        tareas.clear();
        tareasPorCodigo.clear();
        proyectos.clear();
        responsables.clear();
        tareasOrdenadasPorCodigo.clear();
        colaPrioridad.clear();
    }

    // ------------------------------------------------------------------
    // Registro de proyectos, responsables y tareas
    // ------------------------------------------------------------------
    public Proyecto registrarProyecto(String codigo, String nombre, String area) {
        Proyecto proyecto = new Proyecto(codigo, nombre, area);
        if (!proyectos.add(proyecto)) {
            throw new IllegalArgumentException(
                    "Ya existe un proyecto con el código " + codigo + ".");
        }
        return proyecto;
    }

    public Responsable registrarResponsable(String documento, String nombre, String cargo) {
        Responsable responsable = new Responsable(documento, nombre, cargo);
        if (!responsables.add(responsable)) {
            throw new IllegalArgumentException(
                    "Ya existe un responsable con el documento " + documento + ".");
        }
        return responsable;
    }

    public Tarea registrarTarea(String codigo, String descripcion,
                                String codigoProyecto, String documentoResponsable,
                                Prioridad prioridad, EstadoTarea estado) {
        if (buscarTarea(codigo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una tarea con el código " + codigo + ".");
        }

        Proyecto proyecto = buscarProyecto(codigoProyecto);
        if (proyecto == null) {
            throw new IllegalArgumentException(
                    "No existe un proyecto con el código " + codigoProyecto + ".");
        }

        Responsable responsable = buscarResponsable(documentoResponsable);
        if (responsable == null) {
            throw new IllegalArgumentException(
                    "No existe un responsable con el documento " + documentoResponsable + ".");
        }

        Tarea tarea = new Tarea(codigo, descripcion, proyecto, responsable, prioridad, estado);
        agregarTarea(tarea);
        return tarea;
    }

    private void agregarTarea(Tarea tarea) {
        if (tareasPorCodigo.containsKey(clave(tarea.getCodigo()))) {
            throw new IllegalArgumentException(
                    "Ya existe una tarea con el código " + tarea.getCodigo() + ".");
        }
        tareas.add(tarea);
        tareasPorCodigo.put(clave(tarea.getCodigo()), tarea);
        tareasOrdenadasPorCodigo.add(tarea);
        if (tarea.getEstado().estaPendiente()) {
            colaPrioridad.add(tarea);
        }
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------
    public List<Tarea> listarTareas() {
        return new ArrayList<>(tareas);
    }

    public List<Tarea> listarTareasOrdenadasPorCodigo() {
        return new ArrayList<>(tareasOrdenadasPorCodigo);
    }

    public List<Proyecto> listarProyectos() {
        List<Proyecto> lista = new ArrayList<>(proyectos);
        lista.sort(Comparator.comparing(Proyecto::getCodigo, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

    public List<Responsable> listarResponsables() {
        List<Responsable> lista = new ArrayList<>(responsables);
        lista.sort(Comparator.comparing(Responsable::getDocumento, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

    /**
     * Copia ordenada de la cola de prioridad. No se debe recorrer una
     * PriorityQueue directamente con un for-each esperando orden: su
     * iterador no garantiza el orden de atención.
     */
    public List<Tarea> listarColaPrioridad() {
        List<Tarea> lista = new ArrayList<>(colaPrioridad);
        lista.sort(Comparator.naturalOrder());
        return lista;
    }

    public Tarea buscarTarea(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        return tareasPorCodigo.get(clave(codigo));
    }

    public boolean existeTarea(String codigo) {
        return buscarTarea(codigo) != null;
    }

    public Proyecto buscarProyecto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return proyecto;
            }
        }
        return null;
    }

    public Responsable buscarResponsable(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return null;
        }
        for (Responsable responsable : responsables) {
            if (responsable.getDocumento().equalsIgnoreCase(documento.trim())) {
                return responsable;
            }
        }
        return null;
    }

    public int cantidadTareas() {
        return tareas.size();
    }

    public int cantidadPendientes() {
        return colaPrioridad.size();
    }

    // ------------------------------------------------------------------
    // Procesamiento por prioridad
    // ------------------------------------------------------------------
    public Tarea siguienteTareaPrioritaria() {
        return colaPrioridad.peek();
    }

    public Tarea procesarSiguienteTarea() {
        Tarea tarea = colaPrioridad.poll();
        if (tarea == null) {
            throw new NoSuchElementException(
                    "No hay tareas pendientes en la cola de prioridad.");
        }
        tarea.setEstado(EstadoTarea.PROCESADA);
        return tarea;
    }

    public void encolarTarea(String codigo) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        if (!tarea.getEstado().estaPendiente()) {
            throw new IllegalArgumentException(
                    "Solo se pueden encolar tareas en estado PENDIENTE. "
                            + "La tarea " + tarea.getCodigo() + " está en estado "
                            + tarea.getEstado() + ".");
        }
        if (colaPrioridad.contains(tarea)) {
            throw new IllegalArgumentException(
                    "La tarea " + tarea.getCodigo() + " ya está en la cola de prioridad.");
        }
        colaPrioridad.add(tarea);
    }

    // ------------------------------------------------------------------
    // Modificaciones con sincronización de colecciones
    // ------------------------------------------------------------------
    public void actualizarDescripcion(String codigo, String nuevaDescripcion) {
        obtenerTareaObligatoria(codigo).setDescripcion(nuevaDescripcion);
    }

    public void actualizarCodigo(String codigo, String nuevoCodigo) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        String nuevo = nuevoCodigo == null ? "" : nuevoCodigo.trim();

        if (nuevo.isEmpty()) {
            throw new IllegalArgumentException("El nuevo código no puede estar vacío.");
        }
        if (!tarea.getCodigo().equalsIgnoreCase(nuevo) && buscarTarea(nuevo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una tarea con el código " + nuevo + ".");
        }

        boolean pendiente = tarea.getEstado().estaPendiente();

        // Se retira de las colecciones que dependen del código antes de cambiarlo.
        if (pendiente) {
            colaPrioridad.remove(tarea);
        }
        tareasOrdenadasPorCodigo.remove(tarea);
        tareasPorCodigo.remove(clave(tarea.getCodigo()));

        tarea.setCodigo(nuevo);

        tareasPorCodigo.put(clave(tarea.getCodigo()), tarea);
        tareasOrdenadasPorCodigo.add(tarea);
        if (pendiente) {
            colaPrioridad.add(tarea);
        }
    }

    public void actualizarProyecto(String codigo, String codigoProyecto) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        Proyecto proyecto = buscarProyecto(codigoProyecto);
        if (proyecto == null) {
            throw new IllegalArgumentException(
                    "No existe un proyecto con el código " + codigoProyecto + ".");
        }
        tarea.setProyecto(proyecto);
    }

    public void actualizarResponsable(String codigo, String documentoResponsable) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        Responsable responsable = buscarResponsable(documentoResponsable);
        if (responsable == null) {
            throw new IllegalArgumentException(
                    "No existe un responsable con el documento " + documentoResponsable + ".");
        }
        tarea.setResponsable(responsable);
    }

    public void actualizarPrioridad(String codigo, Prioridad nuevaPrioridad) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        boolean pendiente = tarea.getEstado().estaPendiente();

        // La PriorityQueue debe reconstruirse si cambia el criterio de orden.
        if (pendiente) {
            colaPrioridad.remove(tarea);
        }
        tarea.setPrioridad(nuevaPrioridad);
        if (pendiente) {
            colaPrioridad.add(tarea);
        }
    }

    public void actualizarEstado(String codigo, EstadoTarea nuevoEstado) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        boolean estabaPendiente = tarea.getEstado().estaPendiente();
        boolean seraPendiente = nuevoEstado.estaPendiente();

        if (estabaPendiente && !seraPendiente) {
            colaPrioridad.remove(tarea);
        }
        tarea.setEstado(nuevoEstado);
        if (!estabaPendiente && seraPendiente) {
            colaPrioridad.add(tarea);
        }
    }

    public Tarea eliminarTarea(String codigo) {
        Tarea tarea = obtenerTareaObligatoria(codigo);
        tareas.remove(tarea);
        tareasPorCodigo.remove(clave(tarea.getCodigo()));
        tareasOrdenadasPorCodigo.remove(tarea);
        colaPrioridad.remove(tarea);
        return tarea;
    }

    // ------------------------------------------------------------------
    // Información de las colecciones
    // ------------------------------------------------------------------
    public String resumenColecciones() {
        return "List<Tarea> (ArrayList): " + tareas.size() + " tareas registradas.\n"
                + "Map<String, Tarea> (HashMap): " + tareasPorCodigo.size()
                + " claves de búsqueda.\n"
                + "Set<Proyecto> (LinkedHashSet): " + proyectos.size()
                + " proyectos únicos.\n"
                + "Set<Responsable> (HashSet): " + responsables.size()
                + " responsables únicos.\n"
                + "TreeSet<Tarea> (Comparator por código): " + tareasOrdenadasPorCodigo.size()
                + " tareas ordenadas.\n"
                + "PriorityQueue<Tarea> (Comparable por prioridad): " + colaPrioridad.size()
                + " tareas pendientes.\n"
                + "Claves del Map: " + new TreeSet<>(tareasPorCodigo.keySet());
    }

    // ------------------------------------------------------------------
    // Utilidades internas
    // ------------------------------------------------------------------
    private Tarea obtenerTareaObligatoria(String codigo) {
        Tarea tarea = buscarTarea(codigo);
        if (tarea == null) {
            throw new IllegalArgumentException(
                    "No existe una tarea con el código " + codigo + ".");
        }
        return tarea;
    }

    private String clave(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
