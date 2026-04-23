package consola.interfaz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import modelo.producto.Juego;
import modelo.producto.JuegoDificil;
import modelo.producto.Platillo;

public class ConsolaAdministrador {
	
	public boolean validarAdmin() {
	    Scanner sc = new Scanner(System.in);
	    
	    System.out.println("--- Autenticación de Administrador ---");
	    System.out.print("Login: ");
	    String loginIngresado = sc.nextLine();
	    
	    System.out.print("Contraseña: ");
	    String passwordIngresada = sc.nextLine();

	    // Comparamos con el administrador guardado en el Café
	    if (miCafe.getAdmin().getLogin().equals(loginIngresado) && 
	        miCafe.getAdmin().getPassword().equals(passwordIngresada)) {
	        return true;
	    } else {
	        System.out.println("Error: Credenciales incorrectas.");
	        return false;
	    }
	}
	
	public void registrarNuevoJuego() {

	    // Si llegamos aquí, es porque el admin es válido
	    Scanner sc = new Scanner(System.in);
	    System.out.println("\n--- Registro de Nuevo Juego ---");

	    System.out.print("ID: "); int id = sc.nextInt();
	    System.out.print("Precio: "); int precio = sc.nextInt();
	    sc.nextLine(); // Limpiar buffer
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
	
	public void aceptarPlatillo() {
	    // 1. Reutilizamos la función de validación que separamos antes
	    if (!validarAdmin()) {
	        return; 
	    }

	    Scanner sc = new Scanner(System.in);
	    List<Platillo> sugerencias = miCafe.getSugerenciasPendientes();

	    // 2. Verificamos si hay platillos por revisar
	    if (sugerencias == null || sugerencias.isEmpty()) {
	        System.out.println("No hay sugerencias de platillos pendientes por revisar.");
	        return;
	    }

	    System.out.println("\n--- Revisión de Sugerencias de Platillos ---");
	    
	    // 3. Recorremos una copia de la lista para evitar errores al remover elementos
	    ArrayList<Platillo> copiaSugerencias = new ArrayList<>(sugerencias);

	    for (Platillo p : copiaSugerencias) {
	        System.out.println("\nPlatillo: " + p.getNombre());
	        System.out.println("Precio sugerido: $" + p.getPrecio());
	        System.out.println("Categoría: " + p.getAlergeneos());
	        
	        System.out.print("¿Qué desea hacer? (1. Aceptar / 2. Rechazar / 3. Omitir por ahora): ");
	        int decision = sc.nextInt();

	        if (decision == 1) {
	            miCafe.getAdmin().incluirSugerencia(p);
	            System.out.println("El platillo '" + p.getNombre() + "' ha sido agregado al menú.");
	        } 
	        else if (decision == 2) {
	            miCafe.getAdmin().excluirSugerencia(p);
	            System.out.println("El platillo '" + p.getNombre() + "' ha sido rechazado y eliminado.");
	        } 
	        else {
	            System.out.println("Se ha saltado la revisión de este platillo.");
	        }
	    }
	    
	    System.out.println("\n--- Fin de la revisión de sugerencias ---");
	}
	
	
	public void verFinanzas() {
	    // 1. Reutilizamos la validación de seguridad
	    if (!validarAdmin()) {
	        return; 
	    }

	    Scanner sc = new Scanner(System.in);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
	    System.out.println("\n--- Reporte Financiero de Transacciones ---");
	    
	    try {
	        // 2. Pedir y parsear la Fecha Inicial
	        System.out.print("Ingrese fecha inicial (dd/mm/aaaa): ");
	        String inicioStr = sc.nextLine();
	        Calendar fecha1 = Calendar.getInstance();
	        fecha1.setTime(sdf.parse(inicioStr));
	        // Ajustamos a inicio del día (00:00:00)
	        fecha1.set(Calendar.HOUR_OF_DAY, 0);
	        fecha1.set(Calendar.MINUTE, 0);

	        // 3. Pedir y parsear la Fecha Final
	        System.out.print("Ingrese fecha final (dd/mm/aaaa): ");
	        String finStr = sc.nextLine();
	        Calendar fecha2 = Calendar.getInstance();
	        fecha2.setTime(sdf.parse(finStr));
	        // Ajustamos a fin del día (23:59:59)
	        fecha2.set(Calendar.HOUR_OF_DAY, 23);
	        fecha2.set(Calendar.MINUTE, 59);

	        // 4. Validar orden de fechas
	        if (fecha1.after(fecha2)) {
	            System.out.println("Error: La fecha inicial no puede ser posterior a la final.");
	            return;
	        }

	        // 5. Llamar al método del administrador y mostrar el reporte
	        System.out.println("\nGenerando reporte...");
	        String reporte = miCafe.getAdmin().verFinanzas(fecha1, fecha2);
	        
	        System.out.println(reporte);

	    } catch (ParseException e) {
	        System.out.println("Error: Formato de fecha inválido. Use dd/mm/aaaa (ej: 07/04/2026).");
	    }
	}
	
}
