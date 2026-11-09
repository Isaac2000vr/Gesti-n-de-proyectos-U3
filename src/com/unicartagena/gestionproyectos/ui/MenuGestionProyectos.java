package com.unicartagena.gestionproyectos.ui;

import com.unicartagena.gestionproyectos.model.EstadoTarea;
import com.unicartagena.gestionproyectos.model.Prioridad;
import com.unicartagena.gestionproyectos.model.Proyecto;
import com.unicartagena.gestionproyectos.model.Responsable;
import com.unicartagena.gestionproyectos.model.Tarea;
import com.unicartagena.gestionproyectos.service.SistemaGestionProyectos;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Menú de consola del sistema.
 *
 * <p>Esta clase se encarga de la interacción con el usuario. Las reglas de
 * negocio y el manejo de las colecciones están en
 * {@link SistemaGestionProyectos}.</p>
 */
public class MenuGestionProyectos {

    private final Scanner scanner;
    private final SistemaGestionProyectos sistema;
    private boolean ejecutando;

    public MenuGestionProyectos() {
        this.scanner = new Scanner(System.in);
        this.sistema = new SistemaGestionProyectos();
        this.ejecutando = true;
    }

    public void ejecutar() {
        System.out.println("==========================================================");
        System.out.println("   SISTEMA DE GESTIÓN DE PROYECTOS - CASO 26");
        System.out.println("==========================================================");

        while (ejecutando) {
            try {
                mostrarMenu();
                int opcion = leerEntero("Seleccione una opción: ");
                ejecutarOpcion(opcion);
            } catch (NoSuchElementException e) {
                System.out.println("\nError: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("\nError de validación: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nError inesperado: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Gracias por usar el sistema de gestión de proyectos.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("1. Registrar tarea");
        System.out.println("2. Consultar todas las tareas");
        System.out.println("3. Buscar tarea por código");
        System.out.println("4. Registrar proyecto");
        System.out.println("5. Consultar proyectos");
        System.out.println("6. Registrar responsable");
        System.out.println("7. Consultar responsables");
        System.out.println("8. Ver tareas ordenadas por código");
        System.out.println("9. Agregar tarea pendiente a la cola de prioridad");
        System.out.println("10. Consultar próxima tarea prioritaria");
        System.out.println("11. Procesar siguiente tarea prioritaria");
        System.out.println("12. Modificar tarea");
        System.out.println("13. Eliminar tarea");
        System.out.println("14. Mostrar información de las colecciones");
        System.out.println("15. Recargar datos de prueba");
        System.out.println("0. Salir");
        System.out.println("----------------------------------------------------------");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarTarea();
                break;
            case 2:
                verTodasLasTareas();
                break;
            case 3:
                buscarTarea();
                break;
            case 4:
                registrarProyecto();
                break;
            case 5:
                verProyectos();
                break;
            case 6:
                registrarResponsable();
                break;
            case 7:
                verResponsables();
                break;
            case 8:
                verTareasOrdenadasPorCodigo();
                break;
            case 9:
                agregarTareaAColaPrioridad();
                break;
            case 10:
                consultarProximaTarea();
                break;
            case 11:
                procesarSiguienteTarea();
                break;
            case 12:
                modificarTarea();
                break;
            case 13:
                eliminarTarea();
                break;
            case 14:
                mostrarInformacionColecciones();
                break;
            case 15:
                recargarDatosDePrueba();
                break;
            case 0:
                ejecutando = false;
                break;
            default:
                System.out.println("Opción inválida. Debe elegir un número entre 0 y 15.");
        }
    }

    // ------------------------------------------------------------------
    // Registro
    // ------------------------------------------------------------------
    private void registrarTarea() {
        System.out.println("\n--- REGISTRAR TAREA ---");
        String codigo = leerTexto("Código de la tarea: ");

        if (sistema.existeTarea(codigo)) {
            System.out.println("Error: ya existe una tarea con el código " + codigo + ".");
            return;
        }

        String descripcion = leerTexto("Descripción: ");
        mostrarProyectosDisponibles();
        String codigoProyecto = leerTexto("Código del proyecto: ");
        mostrarResponsablesDisponibles();
        String documento = leerTexto("Documento del responsable: ");

        System.out.println("Prioridades disponibles:");
        System.out.println("  1. CRITICA  2. ALTA  3. MEDIA  4. BAJA");
        Prioridad prioridad = leerPrioridad("Prioridad: ");

        System.out.println("Estados disponibles:");
        System.out.println("  1. PENDIENTE  2. EN_PROCESO  3. PROCESADA  4. CANCELADA");
        EstadoTarea estado = leerEstado("Estado inicial: ");

        Tarea tarea = sistema.registrarTarea(
                codigo, descripcion, codigoProyecto, documento, prioridad, estado);

        System.out.println("Tarea registrada correctamente.");
        System.out.println(tarea);
        System.out.println("La tarea quedó registrada en la List, el Map y el TreeSet.");
        if (tarea.getEstado().estaPendiente()) {
            System.out.println("Está PENDIENTE: use la opción 9 para agregarla "
                    + "a la cola de prioridad.");
        } else {
            System.out.println("No está PENDIENTE, así que no puede entrar "
                    + "a la cola de prioridad.");
        }
    }

    private void registrarProyecto() {
        System.out.println("\n--- REGISTRAR PROYECTO ---");
        String codigo = leerTexto("Código del proyecto: ");
        String nombre = leerTexto("Nombre del proyecto: ");
        String area = leerTexto("Área: ");

        Proyecto proyecto = sistema.registrarProyecto(codigo, nombre, area);
        System.out.println("Proyecto registrado correctamente:");
        System.out.println(proyecto);
        System.out.println("El Set<Proyecto> conserva el proyecto sin duplicados.");
    }

    private void registrarResponsable() {
        System.out.println("\n--- REGISTRAR RESPONSABLE ---");
        String documento = leerTexto("Documento: ");
        String nombre = leerTexto("Nombre: ");
        String cargo = leerTexto("Cargo: ");

        Responsable responsable = sistema.registrarResponsable(documento, nombre, cargo);
        System.out.println("Responsable registrado correctamente:");
        System.out.println(responsable);
        System.out.println("El Set<Responsable> conserva el responsable sin duplicados.");
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------
    private void verTodasLasTareas() {
        System.out.println("\n--- TODAS LAS TAREAS REGISTRADAS ---");
        List<Tarea> tareas = sistema.listarTareas();

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas registradas.");
            return;
        }

        for (int i = 0; i < tareas.size(); i++) {
            System.out.println((i + 1) + ". " + tareas.get(i));
        }
        System.out.println("Total en la List: " + tareas.size());
    }

    private void buscarTarea() {
        System.out.println("\n--- BUSCAR TAREA POR CÓDIGO ---");
        String codigo = leerTexto("Código a buscar: ");
        Tarea tarea = sistema.buscarTarea(codigo);

        if (tarea == null) {
            System.out.println("No existe una tarea con el código " + codigo
                    + " en el Map.");
            return;
        }

        System.out.println("Tarea encontrada en el Map con la clave "
                + codigo.trim().toUpperCase() + ":");
        System.out.println(tarea);
        System.out.println("¿Está en la cola de prioridad? "
                + sistema.listarColaPrioridad().contains(tarea));
    }

    private void verProyectos() {
        System.out.println("\n--- PROYECTOS REGISTRADOS ---");
        List<Proyecto> proyectos = sistema.listarProyectos();

        if (proyectos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
            return;
        }

        for (Proyecto proyecto : proyectos) {
            System.out.println("- " + proyecto);
        }
        System.out.println("Total de proyectos únicos: " + proyectos.size());
    }

    private void verResponsables() {
        System.out.println("\n--- RESPONSABLES REGISTRADOS ---");
        List<Responsable> responsables = sistema.listarResponsables();

        if (responsables.isEmpty()) {
            System.out.println("No hay responsables registrados.");
            return;
        }

        for (Responsable responsable : responsables) {
            System.out.println("- " + responsable);
        }
        System.out.println("Total de responsables únicos: " + responsables.size());
    }

    private void verTareasOrdenadasPorCodigo() {
        System.out.println("\n--- TAREAS ORDENADAS POR CÓDIGO (TreeSet) ---");
        List<Tarea> tareas = sistema.listarTareasOrdenadasPorCodigo();

        if (tareas.isEmpty()) {
            System.out.println("No hay tareas registradas.");
            return;
        }

        for (Tarea tarea : tareas) {
            System.out.println("- " + tarea.getCodigo() + " | "
                    + tarea.getPrioridad() + " | " + tarea.getEstado());
        }
        System.out.println("El TreeSet mantiene el orden por código automáticamente.");
    }

    // ------------------------------------------------------------------
    // Prioridad
    // ------------------------------------------------------------------
    private void agregarTareaAColaPrioridad() {
        System.out.println("\n--- AGREGAR TAREA A LA COLA DE PRIORIDAD ---");
        String codigo = leerTexto("Código de la tarea pendiente: ");
        sistema.encolarTarea(codigo);
        System.out.println("Tarea encolada correctamente.");
        mostrarOrdenDeAtencion();
    }

    private void consultarProximaTarea() {
        System.out.println("\n--- PRÓXIMA TAREA PRIORITARIA ---");
        Tarea tarea = sistema.siguienteTareaPrioritaria();

        if (tarea == null) {
            System.out.println("No hay tareas pendientes en la cola de prioridad.");
            return;
        }

        System.out.println("La próxima tarea a procesar es:");
        System.out.println(tarea);
        System.out.println("La PriorityQueue la selecciona por prioridad, "
                + "no por orden de llegada.");
    }

    private void procesarSiguienteTarea() {
        System.out.println("\n--- PROCESAR SIGUIENTE TAREA PRIORITARIA ---");
        Tarea tarea = sistema.procesarSiguienteTarea();

        System.out.println("Tarea procesada correctamente:");
        System.out.println(tarea);
        System.out.println("Su estado cambió a PROCESADA y salió de la cola, "
                + "pero sigue en la List, el Map y el TreeSet.");

        mostrarOrdenDeAtencion();
    }

    private void mostrarOrdenDeAtencion() {
        List<Tarea> pendientes = sistema.listarColaPrioridad();
        if (pendientes.isEmpty()) {
            System.out.println("No hay tareas pendientes en la cola.");
            return;
        }
        System.out.println("Orden previsto de atención (prioridad y luego código):");
        for (int i = 0; i < pendientes.size(); i++) {
            Tarea tarea = pendientes.get(i);
            System.out.println("  " + (i + 1) + ". " + tarea.getCodigo()
                    + " - " + tarea.getPrioridad() + " - " + tarea.getDescripcion());
        }
    }

    // ------------------------------------------------------------------
    // Modificación
    // ------------------------------------------------------------------
    private void modificarTarea() {
        System.out.println("\n--- MODIFICAR TAREA ---");
        String codigo = leerTexto("Código de la tarea a modificar: ");
        Tarea tarea = sistema.buscarTarea(codigo);

        if (tarea == null) {
            System.out.println("No existe una tarea con el código " + codigo + ".");
            return;
        }

        System.out.println("Tarea actual:");
        System.out.println(tarea);
        System.out.println();
        System.out.println("¿Qué desea modificar?");
        System.out.println("1. Descripción");
        System.out.println("2. Código");
        System.out.println("3. Proyecto");
        System.out.println("4. Responsable");
        System.out.println("5. Prioridad");
        System.out.println("6. Estado");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opción: ");
        switch (opcion) {
            case 1:
                sistema.actualizarDescripcion(codigo, leerTexto("Nueva descripción: "));
                break;
            case 2:
                sistema.actualizarCodigo(codigo, leerTexto("Nuevo código: "));
                break;
            case 3:
                mostrarProyectosDisponibles();
                sistema.actualizarProyecto(codigo, leerTexto("Nuevo código de proyecto: "));
                break;
            case 4:
                mostrarResponsablesDisponibles();
                sistema.actualizarResponsable(codigo, leerTexto("Nuevo documento: "));
                break;
            case 5:
                sistema.actualizarPrioridad(codigo, leerPrioridad("Nueva prioridad: "));
                break;
            case 6:
                sistema.actualizarEstado(codigo, leerEstado("Nuevo estado: "));
                break;
            case 0:
                return;
            default:
                System.out.println("Opción inválida. No se modificó la tarea.");
                return;
        }

        Tarea actualizada = sistema.buscarTarea(tarea.getCodigo());
        System.out.println("Tarea actualizada correctamente:");
        System.out.println(actualizada);
        System.out.println("Las colecciones relacionadas quedaron sincronizadas.");
    }

    // ------------------------------------------------------------------
    // Eliminación
    // ------------------------------------------------------------------
    private void eliminarTarea() {
        System.out.println("\n--- ELIMINAR TAREA ---");
        String codigo = leerTexto("Código de la tarea a eliminar: ");
        Tarea tarea = sistema.buscarTarea(codigo);

        if (tarea == null) {
            System.out.println("No existe una tarea con el código " + codigo + ".");
            return;
        }

        System.out.println("Tarea a eliminar:");
        System.out.println(tarea);
        String confirmacion = leerTexto("¿Confirma la eliminación? (s/n): ");

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        Tarea eliminada = sistema.eliminarTarea(codigo);
        System.out.println("Tarea eliminada de la List, el Map, el TreeSet y la PriorityQueue:");
        System.out.println(eliminada);
    }

    // ------------------------------------------------------------------
    // Información de colecciones y datos de prueba
    // ------------------------------------------------------------------
    private void mostrarInformacionColecciones() {
        System.out.println("\n--- INFORMACIÓN DE LAS COLECCIONES ---");
        System.out.println(sistema.resumenColecciones());
        System.out.println();
        mostrarOrdenDeAtencion();
    }

    private void recargarDatosDePrueba() {
        System.out.println("\n--- RECARGAR DATOS DE PRUEBA ---");
        String confirmacion = leerTexto(
                "Esto reemplaza los datos actuales por 15 tareas de prueba. ¿Continuar? (s/n): ");
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }
        sistema.cargarDatosDePrueba();
        System.out.println("Se cargaron 4 proyectos, 5 responsables y 15 tareas de prueba.");
    }

    // ------------------------------------------------------------------
    // Utilidades de lectura
    // ------------------------------------------------------------------
    private void mostrarProyectosDisponibles() {
        System.out.println("Proyectos disponibles:");
        List<Proyecto> proyectos = sistema.listarProyectos();
        if (proyectos.isEmpty()) {
            System.out.println("  (no hay proyectos registrados)");
            return;
        }
        for (Proyecto proyecto : proyectos) {
            System.out.println("  " + proyecto);
        }
    }

    private void mostrarResponsablesDisponibles() {
        System.out.println("Responsables disponibles:");
        List<Responsable> responsables = sistema.listarResponsables();
        if (responsables.isEmpty()) {
            System.out.println("  (no hay responsables registrados)");
            return;
        }
        for (Responsable responsable : responsables) {
            System.out.println("  " + responsable);
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = leerLinea().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Error: el valor no puede estar vacío. Intente de nuevo.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = leerLinea().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido.");
            }
        }
    }

    private Prioridad leerPrioridad(String mensaje) {
        while (true) {
            try {
                return Prioridad.desdeTexto(leerTexto(mensaje));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private EstadoTarea leerEstado(String mensaje) {
        while (true) {
            try {
                return EstadoTarea.desdeTexto(leerTexto(mensaje));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String leerLinea() {
        if (!scanner.hasNextLine()) {
            ejecutando = false;
            throw new NoSuchElementException("No hay más datos de entrada.");
        }
        return scanner.nextLine();
    }
}
