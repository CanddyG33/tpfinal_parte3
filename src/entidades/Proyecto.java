package entidades;

import java.time.LocalDate;
import java.util.*;

public class Proyecto {
    private final int id;
    private final String cliente; // simplificación; podés crear clase Cliente si la necesitan
    private final String domicilio;
    private LocalDate fechaInicio;
    private LocalDate fechaPrevista;
    private LocalDate fechaReal;
    private final Map<String, Tarea> tareasByTitulo;
    private final Set<Integer> empleadosActuales;
    private final List<Integer> historialEmpleados;

    public Proyecto(int id, String cliente, String domicilio, LocalDate fechaInicio, LocalDate fechaPrevista) {
        this.id = id;
        this.cliente = cliente;
        this.domicilio = domicilio;
        this.fechaInicio = fechaInicio;
        this.fechaPrevista = fechaPrevista;
        this.fechaReal = null;
        this.tareasByTitulo = new LinkedHashMap<>();
        this.empleadosActuales = new HashSet<>();
        this.historialEmpleados = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaPrevista() {
        return fechaPrevista;
    }

    public LocalDate getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(LocalDate fechaReal) {
        this.fechaReal = fechaReal;
    }

    public void agregarTarea(Tarea t) {
        tareasByTitulo.put(t.getTitulo(), t);
    }

    public Tarea obtenerTareaPorTitulo(String titulo) {
        return tareasByTitulo.get(titulo);
    }

    public Collection<Tarea> getTodasLasTareas() {
        return tareasByTitulo.values();
    }

    public void agregarEmpleadoActual(int legajo) {
        if (!empleadosActuales.contains(legajo)) {
            empleadosActuales.add(legajo);
            historialEmpleados.add(legajo);
        }
    }

    public void removerEmpleadoActual(int legajo) {
        empleadosActuales.remove(legajo);
    }

    public List<Integer> getHistorialEmpleados() {
        return Collections.unmodifiableList(historialEmpleados);
    }

    public Set<Integer> getEmpleadosActuales() {
        return Collections.unmodifiableSet(empleadosActuales);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto ").append(id).append(" - ").append(cliente).append(" - ").append(domicilio);
        return sb.toString();
    }
}
