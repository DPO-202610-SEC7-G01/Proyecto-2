package consola.interfaz;

import java.util.Scanner;

//Exceptiones
import exceptions.FileNotFoundException;
import exceptions.UserNotFoundException;
import java.io.IOException;

//Persistencias
import persistencia.PersistenciaCafeJson;
import persistencia.PersistenciaOperacionesJson;

//módulos de lógica
import modelo.*;
import modelo.usuario.*;


//Perdón amigos si hay muchos comentarios sino que me parece
// importante  tener como muy claro u.u 
//Luego si algo lo borraré 

public class ConsolaGeneral {
	//Otros
	private static Scanner lector; //Objeto scanner para leer entradas

	//Persistencia
	private PersistenciaCafeJson persistenciaCafe;
	private PersistenciaOperacionesJson persistenciaOps;
	
	//Consolas 
	private ConsolaAdministrador consolaAdmin;
	
	//Objetos Constantes
	static private Cafe miCafe;
	static int opcion = 0;
	Scanner aleatorio;

	
	public void NuevoCafe() throws IOException, FileNotFoundException { // Método de Carga
		miCafe = new Cafe(50); // Un café con 50 de capacidad
		this.persistenciaCafe = new PersistenciaCafeJson(); // Guarda la información mas fija del café 
		// como lo son las mesas, meseros, etc 
		this.persistenciaOps = new PersistenciaOperacionesJson(); // Guarda la información dinámica del café
		// Historiales de reversa, transacciones, etc
		
		 miCafe.descargarDatos(
		            "data/juegosPrestamo.json",  
		            "data/juegosVenta.json",     
		            "data/juegosDificiles",
		            "data/bebidas.json",           
		            "data/platillos.json",        
		            "data/administrador.json", 
		            "data/empleados.json",
		            "data/clientes.json",
		            "data/reservas.json",         
		            "data/historialPrestamos.json", 
		            "data/transacciones.json" ,
		            "data/mesas.json"
		        );
		this.consolaAdmin = new ConsolaAdministrador(miCafe);
				
	}
	
	private Usuario buscarUsuario(String login) throws UserNotFoundException {
	    for (Cliente c : miCafe.getClientes()) {
	        if (c.getLogin().equals(login))
	            return c;
	    }
	    for (Empleado e : miCafe.getEmpleados()) {
	        if (e.getLogin().equals(login))
	            return e;
	    }
	    throw new UserNotFoundException("Usuario con login '" + login + "' no encontrado");
	}

	public void cambioContraseña() throws UserNotFoundException {
		System.out.println("\n--- CAMBIO DE CONTRASEÑA ---");
		System.out.print("Ingrese su login de usuario: ");
		String loginBusqueda = lector.nextLine();

		Usuario usuarioEncontrado = buscarUsuario(loginBusqueda);

		if (usuarioEncontrado != null) {
			System.out.print("Ingrese la nueva contraseña: ");
			String nuevaPass = lector.nextLine();

			usuarioEncontrado.setPassword(nuevaPass);

			System.out.println("Contraseña actualizada para el usuario: " + usuarioEncontrado.getNombre());
		} else {
			System.out.println("Error: No se encontró ningún usuario con el login: " + loginBusqueda);
		}
	}

	
	public static void main(String[] args) throws IOException, FileNotFoundException {
		ConsolaGeneral consola = new ConsolaGeneral();
		ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(miCafe);
	
		consola.NuevoCafe(); 

		System.out.println("BIENVENIDO A DULCES N DADOS ");
		
		if (miCafe.getAdmin()== null ) { //Registrar un nuevo admin si no hay uno en el café 
			consolaAdmin.registrarAdmin();
		}
	}
}