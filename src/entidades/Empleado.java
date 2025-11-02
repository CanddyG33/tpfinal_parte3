package entidades;

import java.util.Objects;

public abstract class Empleado {
    protected final int legajo;
    protected final String nombre;
    protected int nRetrasos;
    protected boolean estaAsignado;

    public Empleado(int legajo, String nombre) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.nRetrasos = 0;
        this.estaAsignado = false;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNRetrasos() {
        return nRetrasos;
    }

    public boolean isAsignado() {
        return estaAsignado;
    }

    public void marcarAsignado() {
        this.estaAsignado = true;
    }

    public void marcarLibre() {
        this.estaAsignado = false;
    }

    public void incrementarRetraso() {
        this.nRetrasos++;
    }

    public void sumarRetraso(int dias) {
        if (dias > 0) this.nRetrasos += dias;
    }

    public abstract double calcularPago(double unidadesTrabajo);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Legajo: ").append(legajo).append(" - ").append(nombre)
          .append(" - Retrasos: ").append(nRetrasos)
          .append(" - Asignado: ").append(estaAsignado);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        Empleado e = (Empleado) o;
        return legajo == e.legajo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(legajo);
    }
}
