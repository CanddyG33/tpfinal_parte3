package test;

import org.junit.Before;
import org.junit.Test;
import entidades.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class HomeSolutionExtraTests {

    private HomeSolution hs;

    @Before
    public void setup() {
        hs = new HomeSolution();
    }

    @Test
    public void asignacionFifo_y_empleadosNoAsignados() throws Exception {
        hs.registrarEmpleado("Ana", 1000);               // legajo 100
        hs.registrarEmpleado("Beto", 1200);       // legajo 101
        String[] tit = {"T1"};
        String[] desc = {"d"};
        double[] dias = {2.0};
        hs.registrarProyecto(tit, desc, dias, "Calle 1", new String[]{"Cliente1"}, "2025-01-01", "2025-01-10");
        hs.asignarResponsableEnTarea(1, "T1"); 
        Object[] noAsign = hs.tareasProyectoNoAsignadas(1);
        assertEquals(0, noAsign.length);
        Object[] noAsigs = hs.empleadosNoAsignados();
      
        assertEquals(1, noAsigs.length);
        assertEquals(101, ((Number) noAsigs[0]).intValue());
    }

    @Test
    public void asignarMenosRetraso_priorizaCorrectamente() throws Exception {
        hs.registrarEmpleado("Ana", 1000); //100
        hs.registrarEmpleado("Beto", 1200); //101
        hs.registrarEmpleado("Cami", 1100); //102
        // 3 tareas: T1, T2, T3
        String[] tit = {"T1","T2","T3"};
        String[] desc = {"x","y","z"};
        double[] dias = {1.0,1.0,1.0};
        hs.registrarProyecto(tit, desc, dias, "D", new String[]{"Cli"}, "2025-01-01", "2025-01-10");

        // Asignaciones FIFO: T1 -> Ana(100), T2 -> Beto(101). T3 queda libre.
        hs.asignarResponsableEnTarea(1, "T1");
        hs.asignarResponsableEnTarea(1, "T2");

        // Registrar retraso en la tarea de Beto (T2) para que su contador internamente se incremente
        hs.registrarRetrasoEnTarea(1, "T2", 5.0);

        // Finalizar T2 para liberar a Beto (así queda libre pero con muchos retrasos)
        hs.finalizarTarea(1, "T2");

        // Ahora asignar por menos retraso la tarea T3 — debe elegir Cami (102) porque Beto (101) tiene más retrasos
        hs.asignarResponsableMenosRetraso(1, "T3");
        Integer resp = hs.responsableDeTarea(1, "T3");
        assertNotNull(resp);
        assertNotEquals(101, resp.intValue());
    }


    @Test
    public void reasignacion_liberaYasignaCorrectamente() throws Exception {
        hs.registrarEmpleado("Ana", 1000);
        hs.registrarEmpleado("Beto", 1200);
        String[] t = {"T1"};
        String[] d = {"x"};
        double[] dias = {1.0};
        hs.registrarProyecto(t, d, dias, "D", new String[]{"Cli"}, "2025-01-01", "2025-01-10");
        hs.asignarResponsableEnTarea(1, "T1"); // asigna Ana (100)
        // reasignar a Beto (101)
        hs.reasignarEmpleadoEnProyecto(1, 101, "T1");
        Integer resp = hs.responsableDeTarea(1, "T1");
        assertEquals(101, resp.intValue());
        // Ana debe quedar libre
        assertFalse(hs.obtenerEmpleado(100).isAsignado());
    }

    @Test
    public void historialEmpleados_deProyecto_reflejaAsignaciones() throws Exception {
        hs.registrarEmpleado("Ana", 1000);
        hs.registrarEmpleado("Beto", 1200);
        String[] t = {"T1"};
        String[] d = {"x"};
        double[] dias = {1.0};
        hs.registrarProyecto(t, d, dias, "D", new String[]{"Cli"}, "2025-01-01", "2025-01-10");
        hs.asignarResponsableEnTarea(1, "T1"); // 100
        hs.reasignarEmpleadoEnProyecto(1, 101, "T1"); // 101
        List<Tupla<Integer,String>> hist = hs.historialEmpleadosDeProyecto(1);
        assertEquals(2, hist.size());
        assertEquals(100, hist.get(0).getValor1().intValue());
        assertEquals(101, hist.get(1).getValor1().intValue());
    }

    @Test
    public void registrarRetraso_actualizaPrioridadYFechas() throws Exception {
        hs.registrarEmpleado("Ana", 1000);
        String[] t = {"T1"};
        String[] d = {"x"};
        double[] dias = {2.0};
        hs.registrarProyecto(t, d, dias, "D", new String[]{"Cli"}, "2025-01-01", "2025-01-10");
        hs.asignarResponsableEnTarea(1, "T1"); // legajo 100
        hs.registrarRetrasoEnTarea(1, "T1", 3.0);
        assertTrue(hs.tieneRestrasos(100));
        Tarea tarea = hs.obtenerProyecto(1).obtenerTareaPorTitulo("T1");
        assertNotNull(tarea.getFechaPrevista());
    }

    @Test
    public void costoProyecto_aplicaFactoresSegunFechas() throws Exception {
        hs.registrarEmpleado("Ana", 1000); // 100
        String[] t = {"T1"};
        String[] d = {"x"};
        double[] dias = {1.0};
        hs.registrarProyecto(t, d, dias, "D", new String[]{"Cli"}, "2025-01-01", "2025-01-02");
        hs.asignarResponsableEnTarea(1, "T1");
        hs.finalizarProyecto(1, "2025-01-10");
        double costo = hs.costoProyecto(1);
        assertTrue(costo > 0);
    }
}
