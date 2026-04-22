package modelo.usuario;

public abstract class Usuario {
	private int id;
	private String login;
	private String password;
	private String nombre;
	
	//Constructor
	public Usuario(int id, String login, String password, String nombre) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
	}
	
	//Getters y Setters
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

	public void setPassword(String nueva) {
		this.password=nueva;
	}
}
