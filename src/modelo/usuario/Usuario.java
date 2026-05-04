package modelo.usuario;

//Exceptions
import exceptions.*;

public abstract class Usuario {
	private int id;
	private String login;
	private String password;
	private String nombre;

	//Constructor con Validaciones
    public Usuario(int id, String login, String password, String nombre) throws UsuariosException {
    	if (id < 0) {
    		 throw new UsuariosException(this, "id", "El ID no puede ser negativo. Valor recibido: " + id);
    	}
  
    	this.id = (id);
    	
        if (login == null || login.trim().isEmpty()) {  //Exceptions de login
            throw new UsuariosException(this, "login", "El login no puede estar vacío.");
        }
        if (!login.matches("[a-zA-Z0-9]+")) {
        	throw new UsuariosException(this, "login", 
                    "El login solo puede contener letras y números, sin espacios. Valor recibido: '" + login + "'");
        }
        this.login = login;
        
        if (password == null || password.trim().isEmpty()) { ///
            throw new UsuariosException(this, "password", "La contraseña no puede estar vacía.");
        }
        
        this.password = password;	
 
        if (nombre == null || nombre.trim().isEmpty()) { //Exceptions de nombre
            throw new UsuariosException(this, "nombre", "El nombre no puede estar vacío.");
        }
        if (nombre.matches(".*\\d.*")) {
        	throw new UsuariosException(this, "nombre", 
                    "El nombre no puede contener números. Valor recibido: '" + nombre + "'");
        }
        this.nombre = nombre;
    }
	
	//Getters y Setters
    public void setPassword(String nueva) throws UsuariosException {
        if (nueva == null || nueva.trim().isEmpty()) {
        	 throw new UsuariosException(this, "password", "La nueva contraseña no puede estar vacía.");
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
