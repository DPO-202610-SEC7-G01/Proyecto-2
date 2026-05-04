package consola.interfaz;

//utils
import java.util.Scanner;
import org.json.JSONException;


//Exceptiones
import exceptions.*;
import java.io.FileNotFoundException;
import java.io.IOException;


//módulos de lógica
import modelo.*;
import modelo.usuario.*;


public class ConsolaGeneral {
	//Objetos Constantes
	static private Cafe miCafe;

	public void NuevoCafe() throws IOException, FileNotFoundException, JSONException,  InvalidCredentialsException, ProductosException
	{ // Método de Carga
		miCafe = new Cafe(0); 
		
		
		 miCafe.descargarDatos(
		            "data/juegosPrestamo.json",  
		            "data/juegosVenta.json",     
		            "data/juegosDificiles.json",
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
		            "data/mesas.json",
		            "data/turnos.json"
		        );
	}

	public static void main(String[] args) throws IOException, FileNotFoundException, JSONException, InvalidCredentialsException, ProductosException {
		ConsolaGeneral consola = new ConsolaGeneral();
		ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(miCafe);

		Scanner lectorMenu = new Scanner(System.in);
		int opcion = 0;
		
		consola.NuevoCafe(); 

		System.out.println("BIENVENIDO A DULCES N DADOS ");
		
		if (miCafe.getAdmin()== null ) { //Registrar un nuevo admin si no hay uno en el café 
			consolaAdmin.registrarUsuarioNuevo();
		}
		
		do {
			System.out.println("\n--- MENÚ PRINCIPAL ---");

			System.out.println("1. Opciones de Administrador");
			System.out.println("2. Opciones de Empleado");
			System.out.println("3. Opciones de Cliente");
			System.out.println("4. Salir");
			System.out.print("Seleccione una opción: ");

			try {
				opcion = lectorMenu.nextInt();
				lectorMenu.nextLine();
				switch (opcion) {
				case 1:
					ConsolaAdministrador.main(miCafe);
					return;
				case 2:
					ConsolaEmpleado.main(miCafe);
					return;
				case 3:
					ConsolaCliente.main(miCafe);
					return;
				case 4:
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