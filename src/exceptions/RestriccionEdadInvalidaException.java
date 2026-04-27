package exceptions;

public class RestriccionEdadInvalidaException extends Exception {
    
    public RestriccionEdadInvalidaException() {
        super("La restricción de edad debe ser '-5' o 'Adultos'.");
    }
    
    public RestriccionEdadInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    public RestriccionEdadInvalidaException(String restriccion, String[] permitidas) {
        super("Restricción de edad inválida: '" + restriccion + "'. Debe ser: " + String.join(" o ", permitidas));
    }
}