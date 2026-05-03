package modelo.usuario;

//Exceptions
import exceptions.InvalidCredentialsException;

public abstract class Usuario {
	private int id;
	private String login;
	private String password;
	private String nombre;
	
	//Constructor con Validaciones
    public Usuario(int id, String login, String password, String nombre) throws InvalidCredentialsException {
        this.id = id; 
        
        if (login == null || login.trim().isEmpty()) {  //Exceptions de login
            throw new InvalidCredentialsException("login", "El login no puede estar vacío.");
        }
        if (!login.matches("[a-zA-Z0-9]+")) {
            throw new InvalidCredentialsException("login", "El login solo puede contener letras y números, sin espacios.");
        }
        this.login = login;
        
 
        if (nombre == null || nombre.trim().isEmpty()) { //Exceptions de nombre
            throw new InvalidCredentialsException("nombre", "El nombre no puede estar vacío.");
        }
        if (nombre.matches(".*\\d.*")) {
            throw new InvalidCredentialsException("nombre", "El nombre no puede contener números.");
        }
        this.nombre = nombre;
    }
	
	//Getters y Setters
    public void setPassword(String nueva) throws InvalidCredentialsException {
        if (nueva == null || nueva.trim().isEmpty()) {
            throw new InvalidCredentialsException("password", "La nueva contraseña no puede estar vacía.");
        }
        this.password = nueva.trim();
    }
	
	public int getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public String getNombre() {
		return nombre;
	}

	
	
	
}
