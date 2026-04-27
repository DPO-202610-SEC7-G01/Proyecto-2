package exceptions;

public class JuegoNoEncontradoException extends Exception {
    
    public JuegoNoEncontradoException() {
        super("El juego no se encuentra en la lista de ventas.");
    }
    
    public JuegoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public JuegoNoEncontradoException(int idJuego) {
        super("No se encontró el juego con ID " + idJuego + " en la lista de ventas.");
    }
    
    public JuegoNoEncontradoException(String nombreJuego, String lista) {
        super("El juego '" + nombreJuego + "' no se encuentra en la lista de " + lista);
    }
}