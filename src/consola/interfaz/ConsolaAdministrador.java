package consola.interfaz;

import java.util.Scanner;

import modelo.Cafe;
import modelo.usuario.Administrador;

public class ConsolaAdministrador {
	
	//Objetos Constantes
	static private Cafe miCafe;
	private static Scanner lector = new Scanner(System.in);//Objeto scanner para leer entradas
	
	
	 public ConsolaAdministrador(Cafe cafe) {
	        ConsolaAdministrador.miCafe = cafe;
	    }
	  
	public  void registrarAdmin() {
		System.out.println("Bienvenido Nuevo Administrador\n");
		System.out.print("Ingrese su Nombre completo: ");
		String nombre = lector.nextLine();
		String login = nombre.split(" ")[0].toLowerCase() + 00;

		System.out.print("Ingrese una Contraseña: ");
		String password = lector.nextLine();
			
		miCafe.cambiarAdmin(new Administrador(00, login, password, nombre, miCafe)); 
		System.out.println("Registro exitoso para " + nombre + "con el login: " + login +"\n");
	}
	
	
	public static void main(String[] args) {
		Scanner lectorMenu = new Scanner(System.in);
		int opcion = 0;
		
		//Quiero sacar un menu solo para los admins
	}
}
