package test;

import entidades.HomeSolution;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.List;
import entidades.Tupla;

public class HomeSolutionTest {

    @Test
    public void testRegistroEmpleados() {
        HomeSolution hs = new HomeSolution();
        hs.registrarEmpleado("Juan", 1000);
        hs.registrarEmpleado("Luis", 2000, "EXPERTO");
        List<Tupla<Integer, String>> empleados = hs.empleados();
        assertEquals(2, empleados.size());
        assertEquals("Juan", empleados.get(0).getValor2());
    }

    @Test
    public void testRegistroProyectoYAsignacion() throws Exception {
        HomeSolution hs = new HomeSolution();
        hs.registrarEmpleado("Ana", 1500);

        String[] titulos = {"Electricidad"};
        String[] descripciones = {"Instalación eléctrica"};
        double[] dias = {5};
        String[] cliente = {"Carlos"};
        hs.registrarProyecto(titulos, descripciones, dias, "Av. Siempre Viva", cliente, "2025-10-01", "2025-10-10");

        Object[] tareas = hs.tareasDeUnProyecto(1);
        assertEquals(1, tareas.length);
        assertEquals("Electricidad", tareas[0]);

        hs.asignarResponsableEnTarea(1, "Electricidad");
        Object[] noAsignadas = hs.tareasProyectoNoAsignadas(1);
        assertEquals(0, noAsignadas.length);
    }

    @Test
    public void testRegistrarRetrasoYFinalizarTarea() throws Exception {
        HomeSolution hs = new HomeSolution();
        hs.registrarEmpleado("Pedro", 1200);

        String[] titulos = {"Pintura"};
        String[] descripciones = {"Pintar paredes"};
        double[] dias = {3};
        String[] cliente = {"Lucía"};
        hs.registrarProyecto(titulos, descripciones, dias, "Calle Falsa 123", cliente, "2025-10-01", "2025-10-05");

        hs.asignarResponsableEnTarea(1, "Pintura");
        hs.registrarRetrasoEnTarea(1, "Pintura", 2);
        assertTrue(hs.tieneRestrasos(100)); // legajo generado automáticamente

        hs.finalizarTarea(1, "Pintura");
        assertTrue(hs.estaFinalizado(1));
    }

    @Test
    public void testCostoProyectoConRetraso() throws Exception {
        HomeSolution hs = new HomeSolution();
        hs.registrarEmpleado("Sofía", 1000);

        String[] titulos = {"Gas"};
        String[] descripciones = {"Instalación de gas"};
        double[] dias = {4};
        String[] cliente = {"Martín"};
        hs.registrarProyecto(titulos, descripciones, dias, "Ruta 8", cliente, "2025-10-01", "2025-10-05");

        hs.asignarResponsableEnTarea(1, "Gas");
        hs.registrarRetrasoEnTarea(1, "Gas", 3);
        hs.finalizarTarea(1, "Gas");

        double costo = hs.costoProyecto(1);
        assertTrue(costo > 0);
    }

    @Test
    public void testConsultasFinalizadosPendientesActivos() throws Exception {
        HomeSolution hs = new HomeSolution();
        hs.registrarEmpleado("Leo", 1000);

        String[] titulos = {"Techo"};
        String[] descripciones = {"Reparación de techo"};
        double[] dias = {2};
        String[] cliente = {"María"};
        hs.registrarProyecto(titulos, descripciones, dias, "Barrio Norte", cliente, "2025-10-01", "2025-10-05");

        hs.asignarResponsableEnTarea(1, "Techo");
        hs.finalizarTarea(1, "Techo");

        List<Tupla<Integer, String>> finalizados = hs.proyectosFinalizados();
        assertEquals(1, finalizados.size());

        List<Tupla<Integer, String>> activos = hs.proyectosActivos();
        assertEquals(0, activos.size());
    }
    public class HomeSolutionExtraTest {

        // --- Helpers locales (no tocan HomeSolution) ---
        private int legajoDeEmpleadoPorNombre(HomeSolution hs, String nombre) {
            List<Tupla<Integer,String>> empleados = hs.empleados();
            for (Tupla<Integer,String> t : empleados) {
                if (t.getValor2().equals(nombre)) return t.getValor1();
            }
            throw new RuntimeException("Empleado no encontrado: " + nombre);
        }

        private boolean tareaEstaAsignada(HomeSolution hs, int nroProyecto, String titulo) {
            Object[] noAsignadas = hs.tareasProyectoNoAsignadas(nroProyecto);
            for (Object o : noAsignadas) {
                if (o instanceof String && ((String) o).equals(titulo)) return false;
                if (o != null && o.equals(titulo)) return false;
            }
            // si no está en noAsignadas, asumimos que está asignada (según API usada)
            return true;
        }

        // --- Tests ---

        @Test
        public void testRegistrarProyectoFechasInvalidas() {
            HomeSolution hs = new HomeSolution();
            String[] titulos = {"X"};
            String[] descripciones = {"d"};
            double[] dias = {1};
            String[] cliente = {"C"};
            assertThrows(IllegalArgumentException.class, () -> {
                hs.registrarProyecto(titulos, descripciones, dias, "Dir", cliente, "2025-10-10", "2025-10-01");
            });
        }

        @Test
        public void testAsignarSinEmpleadosDisponibles() throws Exception {
            HomeSolution hs = new HomeSolution();
            String[] titulos = {"T1"};
            String[] descripciones = {"d"};
            double[] dias = {2};
            String[] cliente = {"C"};
            hs.registrarProyecto(titulos, descripciones, dias, "Dir", cliente, "2025-10-01", "2025-10-05");
            assertThrows(Exception.class, () -> hs.asignarResponsableEnTarea(1, "T1"));
            Object[] noAsign = hs.tareasProyectoNoAsignadas(1);
            assertEquals(1, noAsign.length);
        }

        @Test
        public void testIncrementarRetrasosViaTareas() throws Exception {
            HomeSolution hs = new HomeSolution();
            // crear empleado y proyecto con una tarea
            hs.registrarEmpleado("Pedro", 1200);
            String[] titulos = {"Pintura"};
            String[] descripciones = {"Pintar paredes"};
            double[] dias = {1};
            String[] cliente = {"Lucía"};
            hs.registrarProyecto(titulos, descripciones, dias, "Calle Falsa 123", cliente, "2025-10-01", "2025-10-02");

            // asignar la tarea al único empleado disponible
            hs.asignarResponsableEnTarea(1, "Pintura");

            // obtener legajo del empleado
            int legajo = legajoDeEmpleadoPorNombre(hs, "Pedro");

            // registrar retrasos repetidos sobre la misma tarea (simula múltiples retrasos)
            hs.registrarRetrasoEnTarea(1, "Pintura", 1); // 1 día
            // si la lógica cuenta "tener retrazos" con un umbral, podemos llamar otra vez
            hs.registrarRetrasoEnTarea(1, "Pintura", 1);

            // ahora debería reportar que ese legajo tiene retrasos
            assertTrue(hs.tieneRestrasos(legajo));
        }

        @Test
        public void testFinalizarTareaLiberaEmpleadoYProyectoFinalizado() throws Exception {
            HomeSolution hs = new HomeSolution();
            hs.registrarEmpleado("Leo", 1000);
            String[] titulos = {"Techo"};
            String[] descripciones = {"Reparación de techo"};
            double[] dias = {1};
            String[] cliente = {"María"};
            hs.registrarProyecto(titulos, descripciones, dias, "Barrio Norte", cliente, "2025-10-01", "2025-10-02");

            hs.asignarResponsableEnTarea(1, "Techo");
            assertTrue(tareaEstaAsignada(hs, 1, "Techo"));

            hs.finalizarTarea(1, "Techo");

            // proyect finalizado y sin proyectos activos
            List<Tupla<Integer, String>> finalizados = hs.proyectosFinalizados();
            assertEquals(1, finalizados.size());

            List<Tupla<Integer, String>> activos = hs.proyectosActivos();
            assertEquals(0, activos.size());
        }

        @Test
        public void testCostoProyectoConRetrasoEsPositivo() throws Exception {
            HomeSolution hs = new HomeSolution();
            hs.registrarEmpleado("Sofía", 1000);
            String[] titulos = {"Gas"};
            String[] descripciones = {"Instalación de gas"};
            double[] dias = {2};
            String[] cliente = {"Martín"};
            hs.registrarProyecto(titulos, descripciones, dias, "Ruta 8", cliente, "2025-10-01", "2025-10-03");

            hs.asignarResponsableEnTarea(1, "Gas");
            hs.registrarRetrasoEnTarea(1, "Gas", 2);
            hs.finalizarTarea(1, "Gas");

            double costo = hs.costoProyecto(1);
            assertTrue(costo > 0);
        }

        @Test
        public void testToStringHomeSolutionYFormatoTarea() throws Exception {
            HomeSolution hs = new HomeSolution();
            hs.registrarEmpleado("X", 1000);
            String[] titulos = {"Titulo"};
            String[] descripciones = {"d"};
            double[] dias = {1};
            String[] cliente = {"C"};
            hs.registrarProyecto(titulos, descripciones, dias, "Dir", cliente, "2025-10-01", "2025-10-02");

            String homeStr = hs.toString();
            assertNotNull(homeStr);
            Object[] tareas = hs.tareasDeUnProyecto(1);
            // si tareasDeUnProyecto devuelve strings con títulos, el toString de la tarea es el título
            assertEquals("Titulo", tareas[0]);
        }
    }
   
}
