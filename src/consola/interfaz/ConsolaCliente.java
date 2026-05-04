package consola.interfaz;

import java.util.*;

import exceptions.InvalidCredentialsException;
import exceptions.JuegoNoAptoException;
import exceptions.UsuariosException;
import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class ConsolaCliente extends ConsolaAbstract{
	
	public ConsolaCliente(Cafe cafe){
		super(cafe);
	}

	@Override
	public void registrarUsuarioNuevo() {
		System.out.print("Nombre completo: ");
		String nombre = lector.nextLine();

		// 1. Generar Login y Verificar Unicidad
		int id = aleatorio.nextInt(1001);
		String loginBase = nombre.split(" ")[0].toLowerCase() + id;

		// Si el login ya existe (por pura mala suerte del azar), generamos otro
		while (buscarUsuario(loginBase) != null) {
			id = aleatorio.nextInt(1001);
			loginBase = nombre.split(" ")[0].toLowerCase() + id;
		}

		final String login = loginBase; // Lo hacemos final para usarlo con seguridad
		System.out.print("Ingrese Password: ");
		String password = lector.nextLine();
		System.out.print("Edad: ");
		int edad = lector.nextInt();
		lector.nextLine();
		System.out.print("Alérgenos: ");
		String alergenos = lector.nextLine();
		ArrayList<String> alergenosLista = leerAlergenos(alergenos);
		try {
			Cliente nuevoC = new Cliente(id, login, password, nombre, edad, alergenosLista);
			miCafe.getClientes().add(nuevoC);
		}
		catch (Exception e){
			System.out.println("Error al crear usuario nuevo, intente de nuevo.");
		}
	}
	@Override
	public Cliente autenticarUsuario(){
		// 1. Autenticación del Cliente
		System.out.print("Login del Cliente: ");
		String loginEmp = lector.nextLine();
		System.out.print("Contraseña del Cliente: ");
		String passEmp = lector.nextLine();

		Usuario auth = buscarUsuario(loginEmp);

		// Validamos que el usuario exista, sea un Cliente y la contraseña coincida
		if (auth instanceof Cliente && auth.getPassword().equals(passEmp)) {
			return (Cliente) auth;
		}
		System.out.println("El inicio de sesion ha fallado, intente de nuevo.");
		return null;
	}
	
	public void ingresarJuegoFav() {
		System.out.println("\n--- AGREGAR JUEGO A FAVORITOS ---");

		// 1. Buscamos al usuario usando la función auxiliar
		Cliente c = autenticarUsuario();

		if (c != null) {
			// 2. Validamos que el café tenga juegos para mostrar
			if (miCafe.getJuegosVenta().isEmpty()) {
				System.out.println("❌No hay juegos registrados en el catálogo del café.");
				return;
			}

			System.out.println("Seleccione el juego que desea agregar:");
			for (int i = 0; i < miCafe.getJuegosVenta().size(); i++) {
				System.out.println(i + ". " + miCafe.getJuegosVenta().get(i).getNombre());
			}

			System.out.print("Ingrese el número del juego: ");
			int indice = lector.nextInt();
			lector.nextLine(); // Limpiar el salto de línea del buffer

			if (indice >= 0 && indice < miCafe.getJuegosVenta().size()) {
				Juego juegoElegido = miCafe.getJuegosVenta().get(indice);
				c.agregarJuegoFavorito(juegoElegido);
				System.out.println(juegoElegido.getNombre() + " ha sido añadido a los favoritos de "
						+ c.getNombre());
			} else {
				System.out.println("Opción de juego no válida.");
			}
		}
	}
	
	public void simularCompra() {
		System.out.println("\n--- SIMULACIÓN DE COMPRA INTERACTIVA ---");
		Cliente c = autenticarUsuario();
		if(c== null){
			return;
		}
		List<Producto> carrito = new ArrayList<>();
		boolean comprando = true;

		// 1. Bucle de selección de productos
		while (comprando) {
			System.out.println("\n--- CATÁLOGO DISPONIBLE ---");
			System.out.println("1. Ver Juegos de Mesa");
			System.out.println("2. Ver Menú (Platillos y Bebidas)");
			System.out.println("3. Finalizar Compra y Pagar");
			System.out.print("Seleccione una categoría: ");

			int cat = lector.nextInt();
			lector.nextLine();

			if (cat == 1) {
				agregarProductoACarrito(miCafe.getJuegosVenta(), carrito);
			} else if (cat == 2) {
				List<Producto> menuCompleto = new ArrayList<>();
				menuCompleto.addAll(miCafe.getMenuPlatillos());
				menuCompleto.addAll(miCafe.getMenuBebidas());
				agregarProductoACarrito(menuCompleto, carrito);
			} else if (cat == 3) {
				if (carrito.isEmpty()) {
					System.out.println("El carrito está vacío. Compra cancelada.");
					return;
				}
				comprando = false;
			}
		}

		// 2. Validación de Amistad (Solo para Clientes)
		boolean amigo = clienteEsAmigoDeAlgunEmpleado(c);
		c.setAmigos(amigo);

		// 3. Generación y Registro
		int idT = aleatorio.nextInt(10000);
		Transaccion t = null;
		t = c.generarTransaccion(carrito, idT);

		if (t != null) {
			miCafe.getHistorialTransaccion().add(t);
			imprimirFactura(t, c);
		}
	}

	public void hacerReserva() {
		System.out.println("\n---  PROCESO DE RESERVA ---");
		System.out.print("¿Para cuántas personas es la reserva?: ");
		int numPersonas = lector.nextInt();
		lector.nextLine();

		List<Cliente> listaClientesReserva = new ArrayList<Cliente>();
		System.out.println("Autentique cada usuario que vaya a ser parte de la reserva.");
		for (int i = 1; i <= numPersonas; i++) {
			int opcion = leerEntero("Si el cliente aun no tiene usuario pulse 1, caso contrario 0.");
			Cliente c = null;
			switch (opcion) {
				case 0:
					c = autenticarUsuario();
					if(c == null){
						System.out.println("Fallo la autenticacion del usuario, intente de nuevo.");
						i--;
						continue;
					}
					break;
				case 1:
					System.out.println("No se encontró el cliente. Procediendo a registro obligatorio...");
					registrarUsuarioNuevo();
					c = miCafe.getClientes().get(miCafe.getClientes().size() - 1);
					break;
				default:
					System.out.println("Opción inválida.");
					i--;
					continue;
			}
			listaClientesReserva.add(c);
		}

		Calendar fechaReserva = Calendar.getInstance();
		Reserva nuevaReserva = new Reserva(listaClientesReserva, numPersonas, fechaReserva);

		boolean exito = miCafe.registrarNuevaReserva(nuevaReserva);

		if (exito) {
			System.out.println("\u001B[32m" + " ¡Reserva Exitosa!" + "\u001B[0m");
			System.out.println("Mesa asignada: " + nuevaReserva.getMesa().getId());
			System.out.println("Total de reservas actuales en el café: " + miCafe.getReservasPrevias().size());
		} else {
			System.out.println("❌ No se pudo completar la reserva. Verifique disponibilidad de capacidad o mesas.");
			System.out.println("Total de reservas actuales en el café: " + miCafe.getReservasPrevias().size());
		}
	}
	
	public void solicitudesReserva(ConsolaEmpleado consolaEmpleado) throws JuegoNoAptoException {
		System.out.println("\n--- GESTIÓN DE SOLICITUDES EN MESA ---");
		int numMesa = leerEntero("Ingrese el numero de la mesa: ");

		Reserva reservaEncontrada = null;
		Calendar hoy = Calendar.getInstance();

		for (Reserva r : miCafe.getReservasPrevias()) {
			// Validación de seguridad para evitar NullPointerException
			if (r.getMesa() != null && r.getMesa().getId() == numMesa) {
				Calendar fechaR = r.getFecha();
				if (fechaR.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)
						&& fechaR.get(Calendar.DAY_OF_YEAR) == hoy.get(Calendar.DAY_OF_YEAR)) {
					reservaEncontrada = r;
					break;
				}
			}
		}

		if (reservaEncontrada == null) {
			System.out.println("❌ No hay reserva activa para hoy en la mesa " + numMesa);
			return;
		}

		// Obtenemos los meseros del café
		List<Mesero> meserosDisponibles = new ArrayList<>();
		for (Empleado e : miCafe.getEmpleados()) {
			if (e instanceof Mesero) {
				Mesero mesero = (Mesero) e;
				if (mesero.libreParaReserva(reservaEncontrada.getFecha())) {
					meserosDisponibles.add(mesero);
				}
			}
		}

		// SI LA RESERVA NO TIENE MESERO, LE ASIGNAMOS UNO
		if (reservaEncontrada.getMeseroAsignado() == null && !meserosDisponibles.isEmpty()) {
			Mesero inicial = meserosDisponibles.get(aleatorio.nextInt(meserosDisponibles.size()));
			inicial.nuevaReserva(reservaEncontrada, reservaEncontrada.getFecha());
			reservaEncontrada.setMesero(inicial);
		}

		boolean atendiendo = true;
		while (atendiendo) {
			// EXTRAEMOS AL MESERO DIRECTAMENTE DE LA RESERVA EN CADA VUELTA
			Mesero meseroActual = reservaEncontrada.getMeseroAsignado();

			if (meseroActual == null) {
				System.out.println("❌ Error: No hay meseros disponibles en el café.");
				break;
			}

			System.out.println("\n--- MESA " + numMesa + " | Mesero: " + meseroActual.getNombre() + " ---");
			int op = leerEntero("1. Pedir Platillo\n2. Pedir Bebida\n3. Prestar Juego\n4. Cambiar Mesero\n5. Salir");

			switch (op) {
			case 1:
				consolaEmpleado.pedirYServirPlatillo(reservaEncontrada, meseroActual);
				break;

			case 2:
				consolaEmpleado.pedirYServirBebida(reservaEncontrada, meseroActual);
				break;

			case 3:
				System.out.println("\n--- JUEGOS DISPONIBLES PARA PRÉSTAMO ---");
				List<Juego> juegosLibres = miCafe.getJuegosPrestamo();

				if (juegosLibres.isEmpty()) {
					System.out.println("No hay juegos disponibles en la ludoteca en este momento.");
				} else {
					// 1. Desplegar el menú de juegos
					for (int i = 0; i < juegosLibres.size(); i++) {
						Juego j = juegosLibres.get(i);
						System.out.println(
								i + ". " + j.getNombre() + " (" + j.getCategoria() + ") - " + j.getRestriccionEdad());
					}

					int seleccion = leerEntero("Elija el número del juego que desea: ");
					// 2. Validar selección y solicitar autorización al mesero
					if (seleccion >= 0 && seleccion < juegosLibres.size()) {
						Juego juegoElegido = juegosLibres.get(seleccion);

						// El mesero ejecuta su lógica de validación interna
						try {
							meseroActual.autorizarPrestamo(reservaEncontrada, juegoElegido);
							reservaEncontrada.getJuegosPrestados().add(juegoElegido);
							juegoElegido.setPrestado(true);
							miCafe.registrarJuegoEnHistorial(reservaEncontrada.getFecha(), reservaEncontrada.getClientes().get(0), juegoElegido);
							System.out.println("¡Prestamo exitoso disfrute el juego!");

						} catch(JuegoNoAptoException e){
							System.out.println("El prestamo del juego fue rechazado: " + e.getMessage());
						}
					} else {
						System.out.println("❌ Selección de juego no válida.");
					}
				}
				break;

			case 4:
				cambiarMeseroDeReserva(reservaEncontrada, meserosDisponibles);
				break;

			case 5:
				System.out.println("Finalizando atención de la mesa " + numMesa + "...");
				atendiendo = false;
				break;

			default:
				System.out.println("Opción no reconocida.");
				break;
			}
		}
	}
	
	public void terminarReserva() {
	    Scanner sc = new Scanner(System.in);
	    System.out.println("--- Finalizar Reserva y Generar Factura ---");
		int numMesa = leerEntero("Ingrese el número de la mesa: ");

	    // 1. Buscar la reserva activa
	    Reserva reservaActiva = null;
	    for (Reserva r : miCafe.getReservasPrevias()) {
	        if (r.getMesa() != null && r.getMesa().getId() == numMesa) {
	            reservaActiva = r;
	            break;
	        }
	    }

	    if (reservaActiva != null) {
	        // 2. Liberar juegos y mesa internamente
	        reservaActiva.finalizarReserva();

	        // 3. Preparar datos para la Transacción (Factura)
	        int nuevoId = miCafe.getHistorialTransaccion().size() + 1;
	        Calendar fechaActual = Calendar.getInstance();
	        List<Producto> productosConsumidos = reservaActiva.getFactura();
	        
	        // El cliente principal es el primero de la lista de la reserva
	        Usuario clientePrincipal = reservaActiva.getClientes().get(0);

	        // 4. Preguntar por beneficio de amigo de empleado
	        System.out.print("¿El cliente es amigo de un empleado? (1. Sí / 2. No): ");
	        boolean esAmigo = (sc.nextInt() == 1);

	        // 5. Crear e instanciar la Transacción usando el constructor
	        Transaccion nuevaFactura = new Transaccion(
	            nuevoId, 
	            fechaActual, 
	            productosConsumidos, 
	            clientePrincipal, 
	            esAmigo
	        );

	        // 6. Guardar en el historial del Café y limpiar el sistema
	        miCafe.getHistorialTransaccion().add(nuevaFactura);
	        miCafe.getReservasPrevias().remove(reservaActiva);

	        System.out.println("\nFactura #" + nuevoId + " generada con éxito.");
	        System.out.println("Total procesado: $" + reservaActiva.getTotalFactura());
	        System.out.println("La mesa " + numMesa + " ahora está disponible.");

	    } else {
	        System.out.println("Error: No se encontró una reserva para la mesa " + numMesa);
	    }
	}
	


	private void cambiarMeseroDeReserva(Reserva r, List<Mesero> lista) {
		System.out.println("Meseros disponibles:");
		for (int i = 0; i < lista.size(); i++)
			System.out.println(i + ". " + lista.get(i).getNombre());

		int sel = lector.nextInt();
		lector.nextLine();
		if (sel >= 0 && sel < lista.size()) {
			lista.get(sel).nuevaReserva(r, r.getFecha());
			r.setMesero(lista.get(sel));
			System.out.println(" Mesero cambiado. Ahora atiende: " + r.getMeseroAsignado().getNombre());
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