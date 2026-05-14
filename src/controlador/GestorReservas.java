package controlador;

import modelo.Reserva;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Se encarga de gestionar la lista de reservas y su persistencia en ficheros.
 */
public class GestorReservas {
    private List<Reserva> reservas;
    private final String RUTA_ARCHIVO = "reservas.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor que inicializa la lista e intenta cargar el archivo.
     */
    public GestorReservas() {
        reservas = new ArrayList<>();
        cargarDatos();
    }

    /**
     * Añade una reserva comprobando que no haya solapamiento ni códigos repetidos.
     */
    public boolean anadirReserva(Reserva nuevaReserva) {
        if (buscarReserva(nuevaReserva.getCodigo()) != null) {
            System.out.println("Error: Ya existe una reserva con ese código.");
            return false;
        }
        if (comprobarSolapamiento(nuevaReserva.getNumHabitacion(), nuevaReserva.getFechaEntrada(), nuevaReserva.getFechaSalida(), null)) {
            System.out.println("Error: La habitación ya está reservada en esas fechas.");
            return false;
        }
        reservas.add(nuevaReserva);
        return true;
    }

    /**
     * Busca una reserva por su código.
     */
    public Reserva buscarReserva(String codigo) {
        for (Reserva r : reservas) {
            if (r.getCodigo().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Imprime todas las reservas ordenadas.
     */
    public void listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        Collections.sort(reservas);
        for (Reserva r : reservas) {
            System.out.println(r.toString());
            System.out.println("---");
        }
    }

    /**
     * Modifica las fechas de una reserva existente comprobando solapamientos.
     */
    public boolean modificarReserva(String codigo, LocalDate nuevaEntrada, LocalDate nuevaSalida) {
        Reserva r = buscarReserva(codigo);
        if (r == null) {
            return false;
        }
        if (comprobarSolapamiento(r.getNumHabitacion(), nuevaEntrada, nuevaSalida, r.getCodigo())) {
            System.out.println("Error: Las nuevas fechas solapan con otra reserva existente.");
            return false;
        }
        r.setFechaEntrada(nuevaEntrada);
        r.setFechaSalida(nuevaSalida);
        return true;
    }

    /**
     * Elimina una reserva mediante su código.
     */
    public boolean eliminarReserva(String codigo) {
        Reserva r = buscarReserva(codigo);
        if (r != null) {
            reservas.remove(r);
            return true;
        }
        return false;
    }

    /**
     * Guarda la colección de reservas actual en el archivo de texto.
     */
    public void guardarDatos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Reserva r : reservas) {
                bw.write(r.toFileString());
                bw.newLine();
            }
            System.out.println("Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar en el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga las reservas desde el archivo de texto si existe.
     */
    private void cargarDatos() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return; // No hay archivo al inicio 
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    Reserva r = new Reserva(
                            partes[0], partes[1], Integer.parseInt(partes[2]),
                            LocalDate.parse(partes[3], formatter),
                            LocalDate.parse(partes[4], formatter)
                    );
                    reservas.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo. Se iniciará vacío.");
        }
    }

    /**
     * Método auxiliar para evitar que dos reservas ocupen la misma habitación a la vez.
     * @param codigoExcluir Sirve para no comparar una reserva consigo misma al modificarla.
     */
    private boolean comprobarSolapamiento(int habitacion, LocalDate entrada, LocalDate salida, String codigoExcluir) {
        for (Reserva r : reservas) {
            if (r.getNumHabitacion() == habitacion) {
                if (codigoExcluir != null && r.getCodigo().equals(codigoExcluir)) continue;
                
                // Si la nueva entrada es antes de la salida existente Y la nueva salida es después de la entrada existente, hay solape.
                if (entrada.isBefore(r.getFechaSalida()) && salida.isAfter(r.getFechaEntrada())) {
                    return true;
                }
            }
        }
        return false;
    }
}
