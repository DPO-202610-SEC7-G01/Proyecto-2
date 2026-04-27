package exceptions;

public class JuegoNoAptoException extends Exception {
    
    public static final String RAZON_EDAD = "EDAD";
    public static final String RAZON_JUGADORES = "JUGADORES";
    
    private String razon;
    private int edadRequerida;
    private int edadActual;
    private int jugadoresRequeridos;
    private int jugadoresActuales;
    
    public JuegoNoAptoException(String razon) {
        super("El juego no es apto para esta reserva. Razón: " + razon);
        this.razon = razon;
    }
    
    public JuegoNoAptoException(String mensaje, String razon) {
        super(mensaje);
        this.razon = razon;
    }
    
    public JuegoNoAptoException(int edadMinima, int edadCliente) {
        super("El juego requiere edad mínima de " + edadMinima + " años, pero el cliente tiene " + edadCliente + " años.");
        this.razon = RAZON_EDAD;
        this.edadRequerida = edadMinima;
        this.edadActual = edadCliente;
    }
    
    public JuegoNoAptoException(int jugadoresNecesarios, int jugadoresDisponibles, boolean esPorJugadores) {
        super("El juego requiere " + jugadoresNecesarios + " jugadores, pero la reserva es para " + jugadoresDisponibles + " personas.");
        this.razon = RAZON_JUGADORES;
        this.jugadoresRequeridos = jugadoresNecesarios;
        this.jugadoresActuales = jugadoresDisponibles;
    }
    
    public String getRazon() {
        return razon;
    }
    
    public int getEdadRequerida() {
        return edadRequerida;
    }
    
    public int getEdadActual() {
        return edadActual;
    }
    
    public int getJugadoresRequeridos() {
        return jugadoresRequeridos;
    }
    
    public int getJugadoresActuales() {
        return jugadoresActuales;
    }
    
    public boolean esPorEdad() {
        return RAZON_EDAD.equals(razon);
    }
    
    public boolean esPorJugadores() {
        return RAZON_JUGADORES.equals(razon);
    }
}