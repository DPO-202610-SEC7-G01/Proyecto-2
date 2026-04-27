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
	
	public void agregarTurno() {
		System.out.println("\n--- AGREGAR TURNO A EMPLEADO ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

		Empleado empleadoActivo = autenticarEmpleado();
		if (empleadoActivo == null) {
			return;
		}
		Scanner sc = new Scanner(System.in);
		System.out.print("Ingrese la fecha (dd/MM/yyyy): ");
		String input = sc.nextLine();
		// Parsear a LocalDate
		LocalDate fecha = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// Convertir a Calendar
		Calendar cal = Calendar.getInstance();
		cal.set(fecha.getYear(), fecha.getMonthValue() - 1, fecha.getDayOfMonth());
		// Evita duplicar el turno
		if (tieneTurno(empleadoActivo.getTurno(), cal)) {
			System.out.println("El empleado ya tiene este turno asignado.");
			return;
		} else {
			empleadoActivo.agregarTurno(cal);
			System.out.println("Turno asignado con exito.");
		}
	}
	
	public void gestionarTurno(Scanner lectorMenu) {
		System.out.println("0. Consultar turno de empleado.");
		System.out.println("1. Agregar turno de empleado.");
		System.out.println("2. Solicitar cambio de turno de empleado.");
		System.out.println("Ingrese la opcion deseada: ");
		try {
			opcion = lectorMenu.nextInt();
			lectorMenu.nextLine();
			switch (opcion) {
			case 0:
				consultarTurno();
				return;

			case 1:
				agregarTurno();
				return;
			case 2:
				cambiarTurno();
				return;
			}
		} catch (Exception e) {
			System.out.println(" Error: Por favor ingrese un número válido.");
			lectorMenu.nextLine();
			opcion = 0;
			return;
		}
		return;
	}
	public void cambiarTurno() {
		System.out.println("\n--- CAMBIAR TURNO DE EMPLEADO ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

		Empleado empleadoActivo = autenticarEmpleado();
		if (empleadoActivo == null) {
			return;
		}
		Scanner sc = new Scanner(System.in);
		System.out.print("Ingrese la fecha del turno(dd/MM/yyyy): ");
		String input = sc.nextLine();
		// Parsear a LocalDate
		LocalDate fecha = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// Convertir a Calendar
		Calendar cal = Calendar.getInstance();
		cal.set(fecha.getYear(), fecha.getMonthValue() - 1, fecha.getDayOfMonth());
		System.out.print("Ingrese la fecha del turno(dd/MM/yyyy): ");
		String input2 = sc.nextLine();
		// Parsear a LocalDate
		LocalDate fechaNuevo = LocalDate.parse(input2, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// Convertir a Calendar
		Calendar nuevoTurno = Calendar.getInstance();
		nuevoTurno.set(fechaNuevo.getYear(), fechaNuevo.getMonthValue() - 1, fechaNuevo.getDayOfMonth());

		if (tieneTurno(empleadoActivo.getTurno(), cal)) {
		    boolean cambio = empleadoActivo.pedirCambioTurno(
		        miCafe.getAdmin(), cal, nuevoTurno, empleadoActivo
		    );

		    if (cambio) {
		        System.out.println("Turno cambiado con exito.");
		    } else {
		        System.out.println("Cambio de turno denegado.");
		    }
		} else {
		    System.out.println("El empleado no tiene asignado ese turno.");
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
	
	// --- MÉTODOS DE APOYO PARA MANTENER EL CÓDIGO LIMPIO ---

		private void modificarJuego(Scanner sc) {
		    System.out.print("Ingrese el ID del juego a modificar: ");
		    int idBusqueda = sc.nextInt();
		    sc.nextLine();

		    // Buscamos en ambas listas
		    Juego juegoAEditar = buscarJuegoPorId(idBusqueda);

		    if (juegoAEditar != null) {
		        System.out.println("Modificando: " + juegoAEditar.getNombre());
		        System.out.print("Nuevo Precio (actual: " + juegoAEditar.getPrecio() + "): ");
		        juegoAEditar.setPrecio(sc.nextInt());
		        sc.nextLine();
		        System.out.print("Nueva Categoría (actual: " + juegoAEditar.getCategoria() + "): ");
		        juegoAEditar.setCategoria(sc.nextLine());
		        System.out.println("Parámetros actualizados con éxito.");
		    } else {
		        System.out.println("Juego no encontrado.");
		    }
		}
		
		private void moverInventario(Scanner sc) {
		    System.out.print("Ingrese el ID del juego para mover de VENTA a PRÉSTAMO: ");
		    int idMover = sc.nextInt();
		    sc.nextLine();

		    Juego juegoAMover = null;
		    for (Juego j : miCafe.getJuegosVenta()) {
		        if (j.getId() == idMover) {
		            juegoAMover = j;
		            break;
		        }
		    }

		    if (juegoAMover != null) {
		        miCafe.getAdmin().moverJuego(juegoAMover); // Llamamos al método que implementamos antes
		    } else {
		        System.out.println("El juego no está en la lista de ventas.");
		    }
		}

		private Juego buscarJuegoPorId(int id) {
		    for (Juego j : miCafe.getJuegosPrestamo()) if (j.getId() == id) return j;
		    for (Juego j : miCafe.getJuegosVenta()) if (j.getId() == id) return j;
		    return null;
		}
		
		
		public void gestionarJuego() {
		    Scanner sc = new Scanner(System.in);
		    if (!validarAdmin()) {
		        return; // Si no es admin, salimos de la función
		    }
		        
		        System.out.println("\n¿Qué desea hacer?");
		        System.out.println("1. Crear nuevo juego");
		        System.out.println("2. Modificar parámetros de un juego");
		        System.out.println("3. Mover juego de Venta a Préstamo");
		        System.out.print("Seleccione una opción: ");
		        int opcionPrincipal = sc.nextInt();
		        sc.nextLine(); // Limpiar buffer

		        switch (opcionPrincipal) {
		            case 1:
		                registrarNuevoJuego(); // Lógica de creación (la que ya tenías)
		                break;

		            case 2:
		                modificarJuego(sc);
		                break;

		            case 3:
		                moverInventario(sc);
		                break;

		            default:
		                System.out.println("Opción no válida.");
		                break;
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
		
		
		public boolean mismoDia(Calendar c1, Calendar c2) {
			return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
					&& c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
		}

		public boolean tieneTurno(List<Calendar> turnos, Calendar buscado) {
			for (Calendar c : turnos) {
				if (mismoDia(c, buscado)) {
					return true;
				}
			}
			return false;
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
