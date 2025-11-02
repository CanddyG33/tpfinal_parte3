package entidades;

import java.time.LocalDate;
import java.util.Objects;

public class Tarea {
    private final String titulo;
    private final String descripcion;
    private final double duracionDias; // duración planificada en días (puede ser double)
    private Integer responsableLegajo; // null si sin responsable
    private LocalDate fechaPrevista;
    private LocalDate fechaReal; // null si no finalizada
    private final int id; // id único si hace falta

    public Tarea(int id, String titulo, String descripcion, double duracionDias, LocalDate fechaPrevista) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracionDias = duracionDias;
        this.fechaPrevista = fechaPrevista;
        this.fechaReal = null;
        this.responsableLegajo = null;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getDuracionDias() {
        return duracionDias;
    }

    public Integer getResponsableLegajo() {
        return responsableLegajo;
    }

    public void asignarResponsable(Integer legajo) {
        this.responsableLegajo = legajo;
    }

    public void desasignarResponsable() {
        this.responsableLegajo = null;
    }

    public LocalDate getFechaPrevista() {
        return fechaPrevista;
    }

    public LocalDate getFechaReal() {
        return fechaReal;
    }

    public void marcarFinalizada(LocalDate fechaReal) {
        this.fechaReal = fechaReal;
    }

    public boolean estaFinalizada() {
        return fechaReal != null;
    }

    public void agregarRetrasoDias(int dias) {
        if (fechaReal != null) {
            fechaReal = fechaReal.plusDays(dias);
        } else {
            fechaPrevista = fechaPrevista.plusDays(dias);
        }
    }

    @Override
    public String toString() {
        return titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarea)) return false;
        Tarea t = (Tarea) o;
        return id == t.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

