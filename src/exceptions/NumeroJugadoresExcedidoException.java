package exceptions;

public class NumeroJugadoresExcedidoException extends Exception {
    
    public NumeroJugadoresExcedidoException() {
        super("El número de jugadores debe estar entre 1 y 40.");
    }
    
    public NumeroJugadoresExcedidoException(String mensaje) {
        super(mensaje);
    }
    
    public NumeroJugadoresExcedidoException(int numJugadores) {
        super("Número de jugadores inválido: " + numJugadores + ". Debe estar entre 1 y 40.");
    }
    
    public NumeroJugadoresExcedidoException(int numJugadores, int min, int max) {
        super("Número de jugadores inválido: " + numJugadores + ". Debe estar entre " + min + " y " + max + ".");
    }
}