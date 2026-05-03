package exceptions;

public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = 1L; // NO se pq lo pde pero lo pide
	private String login;
    private boolean userNotFound;
    private String campoInvalido; // Para validaciones de datos
    
    // Constructor para usuario no encontrado
    public InvalidCredentialsException(String login, boolean userNotFound) {
        this.login = login;
        this.userNotFound = userNotFound;
    }
    
    // Constructor para datos inválidos (nombre, login, password)
    public InvalidCredentialsException(String campoInvalido, String mensaje) {
        super(mensaje);
        this.campoInvalido = campoInvalido;
    }
    
    @Override
    public String getMessage() {
        if (userNotFound) {
            return "El usuario '" + login + "' no existe.";
        } else if (campoInvalido != null) {
            return super.getMessage();
        } else {
            return "Contraseña incorrecta para el usuario: " + login;
        }
    }
    
    public String getLogin() {
        return login;
    }
    
    public boolean isUserNotFound() {
        return userNotFound;
    }
    
    public String getCampoInvalido() {
        return campoInvalido;
    }
}