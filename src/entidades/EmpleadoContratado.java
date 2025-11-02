package entidades;

public class EmpleadoContratado extends Empleado {
    private final double valorHora;

    public EmpleadoContratado(int legajo, String nombre, double valorHora) {
        super(legajo, nombre);
        this.valorHora = valorHora;
    }

    public double getValorHora() {
        return valorHora;
    }

    @Override
    public double calcularPago(double horasTrabajadas) {
        return valorHora * horasTrabajadas;
    }

    @Override
    public String toString() {
        return "Contratado - " + super.toString() + " - $/h: " + valorHora;
    }
}

