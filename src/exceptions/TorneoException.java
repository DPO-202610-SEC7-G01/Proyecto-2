package exceptions;

public class TorneoException extends Exception {
    
    private String tipoError;
    
    public TorneoException(String mensaje, String tipoError) {
        super(mensaje);
        this.tipoError = tipoError;
    }
    
    public static TorneoException torneoNoEncontrado(String nombre) {
        return new TorneoException("No se encontró el torneo: " + nombre, "NO_ENCONTRADO");
    }
    
    public static TorneoException excesoTorneos(int max) {
        return new TorneoException("Máximo " + max + " torneos por cliente", "EXCESO");
    }
    
    public static TorneoException yaInscrito(String nombre) {
        return new TorneoException("Ya estás inscrito en el torneo: " + nombre, "YA_INSCRITO");
    }
    
    public String getTipoError() {
        return tipoError;
    }
}