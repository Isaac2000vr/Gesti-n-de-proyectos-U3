# Actividad Unidad 3 — Sistema de gestión de proyectos (Caso 26)

Implementación en Java del **caso 26: Sistema de gestión de proyectos**, usando
únicamente el **Java Collections Framework**. No se implementan manualmente
listas, colas, pilas ni nodos.

**Estudiante:** Isaac David Vásquez Ramos
**Código:** 7502510028
**Asignatura:** Estructuras de datos — Universidad de Cartagena

---

## 1. Qué hace el sistema

Permite administrar proyectos, responsables y tareas:

- Registrar y consultar proyectos y responsables.
- Registrar tareas asociadas a un proyecto y a un responsable.
- Buscar tareas por código.
- Ver las tareas ordenadas por código.
- Consultar y procesar la tarea pendiente más prioritaria.
- Modificar y eliminar tareas manteniendo sincronizadas todas las colecciones.
- Consultar el estado interno de cada colección.

El sistema arranca con **4 proyectos, 5 responsables y 15 tareas de prueba**
(10 pendientes y 5 ya procesadas o canceladas).

---

## 2. Colecciones utilizadas y por qué

| Necesidad del sistema | Colección elegida | Justificación |
|---|---|---|
| Registro general de todas las tareas en orden de inserción | `List<Tarea>` → `ArrayList` | Se recorre con frecuencia y se accede por posición. `ArrayList` ofrece acceso indexado O(1) y buen rendimiento en recorridos. |
| Búsqueda inmediata de una tarea por su código único | `Map<String, Tarea>` → `HashMap` | El código es la clave natural. `HashMap` da búsqueda, inserción y eliminación promedio O(1), sin recorrer toda la lista. |
| Catálogo de proyectos sin duplicados, conservando el orden de registro | `Set<Proyecto>` → `LinkedHashSet` | `Set` impide duplicados mediante `equals/hashCode` y `LinkedHashSet` conserva el orden de inserción para mostrarlo al usuario. |
| Catálogo de responsables sin duplicados | `Set<Responsable>` → `HashSet` | Solo interesa la unicidad por documento; el orden no es relevante y se ordena al mostrarlo. |
| Tareas siempre ordenadas por código | `TreeSet<Tarea>` con `Comparator` por código | `TreeSet` mantiene el orden automáticamente en cada inserción o eliminación, sin ordenamientos manuales. |
| Atender primero las tareas de mayor prioridad | `PriorityQueue<Tarea>` con `Comparable` | La cola de prioridad siempre entrega primero la tarea CRITICA y desempata por código. No atiende por orden de llegada. |

### Orden de atención

`Tarea.compareTo` ordena por **código ascendente**, igual que `equals`, para que
ambas comparaciones sean coherentes. El orden por prioridad se define con el
`Comparator` `SistemaGestionProyectos.POR_PRIORIDAD`: **prioridad descendente**
(CRITICA → ALTA → MEDIA → BAJA) y, si hay empate, **código ascendente**. Por eso
la `PriorityQueue` siempre deja al frente la tarea correcta.

### Consistencia entre colecciones

Una misma tarea vive en varias colecciones a la vez. Al cambiar su código,
prioridad o estado, o al eliminarla, el servicio retira y vuelve a insertar la
tarea en las estructuras afectadas. Así se evita:

- claves viejas en el `Map`,
- tareas desordenadas o duplicadas en el `TreeSet`,
- elementos con prioridad desactualizada dentro de la `PriorityQueue`.

La cola de prioridad se alimenta de forma **explícita** con la opción `9`. Si
una tarea ya estaba encolada y se le cambia el código o la prioridad, el
servicio conserva su lugar en la cola y la reinserta con el nuevo criterio. Si
no estaba encolada, sigue fuera hasta que el usuario la encole.

---

## 3. Estructura del proyecto

```
Actividad-U3-GestionProyectos/
├── src/com/unicartagena/gestionproyectos/
│   ├── Main.java
│   ├── model/
│   │   ├── Prioridad.java
│   │   ├── EstadoTarea.java
│   │   ├── Proyecto.java
│   │   ├── Responsable.java
│   │   └── Tarea.java
│   ├── service/
│   │   └── SistemaGestionProyectos.java
│   └── ui/
│       └── MenuGestionProyectos.java
├── ejecutar.sh
└── README.md
```

- **model**: entidades y enums. `Tarea` implementa `Comparable` y todas las
  entidades sobrescriben `equals/hashCode` por su identificador.
- **service**: contiene las colecciones y las reglas de negocio.
- **ui**: menú de consola y validación de entradas.

---

## 4. Cómo compilar y ejecutar

Requisitos: **JDK 17 o superior**.

```bash
cd Actividad-U3-GestionProyectos
chmod +x ejecutar.sh
./ejecutar.sh
```

O manualmente:

```bash
mkdir -p out
javac -encoding UTF-8 -Xlint:all -d out $(find src -name '*.java')
java -cp out com.unicartagena.gestionproyectos.Main
```

---

## 5. Menú

```
1.  Registrar tarea
2.  Consultar todas las tareas
3.  Buscar tarea por código
4.  Registrar proyecto
5.  Consultar proyectos
6.  Registrar responsable
7.  Consultar responsables
8.  Ver tareas ordenadas por código
9.  Agregar tarea pendiente a la cola de prioridad
10. Consultar próxima tarea prioritaria
11. Procesar siguiente tarea prioritaria
12. Modificar tarea
13. Eliminar tarea
14. Mostrar información de las colecciones
15. Recargar datos de prueba
0.  Salir
```

---

## 6. Validaciones implementadas

- Código de tarea, proyecto y documento de responsable no vacíos.
- Proyectos, responsables y tareas no duplicados (comparación sin distinguir
  mayúsculas).
- Una tarea solo puede registrarse con un proyecto y un responsable existentes.
- Solo las tareas en estado `PENDIENTE` se pueden encolar con la opción `9`.
- Una tarea no puede encolarse dos veces.
- La cola de prioridad de los datos de prueba arranca cargada; las tareas que
  registra el usuario se encolan explícitamente con la opción `9`.
- No se puede procesar una cola vacía.
- Al cambiar el código no se permite chocar con otra tarea existente.
- Entradas numéricas y enums validados con reintento en el menú.

---

## 7. Pruebas sugeridas

1. Ejecutar la opción `2` y verificar las 15 tareas iniciales.
2. Opción `3` con `T001`, `t001` y `T999`.
3. Opción `8` para comprobar el orden por código del `TreeSet`.
4. Opción `10` y luego `11` varias veces: deben salir primero T001, T002 y T013
   (CRITICA), después las ALTA, luego MEDIA y por último BAJA.
5. Registrar una tarea nueva en estado `PENDIENTE` y usar la opción `9` para
   encolarla; luego comprobar con la opción `10` que aparece según su prioridad.
   Volver a ejecutar la opción `9` con esa misma tarea y verificar que la
   rechaza por estar ya encolada.
6. Opción `12` → `5`, subir T007 a CRITICA y comprobar con la opción `10` que
   cambia el orden de atención.
7. Opción `12` → `2`, cambiar `T001` por `T100` y comprobar con las opciones
   `2`, `3`, `8` y `14` que no queda rastro del código anterior.
8. Opción `13` con `T006` y confirmar con `14` que baja el total en todas las
   colecciones.
9. Opción `15` para restaurar los datos de prueba.

---

## 8. Conclusión

El ejercicio muestra que cada colección responde a una necesidad distinta:
`ArrayList` para recorrer, `HashMap` para buscar por clave, `Set` para evitar
duplicados, `TreeSet` para mantener orden y `PriorityQueue` para atender por
prioridad. Elegir la estructura adecuada simplifica el código y mejora el
rendimiento de cada operación.
