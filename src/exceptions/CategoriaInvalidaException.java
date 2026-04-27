package exceptions;

public class CategoriaInvalidaException extends Exception {
    
    private static final String[] CATEGORIAS_PERMITIDAS = {"Tablero", "Cartas", "Acción"};
    
    public CategoriaInvalidaException() {
        super("La categoría debe ser: Tablero, Cartas o Acción.");
    }
    
    public CategoriaInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    public CategoriaInvalidaException(String categoria, String[] permitidas) {
        super("Categoría inválida: '" + categoria + "'. Debe ser: " + String.join(", ", permitidas));
    }
    
    public static String[] getCategoriasPermitidas() {
        return CATEGORIAS_PERMITIDAS.clone();
    }
}