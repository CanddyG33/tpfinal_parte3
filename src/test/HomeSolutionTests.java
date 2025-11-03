package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import entidades.HomeSolution;
import entidades.IHomeSolution;
import entidades.Tupla;

public class HomeSolutionTests {
    private HomeSolution homeSolution;


    @Before
    public void setUp() {
        homeSolution=new HomeSolution();

        String titulos[]={"Pintar","Instalacion electrica","Trabajos jardineria","Instalar AA"};
        String descripciones[]={"","","",""};
        double duracion[]={4,2,1,.5};
        String cliente[]={"Pedro Gomez", "mail@mail.com", "123456"};
        homeSolution.registrarProyecto(titulos,descripciones,duracion,"San Martin 1000",cliente,"2025-12-01","2025-12-05");
        homeSolution.registrarEmpleado("Juan",15000);
        homeSolution.registrarEmpleado("Luis",80000, "EXPERTO");
        homeSolution.registrarEmpleado("Julieta",15000);
        homeSolution.registrarEmpleado("Carlos", 50000,"INICIAL");
        homeSolution.registrarProyecto(titulos,descripciones,duracion,"Libertador 500", cliente,"2025-12-10","2025-12-15");
    }

    // ============================================================
    // REGISTRO DE EMPLEADOS
    // ============================================================

    @Test
    public void testRegistrarEmpleadoContratadoValido() {
        homeSolution.registrarEmpleado("Juan Perez", 500.0);
        Object[] noAsignados = homeSolution.empleadosNoAsignados();
        assertEquals(5, noAsignados.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarEmpleadoValorNegativoLanzaExcepcion() {
        homeSolution.registrarEmpleado("Pedro", -200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarEmpleadoDePlantaNoValido() {
        homeSolution.registrarEmpleado("Ana", 600.0, "Senior");
    }

    // ============================================================
    // REGISTRO DE PROYECTOS
    // ============================================================

    @Test
    public void testRegistrarProyectoValido() {
        String[] titulos = {"T1"};
        String[] desc = {"Desc1"};
        double[] dias = {5.0};
        String domicilio = "Calle Falsa 123";
        String[] cliente = {"Carlos", "mail@mail.com", "123456"};
        String inicio = "2025-01-01";
        String fin = "2025-02-01";

        homeSolution.registrarProyecto(titulos, desc, dias, domicilio, cliente, inicio, fin);
        assertEquals(3, homeSolution.proyectosPendientes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProyectoDatosInvalidosLanzaExcepcion() {
        String[] titulos = {"T1"};
        String[] desc = {"Desc1"};
        double[] dias = {5.0};
        String domicilio = "Calle 123";
        String[] cliente = {"Carlos", "mail@mail.com", "123456"};
        String inicio = "2025-02-01";
        String fin = "2025-01-01";
        homeSolution.registrarProyecto(titulos, desc, dias, domicilio, cliente, inicio, fin);
    }

    // ============================================================
    // CONSULTAS Y ESTADO
    // ============================================================

    @Test
    public void testEmpleadosNoAsignadosInicialmente() {
        Object[] noAsignados = homeSolution.empleadosNoAsignados();
        assertEquals(4, noAsignados.length);
    }

    @Test
    public void testProyectosPendientesInicialmente() {
        List<Tupla<Integer, String>> lista = homeSolution.proyectosPendientes();
        assertTrue(lista.size()==2);
    }
    // ============================================================
    // REASIGNACIÓN Y FINALIZACIÓN
    // ============================================================
    @Test(expected = IllegalArgumentException.class)
    public void testTareasNoAsignadasProyectoFinalizadoLanzaExcepcion() {
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        homeSolution.finalizarProyecto(numeroProyecto, "2025-12-10");
        Object[] tareas= homeSolution.tareasProyectoNoAsignadas(numeroProyecto);
        assertTrue(tareas.length==0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFinalizarProyectoConFechaInvalidaLanzaExcepcion() {
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        homeSolution.finalizarProyecto(numeroProyecto, "2025-12-04");
    }

    // ============================================================
    // TESTS DE EMPLEADOS
    // ============================================================
    @Test
    public void testTotalTareasAsignadas() throws Exception {
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        Object[] tareas=homeSolution.tareasProyectoNoAsignadas(numeroProyecto);
        assertTrue(tareas.length==0);
    }
    @Test
    public void testTareasAsignadasProyectoActivo() throws Exception {
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        assertTrue(homeSolution.proyectosActivos().size()==1);
    }
    @Test(expected = Exception.class)
    public void testSinEmpleadosParaAsignar() throws Exception {
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        homeSolution.agregarTareaEnProyecto(numeroProyecto,"Limpieza","limpieza general",1);
        asignarTareas(numeroProyecto);
        homeSolution.asignarResponsableEnTarea(numeroProyecto,"Limpieza");
        assertTrue(homeSolution.tareasProyectoNoAsignadas(numeroProyecto).length==1);
    }
    @Test
    public void testAsignarMenosRetrasos() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Pintar",3);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalacion electrica",2);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalar AA",4);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Trabajos jardineria",1);
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-10");
        numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        homeSolution.asignarResponsableMenosRetraso(numeroProyecto,"Pintar");
        Object[] empleadosDisponibles=homeSolution.empleadosNoAsignados();
        List<Tupla<Integer, String>> todosLosEmpleados=homeSolution.empleados();
        assertEquals(1, todosLosEmpleados.size() - empleadosDisponibles.length);

    }
    @Test
    public void testAsignarElQueTieneMenosRetrasos() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Pintar",3);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalacion electrica",2);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalar AA",4);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Trabajos jardineria",2);
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-10");
        numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        homeSolution.asignarResponsableMenosRetraso(numeroProyecto,"Pintar");
        List<Tupla<Integer,String>> empleados=homeSolution.empleadosAsignadosAProyecto(numeroProyecto);
        System.out.println("=== DEBUG COMPLETO ANTES DE ASIGNAR MENOS RETRASO ===");
        Integer idActual = numeroProyecto;
        System.out.println(homeSolution.debugEstadoProyecto(idActual));
        
        // listar empleados totales y su contador
        System.out.println("Empleados listados por homeSolution.empleados():");
        for (Tupla<Integer,String> t : homeSolution.empleados()) {
            int leg = t.getValor1();
            System.out.println(" - legajo=" + leg + " nombre=" + t.getValor2()
                + " retrasos=" + homeSolution.consultarCantidadRetrasosEmpleado(leg)
                + " asignado? " + (!Arrays.asList(homeSolution.empleadosNoAsignados()).contains(leg)));
            System.out.println("Tras registrar retraso en tarea X, legajo " + leg + " tiene retrasos=" + homeSolution.consultarCantidadRetrasosEmpleado(leg));
        }

        // volcar empleadosNoAsignados y sus retrasos
        Object[] libres = homeSolution.empleadosNoAsignados();
        System.out.println("Empleados no asignados (count=" + libres.length + "): " + Arrays.toString(libres));
        for (Object o : libres) {
            int leg = Integer.parseInt(o.toString());
            System.out.println("  libre leg=" + leg + " retrasos=" + homeSolution.consultarCantidadRetrasosEmpleado(leg));
        }

        // volcar orden interno del TreeSet empleadosPorRetrasos (si lo exponés o con helper)
        System.out.println("Orden en empleadosPorRetrasos (menor->mayor):");
        for (Tupla<Integer,String> t : homeSolution.empleadosPorRetrasosOrden()) { // si no existe, usa el siguiente fallback
            System.out.println("  leg=" + t.getValor1() + " name=" + t.getValor2()
                + " retrasos=" + homeSolution.consultarCantidadRetrasosEmpleado(t.getValor1()));
        }

        assertEquals(homeSolution.consultarCantidadRetrasosEmpleado(empleados.get(0).getValor1()),1);
        
    }
    @Test
    public void testAsignarElQueNoTieneRetrasos() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Pintar",3);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalacion electrica",2);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalar AA",4);
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-10");
        numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();

        homeSolution.asignarResponsableMenosRetraso(numeroProyecto,"Pintar");
        List<Tupla<Integer,String>> empleados=homeSolution.empleadosAsignadosAProyecto(numeroProyecto);
        assertEquals(homeSolution.consultarCantidadRetrasosEmpleado(empleados.get(0).getValor1()),0);
    }
    @Test
    public void testEmpleadosTareasConRetraso() throws Exception{
        Integer numeroProyecto=homeSolution.proyectosPendientes().get(0).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Pintar",2);
        homeSolution.registrarRetrasoEnTarea(numeroProyecto,"Instalar AA",2);
        List<Tupla<Integer,String>> empleados=homeSolution.empleadosAsignadosAProyecto(numeroProyecto);
        int conRetraso=totalConRetraso(empleados);
        assertEquals(conRetraso,2);
    }
    @Test
    public void testEmpleadosLiberadosCorrectamente() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-06");
        assertTrue(homeSolution.empleadosNoAsignados().length==4);
    }
    
    @Test
    public void testCalculaCostoSinRetrasosCorrectamente() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        assertEquals(calculoCostoSinRetraso(), homeSolution.costoProyecto(numeroProyecto), 0.001);
    }
    @Test
    public void testCalculaCostoConRetrasosCorrectamente() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-10");
        assertEquals(calculoCostoConRetraso(), homeSolution.costoProyecto(numeroProyecto), 0.001);
    }
    @Test
    public void testCalculaCostoSinRetrasosYReasignacion() throws Exception{
        Integer numeroProyecto = (homeSolution.proyectosPendientes().get(0)).getValor1();
        asignarTareas(numeroProyecto);
        homeSolution.registrarEmpleado("Lidia",20000);
        Object[] emp=homeSolution.empleadosNoAsignados();
        Integer legajo=Integer.parseInt(emp[0].toString());
        homeSolution.reasignarEmpleadoEnProyecto(numeroProyecto,legajo,"Instalacion electrica");
        double costo=calculoCostoSinRetraso();  // Calculo el costo original sin retrasos
        costo /= 1.35;         // quito el porcentaje adicional
        costo -= 80000*2*1.02; // quito el costo del responsable reemplazado
        costo += 20000*2*8;    // agrego el costo del nuevo responsable
        costo *= 1.35;         // Agrego el porenteja adicional por no tener demoras
        homeSolution.finalizarProyecto(numeroProyecto,"2025-12-05");
        assertEquals(costo, homeSolution.costoProyecto(numeroProyecto), 0.001);
    }



    private void asignarTareas(Integer numeroProyecto) throws Exception{
        homeSolution.asignarResponsableEnTarea(numeroProyecto,"Pintar");
        homeSolution.asignarResponsableEnTarea(numeroProyecto,"Instalacion electrica");
        homeSolution.asignarResponsableEnTarea(numeroProyecto,"Trabajos jardineria");
        homeSolution.asignarResponsableEnTarea(numeroProyecto,"Instalar AA");
    }
    private double calcularCosto(){
        double costo=0;
        costo=15000*32;
        costo+=80000*2*1.02;
        costo+=15000*8;
        costo+=50000*1*1.02;
        return costo;
    }
    private double calculoCostoSinRetraso(){
        return calcularCosto()*1.35;
    }
    private double calculoCostoConRetraso(){
        return calcularCosto()*1.25;
    }
    private int totalConRetraso(List<Tupla<Integer,String>> empleados){
        int total=0;
        for(Tupla<Integer,String> emp: empleados)
        {
            total=homeSolution.tieneRestrasos(emp.getValor1())?total+1:total;
        }
        return total;
    }
}
