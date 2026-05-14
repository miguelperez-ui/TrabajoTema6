package vista;

import controlador.GestorReservas;
import excepciones.FormatoFechaExcepcion;
import modelo.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        GestorReservas gestor = new GestorReservas();
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Añadir reserva ");
            System.out.println("2. Buscar reserva ");
            System.out.println("3. Listar reservas ");
            System.out.println("4. Modificar reserva ");
            System.out.println("5. Eliminar reserva ");
            System.out.println("6. Guardar ");
            System.out.println("0. Salir ");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduzca un número.");
                continue;
            }

            switch (opcion) {
                case 1: 
                    try {
                        System.out.print("Código de reserva: ");
                        String cod = scanner.nextLine();
                        System.out.print("Nombre del cliente: ");
                        String nom = scanner.nextLine();
                        System.out.print("Nº de habitación: ");
                        int hab = Integer.parseInt(scanner.nextLine());
                        
                        LocalDate fEntrada = leerFecha(scanner, "Fecha de entrada (dd/mm/aaaa): ");
                        LocalDate fSalida = leerFecha(scanner, "Fecha de salida (dd/mm/aaaa): ");

                        if (fEntrada.isAfter(fSalida) || fEntrada.isEqual(fSalida)) {
                            System.out.println("La fecha de salida debe ser posterior a la de entrada.");
                            break;
                        }

                        Reserva r = new Reserva(cod, nom, hab, fEntrada, fSalida);
                        if (gestor.anadirReserva(r)) {
                            System.out.println("Reserva añadida con éxito.");
                        }
                    } catch (FormatoFechaExcepcion e) {
                        System.out.println(e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: El número de habitación debe ser un número entero.");
                    }
                    break;

                case 2: // Buscar reserva 
                    System.out.print("Introduzca código de la reserva a buscar: ");
                    String codBuscar = scanner.nextLine();
                    Reserva encontrada = gestor.buscarReserva(codBuscar);
                    if (encontrada != null) {
                        System.out.println(encontrada.toString());
                    } else {
                        System.out.println("Reserva no encontrada.");
                    }
                    break;

                case 3: // Listar reservas 
                    gestor.listarReservas();
                    break;

                case 4: // Modificar reserva 
                    System.out.print("Introduzca código de la reserva a modificar: ");
                    String codMod = scanner.nextLine();
                    Reserva rMod = gestor.buscarReserva(codMod);
                    if (rMod == null) {
                        System.out.println("La reserva indicada no existe.");
                    } else {
                        try {
                            LocalDate nuevaEntrada = leerFecha(scanner, "Nueva fecha de entrada (dd/mm/aaaa):  ");
                            LocalDate nuevaSalida = leerFecha(scanner, "Nueva fecha de salida (dd/mm/aaaa):  ");
                            
                            if (nuevaEntrada.isAfter(nuevaSalida) || nuevaEntrada.isEqual(nuevaSalida)) {
                                System.out.println("La fecha de salida debe ser posterior a la de entrada.");
                                break;
                            }

                            if(gestor.modificarReserva(codMod, nuevaEntrada, nuevaSalida)) {
                                System.out.println("Reserva modificada con éxito.");
                            }
                        } catch (FormatoFechaExcepcion e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;

                case 5: // Eliminar reserva 
                    System.out.print("Introduzca código de la reserva a eliminar: ");
                    String codDel = scanner.nextLine();
                    if (gestor.eliminarReserva(codDel)) {
                        System.out.println("Reserva eliminada con éxito.");
                    } else {
                        System.out.println("No se encontró la reserva para eliminar.");
                    }
                    break;

                case 6: // Guardar 
                    gestor.guardarDatos();
                    break;

                case 0: // Salir 
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    /**
     * Método auxiliar para pedir y validar la fecha.
     */
    private static LocalDate leerFecha(Scanner scanner, String mensaje) throws FormatoFechaExcepcion {
        System.out.print(mensaje);
        String fechaStr = scanner.nextLine();
        
        // Expresión regular para obligar a que se escriba exactamente dd/mm/aaaa
        if (!Pattern.matches("\\d{2}/\\d{2}/\\d{4}", fechaStr)) {
            throw new FormatoFechaExcepcion("Excepción lanzada: El formato de la fecha no es válido. Debe ser dd/mm/aaaa. ");
        }
        
        try {
            return LocalDate.parse(fechaStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new FormatoFechaExcepcion("Excepción lanzada: Fecha inexistente o incorrecta. ");
        }
    }
}
