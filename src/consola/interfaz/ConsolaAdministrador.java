package consola.interfaz;

import java.util.Scanner;

import modelo.*;
import modelo.usuario.*;
import modelo.producto.*;

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
	
	
	public void registrarNuevoJuego() { //Acá hace falta mirar la persistencia para cargarlo 
	    Scanner sc = new Scanner(System.in);
	    System.out.println("\n--- Registro de Nuevo Juego ---");

	    System.out.print("ID: "); int id = sc.nextInt();
	    System.out.print("Precio: "); int precio = sc.nextInt();
	    sc.nextLine(); 
	    System.out.print("Nombre: "); String nombre = sc.nextLine();
	    System.out.print("Año: "); int anio = sc.nextInt();
	    sc.nextLine(); 
	    System.out.print("Empresa Matriz: "); String empresa = sc.nextLine();
	    System.out.print("Num. Jugadores: "); int numJug = sc.nextInt();
	    sc.nextLine();
	    System.out.print("Restricción Edad: "); String edad = sc.nextLine();
	    System.out.print("Categoría: "); String cat = sc.nextLine();

	    System.out.print("¿Es un juego difícil? (si/no): ");
	    String esDificil = sc.nextLine().toLowerCase();

	    Juego nuevoJuego;
	    if (esDificil.equals("si")) {
	        System.out.print("Ingrese Instrucciones Especiales: ");
	        String instrucciones = sc.nextLine();
	        nuevoJuego = new JuegoDificil(id, precio, nombre, anio, empresa, numJug, edad, cat, instrucciones);
	    } else {
	        nuevoJuego = new Juego(id, precio, nombre, anio, empresa, numJug, edad, cat);
	    }

	    System.out.print("¿Destino? (1. Préstamo / 2. Venta): ");
	    int tipo = sc.nextInt();

	    if (tipo == 1) {
	        miCafe.getJuegosPrestamo().add(nuevoJuego);
	        System.out.println("Juego añadido a PRÉSTAMO.");
	    } else if (tipo == 2) {
	        miCafe.getJuegosVenta().add(nuevoJuego);
	        System.out.println("Juego añadido a VENTA.");
	    }
	}
	
	
	public static void main(Cafe miCafe) {
		Scanner lectorMenu = new Scanner(System.in);
		ConsolaAdministrador consola = new ConsolaAdministrador(miCafe);

		int opcion = 0;
		
		do {
			System.out.println("\n--- Bienvenido Administrador ---");
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
					consola.registrarNuevoJuego();
					break;
				case 1:
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
