package consola.interfaz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import exceptions.CategoriaInvalidaException;
import exceptions.JuegoNoEncontradoException;
import exceptions.NumeroJugadoresExcedidoException;
import exceptions.RestriccionEdadInvalidaException;
import modelo.*;
import modelo.usuario.*;
import modelo.producto.*;

public class ConsolaAdministrador extends ConsolaAbstract{

	public ConsolaAdministrador(Cafe cafe){
		super(cafe);
	}
	  
	public  void registrarUsuarioNuevo() {
		System.out.println("Bienvenido Nuevo Administrador\n");
		System.out.print("Ingrese su Nombre completo: ");
		String nombre = lector.nextLine();
		String login = nombre.split(" ")[0].toLowerCase() + 00;

		System.out.print("Ingrese una Contraseña: ");
		String password = lector.nextLine();
			
		miCafe.cambiarAdmin(new Administrador(00, login, password, nombre, miCafe)); 
		System.out.println("Registro exitoso para " + nombre + "con el login: " + login +"\n");
	}
	
	public Usuario autenticarUsuario(){
		System.out.print("Login del Administrador: ");
		String loginEmp = lector.nextLine();
		System.out.print("Contraseña del Administrador: ");
		String passEmp = lector.nextLine();

		Usuario auth = buscarUsuario(loginEmp);

		// Validamos que el usuario exista, sea un administrador y la contraseña coincida
		if (auth instanceof Administrador && auth.getPassword().equals(passEmp)) {
			return (Administrador) auth;
		}
		System.out.println("El inicio de sesion ha fallado, intente de nuevo.");
		return null;
	}
	
	public void registrarNuevoJuego() throws NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException { //TODO: Acá hace falta mirar la persistencia para cargarlo
		if(autenticarUsuario()== null){
			return;
		}
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

	    System.out.print("¿Es un juego difícil? (y/n): ");
	    String esDificil = sc.nextLine().toLowerCase();
		if(numJug<0 || numJug>40) {
			throw new NumeroJugadoresExcedidoException();
		}
		if(!cat.equals("Tablero") && !cat.equals("Cartas") && !cat.equals("Acción")){
			throw new CategoriaInvalidaException(cat, new String[]{"Tablero", "Cartas", "Acción"});
		}
		if(!edad.equals("-5") && !edad.equals("Adultos")){
			throw new RestriccionEdadInvalidaException();
		}
	    Juego nuevoJuego;
	    if (esDificil.equals("y")) {
	        System.out.print("Ingrese Instrucciones Especiales: ");
	        String instrucciones = sc.nextLine();
	        nuevoJuego = new JuegoDificil(id, precio, nombre, anio, empresa, numJug, edad, cat, instrucciones);
	    } else {
	        nuevoJuego = new Juego(id, precio, nombre, anio, empresa, numJug, edad, cat);
	    }
		int tipo = leerEntero("¿Destino? (1. Préstamo / 2. Venta): ");
	    if (tipo == 1) {
	        miCafe.getJuegosPrestamo().add(nuevoJuego);
	        System.out.println("Juego añadido a PRÉSTAMO.");
	    } else if (tipo == 2) {
	        miCafe.getJuegosVenta().add(nuevoJuego);
	        System.out.println("Juego añadido a VENTA.");
	    }
	}
	
	public void agregarTurno() {
		Administrador admin = (Administrador) autenticarUsuario();
		if(admin == null){
			return;
		}
		System.out.println("\n--- AGREGAR TURNO A EMPLEADO ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida
		System.out.println("Ingrese el login del empleado del cual desea agregar el turno: ");
		String login = lector.nextLine();
		lector.nextLine();
		Empleado empleadoActivo = (Empleado) buscarUsuario(login);
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
		if (tieneTurno(empleadoActivo.getListaFechas(), cal)) {
			System.out.println("El empleado ya tiene este turno asignado.");
			return;
		} else {
			admin.asignarTurno(empleadoActivo, cal, true);
			System.out.println("Turno asignado con exito.");
		}
	}
	public boolean tieneTurno(ArrayList<Calendar> turnos, Calendar cal){
		for(Calendar c: turnos){
			if(c.equals(cal)){
				return true;
			}
		}
		return false;
	}
	public void gestionarTurno(Scanner lectorMenu) {
		if(autenticarUsuario()== null){
			return;
		}
		System.out.println("0. Consultar turno de empleado.");
		System.out.println("1. Agregar turno de empleado.");
		System.out.println("2. Solicitar cambio de turno de empleado.");

		try {
			int opcion = leerEntero("Ingrese la opcion deseada: ");
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
			int opcion = 0;
		}
	}

	private void consultarTurno() {
		System.out.println("\n--- Consultar Turnos de Empleado ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida
		System.out.println("Ingrese el login del empleado");
		String login = lector.nextLine();
		lector.nextLine();
		Empleado empleadoActivo = (Empleado) buscarUsuario(login);
		if (empleadoActivo == null) {
			return;
		}
		List<Turno> turnos = empleadoActivo.getTurnos();
		for (Turno jornada : turnos) {
			System.out.println(jornada.getFecha());
			return;
		}
	}

	public void cambiarTurno() {
		if(autenticarUsuario()== null){
			return;
		}
		System.out.println("\n--- CAMBIAR TURNO DE EMPLEADO ---");
		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida
		System.out.println("Ingrese el login del empleado");
		String login = lector.nextLine();
		lector.nextLine();
		Empleado empleadoActivo = (Empleado) buscarUsuario(login);
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

		if (tieneTurno(empleadoActivo.getListaFechas(), cal)) {
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
	    if (autenticarUsuario() == null){
	        return; 
	    }

	    Scanner sc = new Scanner(System.in);
	    ArrayList<Producto> sugerencias = miCafe.getSugerenciasPendientes();
		ArrayList<Platillo> platillosSug= new ArrayList<>();
		for(Producto pro: sugerencias){
			if(pro instanceof Platillo){
				platillosSug.add((Platillo) pro);
			}
		}
	    // 2. Verificamos si hay platillos por revisar
	    if (platillosSug == null || sugerencias.isEmpty()) {
	        System.out.println("No hay sugerencias de platillos pendientes por revisar.");
	        return;
	    }

	    System.out.println("\n--- Revisión de Sugerencias de Platillos ---");
	    
	    // 3. Recorremos una copia de la lista para evitar errores al remover elementos
	    ArrayList<Platillo> copiaSugerencias = new ArrayList<>(platillosSug);

	    for (Platillo p : copiaSugerencias) {
	        System.out.println("\nPlatillo: " + p.getNombre());
	        System.out.println("Precio sugerido: $" + p.getPrecio());
	        System.out.println("Categoría: " + p.getAlergeneos());

			int decision = leerEntero("¿Qué desea hacer? (1. Aceptar / 2. Rechazar / 3. Omitir por ahora): ");

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
		Administrador admin = (Administrador) autenticarUsuario();
		if(admin == null){
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
				try{
					juegoAEditar.setCategoria(sc.nextLine());
					System.out.println("Parámetros actualizados con éxito.");
				}
		        catch(CategoriaInvalidaException e){
					System.out.println("Error: Categoria invalida, ingrese Tablero, Cartas o Acción (con tilde).");
				}
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
				try {
					miCafe.getAdmin().moverJuego(juegoAMover);
				}
				catch(JuegoNoEncontradoException e){
					System.out.println("Juego no encontrado en la base de datos del cafe.");
				}
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
			Administrador admin = (Administrador) autenticarUsuario();
			if(admin == null){
				return;
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
						try {
							registrarNuevoJuego();
						}
						catch(NumeroJugadoresExcedidoException e){
							System.out.println("El numero de jugadores excede el maximo (40).");
						}
						catch(RestriccionEdadInvalidaException e){
							System.out.println("Formato de retriccion de edad indebido, debe ser o -5 o Adultos.");
						}
						catch(CategoriaInvalidaException e){
							System.out.println("Error: Categoria invalida, ingrese Tablero, Cartas o Acción (con tilde).");
						}
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
