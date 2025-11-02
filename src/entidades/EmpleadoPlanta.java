package entidades;

public class EmpleadoPlanta extends Empleado {
    private final double valorDia;
    private final String categoria;
    private boolean sinRetrasosPeriodo; // para el +2% si corresponde

    public EmpleadoPlanta(int legajo, String nombre, double valorDia, String categoria) {
        super(legajo, nombre);
        this.valorDia = valorDia;
        this.categoria = categoria;
        this.sinRetrasosPeriodo = true;
    }

    public double getValorDia() {
        return valorDia;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isSinRetrasosPeriodo() {
        return sinRetrasosPeriodo;
    }

    public void setSinRetrasosPeriodo(boolean v) {
        this.sinRetrasosPeriodo = v;
    }

    @Override
    public double calcularPago(double diasTrabajados) {
        double base = valorDia * diasTrabajados;
        if (sinRetrasosPeriodo) {
            base *= 1.02; // +2%
        }
        return base;
    }

    @Override
    public String toString() {
        return "Planta - " + super.toString() + " - $/dia: " + valorDia + " - Cat: " + categoria;
    }
}
