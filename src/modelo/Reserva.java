package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Representa una reserva del hotel.
 */
public class Reserva implements Comparable<Reserva> {
    private String codigo;
    private String nombreCliente;
    private int numHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    /**
     * Constructor de la clase Reserva.
     * @param codigo Código único de reserva.
     * @param nombreCliente Nombre del cliente.
     * @param numHabitacion Número de la habitación.
     * @param fechaEntrada Fecha de entrada.
     * @param fechaSalida Fecha de salida.
     */
    public Reserva(String codigo, String nombreCliente, int numHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.numHabitacion = numHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    // Getters necesarios 
    public String getCodigo() { return codigo; }
    public String getNombreCliente() { return nombreCliente; }
    public int getNumHabitacion() { return numHabitacion; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }

    // Setters necesarios para modificar la reserva 
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    /**
     * Compara dos reservas para saber si son iguales en base a su código.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(codigo, reserva.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    /**
     * Ordena las reservas por número de habitación y, en caso de empate, por fecha de entrada.
     */
    @Override
    public int compareTo(Reserva o) {
        if (this.numHabitacion != o.numHabitacion) {
            return Integer.compare(this.numHabitacion, o.numHabitacion);
        }
        return this.fechaEntrada.compareTo(o.fechaEntrada);
    }

    /**
     * Formatea la reserva en dos líneas según el requerimiento.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return codigo + " " + nombreCliente + "\n" +
               numHabitacion + " " + fechaEntrada.format(formatter) + " " + fechaSalida.format(formatter);
    }

    /**
     * Devuelve la representación de la reserva para ser guardada en fichero.
     * @return String con formato código;nombre;n_habitacion;fecha_entrada;fecha_salida
     */
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return codigo + ";" + nombreCliente + ";" + numHabitacion + ";" + 
               fechaEntrada.format(formatter) + ";" + fechaSalida.format(formatter);
    }
}