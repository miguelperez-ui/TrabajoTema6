package excepciones;

/**
 * Excepción personalizada que se lanza cuando una fecha no cumple 
 * con el formato dd/mm/aaaa.
 */
public class FormatoFechaExcepcion extends Exception {
    
    /**
     * Constructor de la excepción.
     * @param mensaje Mensaje descriptivo del error.
     */
    public FormatoFechaExcepcion(String mensaje) {
        super(mensaje);
    }
}