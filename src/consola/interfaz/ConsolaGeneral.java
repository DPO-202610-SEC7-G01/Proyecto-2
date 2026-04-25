package consola.interfaz;

import java.util.Scanner;

import exceptions.FileNotFoundException;
import persistencia.PersistenciaCafeJson;
import persistencia.PersistenciaOperacionesJson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import modelo.*;
import modelo.usuario.*;
import modelo.producto.*;


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
	
	
	public static void main(String[] args) {
		ConsolaGeneral consola = new ConsolaGeneral();
		ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(miCafe);
	
		consola.NuevoCafe(); 

		System.out.println("BIENVENIDO A DULCES N DADOS ");
		
		if (miCafe.getAdmin()== null ) { //Registrar un nuevo admin si no hay uno en el café 
			consolaAdmin.registrarAdmin();
		}
		
		
		
		
	}
}