package consola.interfaz;

import java.util.Scanner;

//Exceptiones
import exceptions.FileNotFoundException;
import exceptions.UserNotFoundException;
import java.io.IOException;


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

	//Consolas 
	private ConsolaAdministrador consolaAdmin;
	private ConsolaCliente consolaCliente;
	private ConsolaEmpleado consolaEmpleado;
	
	//Objetos Constantes
	static private Cafe miCafe;
	static int opcion = 0;
	Scanner aleatorio;

	
	public void NuevoCafe() throws IOException, FileNotFoundException { // Método de Carga
		miCafe = new Cafe(0); 
		
		
		 miCafe.descargarDatos(
		            "data/juegosPrestamo.json",  
		            "data/juegosVenta.json",     
		            "data/juegosDificiles",
		            "data/bebidas.json",           
		            "data/platillos.json",        
		            "data/administrador.json", 
		            "data/cocineros.json",
		            "data/meseros.json",
		            "data/clientes.json",
		            "data/reservas.json",         
		            "data/historialPrestamos.json", 
		            "data/sugerenciasPendientes.json",
		            "data/transacciones.json" ,
		            "data/mesas.json"
		        );
		this.consolaAdmin = new ConsolaAdministrador(miCafe);
				
	}
	
	public void registrarUsuarioNuevo() throws UserNotFoundException {
		System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
		System.out.println("1. Cliente | 2. Mesero | 3. Cocinero | 4. Admin ");
		System.out.print("Seleccione: ");
		int tipo = lector.nextInt();
		lector.nextLine();

		System.out.print("Nombre completo: ");
		String nombre = lector.nextLine();
		int id = aleatorio.nextInt(1001);
		String loginBase = nombre.split(" ")[0].toLowerCase() + id;
		
		
		while (buscarUsuario(loginBase) != null) {
			id = aleatorio.nextInt(1001);
			loginBase = nombre.split(" ")[0].toLowerCase() + id;
		}
		
		final String login = loginBase; 
		System.out.print("Ingrese Password: ");
		String password = lector.nextLine();
		
		switch (tipo) {
		case 1:
			consolaCliente.registrarCliente(id,nombre,login,password,miCafe);
			break;
		case 2:
			consolaEmpleado.registrarMesero(id,nombre,login,password,miCafe);
			break;
		case 3:
			consolaEmpleado.registrarCocinero(id,nombre,login,password,miCafe);
			break;
		case 4:
			consolaAdmin.registrarAdmin();
			break;

		default:
			System.out.println("Opción inválida.");
			return;
		}

		System.out.println("Registro exitoso con el login: " + login);
	}
	
	
	private Usuario buscarUsuario(String login) throws UserNotFoundException {
		if (miCafe.getAdmin().getLogin().contains(login)) {
			return miCafe.getAdmin();
		}
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
		System.out.print("Ingrese la nueva contraseña: ");
		String nuevaPass = lector.nextLine();

		usuarioEncontrado.setPassword(nuevaPass);
	}

	public Usuario verificarUsuario() throws UserNotFoundException {
		System.out.print("Ingrese su login de usuario: ");
		String loginBusqueda = lector.nextLine();

		Usuario usuarioEncontrado = buscarUsuario(loginBusqueda);
		System.out.print("Ingrese contraseña: ");
		String contraseña = lector.nextLine();
		if (contraseña.equals(usuarioEncontrado.getPassword())) {
			return usuarioEncontrado;
		}
		
		throw new UserNotFoundException("Usuario con login '" + loginBusqueda + "' no encontrado");
	}

	public static void main(String[] args) throws IOException, FileNotFoundException {
		ConsolaGeneral consola = new ConsolaGeneral();
		ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(miCafe);
		ConsolaEmpleado consolaEmpleado = new ConsolaEmpleado(miCafe);
		ConsolaCliente consolaCliente = new ConsolaCliente(miCafe);
		Scanner lectorMenu = new Scanner(System.in);
		int opcion = 0;
		
		consola.NuevoCafe(); 

		System.out.println("BIENVENIDO A DULCES N DADOS ");
		
		if (miCafe.getAdmin()== null ) { //Registrar un nuevo admin si no hay uno en el café 
			consolaAdmin.registrarAdmin();
		}
		
		do {
			System.out.println("\n--- MENÚ PRINCIPAL ---");
			System.out.println("0.  Registrarse Primera Vez ");
			System.out.println("1. Cambiar Contraseña");
			System.out.println("2. Opciones de Administrador");
			System.out.println("3. Opciones de Empleado");
			System.out.println("4. Opciones de Cliente");
			System.out.println("5. Salir");
			System.out.print("Seleccione una opción: ");

			try {
				opcion = lectorMenu.nextInt();
				lectorMenu.nextLine();

				switch (opcion) {
				case 0:
					consola.registrarUsuarioNuevo();
					break;
				case 1:
					consola.cambioContraseña();
					return;
				case 2:
					Usuario admin =consola.verificarUsuario();
					if(admin instanceof Administrador) {
						 ConsolaAdministrador.main(miCafe);
					}
					return;
				case 3:
					Usuario empleado=consola.verificarUsuario();
					if(empleado instanceof Empleado) {
						//
					}
					return;
				case 4:
					Usuario cliente=consola.verificarUsuario();
					if(cliente instanceof Administrador) {
						//
					}
					return;
				case 5:
					System.out.println("Saliendo del sistema... ¡Hasta luego!");
					return;
				}
			} catch (Exception e) {
				System.out.println(" Ingrese un número válido.");
				lectorMenu.nextLine();
				opcion = 0;
			}

		} while (opcion != 1);

		lectorMenu.close();
	}
		
}