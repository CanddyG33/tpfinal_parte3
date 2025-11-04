# Trabajo Final — Programación 2 (2025)

##Descripción del proyecto

Este trabajo práctico simula la gestión de proyectos de servicios domiciliarios. Permite registrar empleados, crear proyectos con tareas, asignar responsables, marcar tareas como finalizadas, calcular costos y controlar retrasos.  
La lógica está implementada en la clase `HomeSolution`, que cumple con la interfaz `IHomeSolution` provista por la cátedra.  
Incluye una interfaz gráfica (`PanelManager`) para facilitar la interacción con el sistema.

---

## Estructura del proyecto

- `src/entidades`: contiene las clases del modelo (`Empleado`, `Proyecto`, `Tarea`, etc.).
- `src/gui`: contiene la interfaz gráfica.
- `src/main`: contiene el `Main.java` que inicia el programa.
- `IHomeSolution.java`: interfaz obligatoria del TP.
- `Informe_TP.pdf`: documento con análisis, IREP, decisiones de diseño y resultados de pruebas.

---

##  Cómo ejecutar

1. Abrir el proyecto en Eclipse, IntelliJ o VSCode.
2. Ejecutar la clase `Main.java` (ubicada en `src/main`) para iniciar la aplicación.
3. Usar la interfaz gráfica para:
   - Registrar empleados y proyectos.
   - Asignar tareas.
   - Consultar costos y finalizar proyectos.

---

##  Funcionalidades implementadas

- Registro de empleados (`EmpleadoPlanta`, `EmpleadoContratado`).
- Registro de proyectos con múltiples tareas.
- Asignación de empleados por orden FIFO, menor retraso o mayor eficiencia.
- Control de fechas previstas y reales.
- Cálculo de costos por hora y penalizaciones por retraso.
- Pruebas unitarias con JUnit (19/20 tests OK).
- Informe técnico con IREP completo y análisis de diseño.

---

## Autores

- **Tomas Clauser** — desarrollo principal, lógica, pruebas, documentación.
- **Matías [apellido]** — colaboración parcial en diagrama y correcciones.

---

## Repositorio

El proyecto también está disponible en GitHub:  
[https://github.com/CanddyG33/tpfinal_parte3](https://github.com/CanddyG33/tpfinal_parte3)

---

## Notas finales

- El código fue revisado y limpiado para la entrega.
- Se agregaron comentarios explicativos en métodos clave para facilitar la exposición oral.
- El informe incluye el análisis de un test que falla por diferencia de interpretación, debidamente justificado.

