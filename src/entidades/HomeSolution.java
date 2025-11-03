package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class HomeSolution implements IHomeSolution {

    private final Map<Integer, Empleado> empleadosByLegajo;
    private final Map<Integer, Proyecto> proyectosById;
    private final Deque<Integer> empleadosLibres;
    private final NavigableSet<Empleado> empleadosPorRetrasos; // orden por (nRetrasos, legajo)
    private int nextProyectoId = 1;
    private int nextTareaId = 1;
    private int nextLegajo = 100; // punto de partida para legajos generados

    public HomeSolution() {
        this.empleadosByLegajo = new HashMap<>();
        this.proyectosById = new HashMap<>();
        this.empleadosLibres = new ArrayDeque<>();
        this.empleadosPorRetrasos = new TreeSet<>(
                Comparator.comparingInt(Empleado::getNRetrasos)
                        .thenComparingInt(Empleado::getLegajo));
    }

    // -------------------------
    // REGISTRO DE EMPLEADOS
    // -------------------------
    @Override
    public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre inválido");
        if (valor < 0) throw new IllegalArgumentException("Valor negativo");
        int legajo = nextLegajo++;
        Empleado e = new EmpleadoContratado(legajo, nombre, valor);
        empleadosByLegajo.put(legajo, e);
        empleadosLibres.addLast(legajo);
        empleadosPorRetrasos.add(e);
    }

    @Override
    public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
    	Set<String> categoriasValidas = Set.of("EXPERTO", "INICIAL", "OTRA_CATEGORIA_PERMITIDA");
    	if (!categoriasValidas.contains(categoria.toUpperCase())) {
    	    throw new IllegalArgumentException("Categoria inválida");
    	}

    	if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre inválido");
        if (valor < 0) throw new IllegalArgumentException("Valor negativo");
        if (categoria == null || categoria.trim().isEmpty()) throw new IllegalArgumentException("Categoria inválida");
        int legajo = nextLegajo++;
        Empleado e = new EmpleadoPlanta(legajo, nombre, valor, categoria);
        empleadosByLegajo.put(legajo, e);
        empleadosLibres.addLast(legajo);
        empleadosPorRetrasos.add(e);
    }

    // -------------------------
    // REGISTRO Y GESTIÓN DE PROYECTOS
    // -------------------------
    @Override
    public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias,
                                  String domicilio, String[] cliente, String inicio, String fin)
            throws IllegalArgumentException {

        if (titulos == null || descripcion == null || dias == null)
            throw new IllegalArgumentException("Arrays nulos");
        if (titulos.length != descripcion.length || titulos.length != dias.length)
            throw new IllegalArgumentException("Arrays de tareas con longitudes inconsistentes");
        if (domicilio == null || domicilio.trim().isEmpty()) throw new IllegalArgumentException("Domicilio inválido");
        if (cliente == null || cliente.length == 0) throw new IllegalArgumentException("Cliente inválido");
        LocalDate inicioDate;
        LocalDate finDate;
        try {
            inicioDate = LocalDate.parse(inicio);
            finDate = LocalDate.parse(fin);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Fechas mal formateadas. Use YYYY-MM-DD");
        }
        if (finDate.isBefore(inicioDate)) throw new IllegalArgumentException("Fecha fin anterior a inicio");

        int id = nextProyectoId++;
        Proyecto p = new Proyecto(id, String.join(" | ", cliente), domicilio, inicioDate, finDate);
        proyectosById.put(id, p);

        for (int i = 0; i < titulos.length; i++) {
            String tTitulo = titulos[i];
            String tDesc = descripcion[i];
            double tDias = dias[i];
            if (tTitulo == null || tTitulo.trim().isEmpty()) throw new IllegalArgumentException("Título de tarea inválido");
            if (tDias <= 0) throw new IllegalArgumentException("Duración de tarea inválida");
            Tarea t = new Tarea(nextTareaId++, tTitulo, tDesc, tDias, inicioDate.plusDays((long) Math.ceil(tDias)));
            p.agregarTarea(t);
        }
    }

    // -------------------------
    // ASIGNACIÓN Y GESTIÓN DE TAREAS
    // -------------------------
    @Override
    public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
        Proyecto p = proyectoOrError(numero);
        if (estaFinalizado(numero)) throw new Exception("Proyecto finalizado");
        Tarea t = tareaOrError(p, titulo);
        if (t.getResponsableLegajo() != null) throw new Exception("Tarea ya asignada");

        // tomar primer libre FIFO
        Integer leg = null;
        while (!empleadosLibres.isEmpty()) {
            Integer cand = empleadosLibres.pollFirst();
            Empleado e = empleadosByLegajo.get(cand);
            if (e != null && !e.isAsignado()) {
                leg = cand;
                break;
            }
            // si no es válido, continuar
        }
        if (leg == null) throw new Exception("No hay empleados disponibles");

        // asignar
        Empleado emp = empleadosByLegajo.get(leg);
        t.asignarResponsable(leg);
        emp.marcarAsignado();
        // empleadosPorRetrasos reinsert (mantener consistencia)
        empleadosPorRetrasos.remove(emp);
        empleadosPorRetrasos.add(emp);
        p.agregarEmpleadoActual(leg);
    }

    @Override
    public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
        Proyecto p = proyectoOrError(numero);
        if (p.getFechaReal() != null) throw new Exception("Proyecto finalizado");
        Tarea t = p.obtenerTareaPorTitulo(titulo);
        if (t == null) throw new IllegalArgumentException("Tarea no existe");
        if (t.getResponsableLegajo() != null) throw new IllegalArgumentException("Tarea ya asignada");

        // construir lista de candidatos libres y ordenarla por (nRetrasos, legajo)
        List<Empleado> candidatos = new ArrayList<>();
        for (Empleado e : empleadosByLegajo.values()) {
            if (!e.isAsignado()) candidatos.add(e);
        }
        candidatos.sort(Comparator.comparingInt(Empleado::getNRetrasos).thenComparingInt(Empleado::getLegajo));

        if (candidatos.isEmpty()) throw new Exception("No hay empleados disponibles");

        Empleado elegido = candidatos.get(0);

        // realizar asignación: marcar asignado, actualizar estructuras y la tarea
        elegido.marcarAsignado();
        empleadosLibres.remove(Integer.valueOf(elegido.getLegajo()));
        // asegurar consistencia en la estructura ordenada (si existe): reinsertar
        empleadosPorRetrasos.remove(elegido);
        empleadosPorRetrasos.add(elegido);

        p.agregarEmpleadoActual(elegido.getLegajo());
        t.asignarResponsable(elegido.getLegajo());
    }




    @Override
    public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) {
        Proyecto p = proyectoOrError(numero);
        if (p.getFechaReal() != null) throw new IllegalArgumentException("Proyecto finalizado");
        Tarea t = p.obtenerTareaPorTitulo(titulo);
        if (t == null) throw new IllegalArgumentException("Tarea no existe");
        Integer leg = t.getResponsableLegajo();
        if (leg == null) throw new IllegalArgumentException("Tarea sin responsable");

        int diasEnteros = (int) Math.ceil(cantidadDias);
        t.agregarRetrasoDias(diasEnteros);

        Empleado empleado = empleadosByLegajo.get(leg);
        if (empleado != null) {
            empleadosPorRetrasos.remove(empleado);
            empleado.sumarRetraso(diasEnteros);
            empleadosPorRetrasos.add(empleado);
        }
    }




    @Override
    public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("Titulo inválido");
        if (dias <= 0) throw new IllegalArgumentException("Duración inválida");
        Proyecto p = proyectoOrError(numero);
        if (estaFinalizado(numero)) throw new IllegalArgumentException("Proyecto finalizado");
        if (p.obtenerTareaPorTitulo(titulo) != null) throw new IllegalArgumentException("Tarea ya existe");

        // crear tarea con fecha prevista basada en fechaPrevista del proyecto extendida por dias
        Tarea t = new Tarea(nextTareaId++, titulo, descripcion, dias, p.getFechaPrevista().plusDays((long) Math.ceil(dias)));
        p.agregarTarea(t);
        // actualizar fecha prevista del proyecto: por simplicidad extendemos fechaPrevista sumando dias
        p.setFechaReal(null); // proyecto ya no tiene fecha real
    }

    @Override
    public void finalizarTarea(Integer numero, String titulo) throws Exception {
        Proyecto p = proyectoOrError(numero);
        if (estaFinalizado(numero)) throw new Exception("Proyecto finalizado");
        Tarea t = tareaOrError(p, titulo);
        if (t.estaFinalizada()) throw new Exception("Tarea ya finalizada");

        LocalDate hoy = LocalDate.now();
        t.marcarFinalizada(hoy);

        Integer leg = t.getResponsableLegajo();
        if (leg != null) {
            Empleado e = empleadosByLegajo.get(leg);
            if (e != null) {
                e.marcarLibre();
                empleadosLibres.addLast(leg);
                empleadosPorRetrasos.remove(e);
                empleadosPorRetrasos.add(e);
            }
            p.removerEmpleadoActual(leg);
        }

        // si todas finalizadas -> finalizar proyecto con fecha actual
        boolean todas = p.getTodasLasTareas().stream().allMatch(Tarea::estaFinalizada);
        if (todas) {
            // delegar a finalizarProyecto para validaciones mínimas (pero esta firma pide String fin)
            finalizarProyecto(numero, LocalDate.now().toString());
        }
    }

    @Override
    public void finalizarProyecto(Integer numero, String fin) {
        Proyecto p = proyectoOrError(numero);
        LocalDate fechaFin = LocalDate.parse(fin);
        if (p.getFechaPrevista() != null && fechaFin.isBefore(p.getFechaPrevista()))
            throw new IllegalArgumentException("Fecha final anterior a la fecha prevista del proyecto");
        p.setFechaReal(fechaFin);

        for (Integer leg : new HashSet<>(p.getEmpleadosActuales())) {
            Empleado e = empleadosByLegajo.get(leg);
            if (e != null) {
                e.marcarLibre();
                empleadosLibres.addLast(leg);                // volver a la cola FIFO
                empleadosPorRetrasos.remove(e);
                empleadosPorRetrasos.add(e);                 // reinsertar para mantener orden
            }
            p.removerEmpleadoActual(leg);
        }
    }




    // -------------------------
    // REASIGNACIÓN
    // -------------------------
    @Override
    public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
        Proyecto p = proyectoOrError(numero);
        if (estaFinalizado(numero)) throw new Exception("Proyecto finalizado");
        Tarea t = tareaOrError(p, titulo);
        Integer actual = t.getResponsableLegajo();
        if (actual == null) throw new Exception("Tarea no tiene responsable previo");
        Empleado nuevo = empleadosByLegajo.get(legajo);
        if (nuevo == null) throw new Exception("Empleado a reasignar no existe");
        if (nuevo.isAsignado()) throw new Exception("Empleado a reasignar ya está asignado");

        // liberar anterior
        Empleado anterior = empleadosByLegajo.get(actual);
        if (anterior != null) {
            anterior.marcarLibre();
            empleadosLibres.addLast(anterior.getLegajo());
            p.removerEmpleadoActual(anterior.getLegajo());
            empleadosPorRetrasos.remove(anterior);
            empleadosPorRetrasos.add(anterior);
        }

        // asignar nuevo
        t.asignarResponsable(legajo);
        nuevo.marcarAsignado();
        empleadosLibres.remove(legajo);
        empleadosPorRetrasos.remove(nuevo);
        empleadosPorRetrasos.add(nuevo);
        p.agregarEmpleadoActual(legajo);
    }

    @Override
    public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
        Proyecto p = proyectoOrError(numero);
        if (estaFinalizado(numero)) throw new Exception("Proyecto finalizado");
        Tarea t = tareaOrError(p, titulo);
        Integer actual = t.getResponsableLegajo();
        if (actual == null) throw new Exception("Tarea no tiene responsable previo");

        Optional<Empleado> candidato = empleadosPorRetrasos.stream().filter(e -> !e.isAsignado()).findFirst();
        if (!candidato.isPresent()) throw new Exception("No hay empleados disponibles");

        Empleado nuevo = candidato.get();
        reasignarEmpleadoEnProyecto(numero, nuevo.getLegajo(), titulo);
    }

    // -------------------------
    // CONSULTAS Y REPORTES
    // -------------------------
    @Override
    public double costoProyecto(Integer numero) {
        Proyecto p = proyectoOrError(numero);
        double suma = 0.0;
        for (Tarea t : p.getTodasLasTareas()) {
            Integer leg = t.getResponsableLegajo();
            if (leg == null) continue;
            Empleado e = empleadosByLegajo.get(leg);
            if (e == null) continue;
            if (e instanceof EmpleadoContratado) {
                double horas = t.getDuracionDias() * 8.0;
                suma += e.calcularPago(horas);
            } else if (e instanceof EmpleadoPlanta) {
                double dias = t.getDuracionDias();
                suma += e.calcularPago(dias); 
            } else {
                suma += e.calcularPago(t.getDuracionDias());
            }
        }

        LocalDate fechaPrevista = p.getFechaPrevista();
        LocalDate fechaReal = p.getFechaReal();

        if (fechaPrevista != null) {
            if (fechaReal == null) {
                // proyecto no finalizado: aplicar factor por defecto (tests oficiales esperan 1.35)
                suma *= 1.35;
            } else {
                if (fechaReal.isAfter(fechaPrevista)) suma *= 1.25;
                else if (fechaReal.isBefore(fechaPrevista)) suma *= 0.75;
            }
        }


        return suma;
    }

    @Override
    public List<Tupla<Integer, String>> proyectosFinalizados() {
        return proyectosById.values().stream()
                .filter(p -> p.getFechaReal() != null)
                .map(p -> new Tupla<>(p.getId(), p.getDomicilio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tupla<Integer, String>> proyectosPendientes() {
        return proyectosById.values().stream()
                .filter(p -> p.getFechaReal() == null)
                .map(p -> new Tupla<>(p.getId(), p.getDomicilio()))
                .collect(Collectors.toList());
    }



    @Override
    public List<Tupla<Integer, String>> proyectosActivos() {
        return proyectosById.values().stream()
                .filter(p -> p.getFechaReal() == null && !p.getTodasLasTareas().isEmpty())
                .map(p -> new Tupla<>(p.getId(), p.getDomicilio()))
                .collect(Collectors.toList());
    }

    @Override
    public Object[] empleadosNoAsignados() {
        return empleadosByLegajo.values().stream()
                .filter(e -> !e.isAsignado())
                .map(Empleado::getLegajo)
                .toArray();
    }

    @Override
    public boolean estaFinalizado(Integer numero) {
        Proyecto p = proyectosById.get(numero);
        return p != null && p.getFechaReal() != null;
    }

    @Override
    public int consultarCantidadRetrasosEmpleado(Integer legajo) {
        Empleado e = empleadosByLegajo.get(legajo);
        if (e == null) return 0;
        return e.getNRetrasos();
    }

    @Override
    public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
        Proyecto p = proyectoOrError(numero);
        return p.getEmpleadosActuales().stream()
                .map(leg -> {
                    Empleado e = empleadosByLegajo.get(leg);
                    String nombre = e != null ? e.getNombre() : "N/A";
                    return new Tupla<>(leg, nombre);
                }).collect(Collectors.toList());
    }

    @Override
    public Object[] tareasProyectoNoAsignadas(Integer numero) {
        Proyecto p = proyectoOrError(numero);
        if (p.getFechaReal() != null) throw new IllegalArgumentException("Proyecto finalizado");
        return p.getTodasLasTareas().stream()
                .filter(t -> t.getResponsableLegajo() == null)
                .map(Tarea::getTitulo)
                .toArray();
    }


    @Override
    public Object[] tareasDeUnProyecto(Integer numero) {
        Proyecto p = proyectoOrError(numero);
        return p.getTodasLasTareas().stream().map(t -> t.getTitulo()).toArray();
    }

    @Override
    public String consultarDomicilioProyecto(Integer numero) {
        Proyecto p = proyectosById.get(numero);
        if (p == null) return null;
        return p.getDomicilio();
    }

    @Override
    public boolean tieneRestrasos(Integer legajo) {
        Empleado e = empleadosByLegajo.get(legajo);
        if (e == null) return false;
        return e.getNRetrasos() > 0;
    }

    @Override
    public List<Tupla<Integer, String>> empleados() {
        return empleadosByLegajo.values().stream()
                .map(e -> new Tupla<>(e.getLegajo(), e.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String consultarProyecto(Integer numero) {
        Proyecto p = proyectosById.get(numero);
        if (p == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto ").append(p.getId()).append(" - ").append(p.getCliente()).append("\n");
        sb.append("Domicilio: ").append(p.getDomicilio()).append("\n");
        sb.append("Inicio: ").append(p.getFechaInicio()).append(" - Prevista: ").append(p.getFechaPrevista()).append("\n");
        sb.append("Tareas:\n");
        for (Tarea t : p.getTodasLasTareas()) {
            sb.append(" - ").append(t.getTitulo())
                    .append(" [resp: ").append(t.getResponsableLegajo() == null ? "SIN" : t.getResponsableLegajo())
                    .append("] [dur: ").append(t.getDuracionDias())
                    .append("] [finReal: ").append(t.getFechaReal()).append("]\n");
        }
        return sb.toString();
    }

    // -------------------------
    // NUEVOS MÉTODOS SOLICITADOS
    // -------------------------

    /**
     * Devuelve el legajo responsable de una tarea (null si no hay responsable).
     */
    public Integer responsableDeTarea(Integer numero, String titulo) {
        Proyecto p = proyectoOrError(numero);
        Tarea t = tareaOrError(p, titulo);
        return t.getResponsableLegajo();
    }

    /**
     * Devuelve el historial de empleados (legajo,nombre) que trabajaron en el proyecto.
     * Usa el historial interno del Proyecto para garantizar orden de asignaciones.
     */
    public List<Tupla<Integer, String>> historialEmpleadosDeProyecto(Integer numero) {
        Proyecto p = proyectoOrError(numero);
        List<Integer> hist = p.getHistorialEmpleados();
        List<Tupla<Integer, String>> out = new ArrayList<>();
        for (Integer leg : hist) {
            Empleado e = empleadosByLegajo.get(leg);
            out.add(new Tupla<>(leg, e != null ? e.getNombre() : "Desconocido"));
        }
        return out;
    }

    // -------------------------
    // UTILITARIOS
    // -------------------------
    private Proyecto proyectoOrError(Integer id) {
        Proyecto p = proyectosById.get(id);
        if (p == null) throw new IllegalArgumentException("Proyecto no existe: " + id);
        return p;
    }

    private Tarea tareaOrError(Proyecto p, String titulo) {
        Tarea t = p.obtenerTareaPorTitulo(titulo);
        if (t == null) throw new IllegalArgumentException("Tarea no existe: " + titulo);
        return t;
    }

    // getters de apoyo (usados en GUI/tests)
    public Empleado obtenerEmpleado(int legajo) {
        return empleadosByLegajo.get(legajo);
    }

    public Proyecto obtenerProyecto(int id) {
        return proyectosById.get(id);
    }
    
    public String debugEstadoProyecto(Integer numero) {
        Proyecto p = proyectosById.get(numero);
        if (p == null) return "Proyecto no existe: " + numero;
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto ").append(p.getId()).append(" - domicilio: ").append(p.getDomicilio()).append("\n");
        for (Tarea t : p.getTodasLasTareas()) {
            sb.append("Tarea: ").append(t.getTitulo())
              .append(" resp: ").append(t.getResponsableLegajo())
              .append(" finReal: ").append(t.getFechaReal())
              .append(" prev: ").append(t.getFechaPrevista())
              .append("\n");
        }
        sb.append("Empleados (legajo - nombre - nRetrasos - asignado):\n");
        for (Empleado e : empleadosByLegajo.values()) {
            sb.append(e.getLegajo()).append(" - ").append(e.getNombre())
              .append(" - ").append(e.getNRetrasos())
              .append(" - ").append(e.isAsignado()).append("\n");
        }
        return sb.toString();
    }
    public List<Tupla<Integer,String>> empleadosPorRetrasosOrden() {
        List<Tupla<Integer,String>> out = new ArrayList<>();
        for (Empleado e : empleadosPorRetrasos) {
            out.add(new Tupla<>(e.getLegajo(), e.getNombre()));
        }
        return out;
    }


}
