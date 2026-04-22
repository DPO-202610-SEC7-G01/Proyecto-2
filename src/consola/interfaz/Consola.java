package consola.interfaz;

import java.util.Scanner;
import persistencia.PersistenciaCafeJson;
import persistencia.PersistenciaOperacionesJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import modelo.Cafe;
import modelo.Reserva;
import modelo.Transaccion;
import modelo.Mesa;

import modelo.usuario.Cliente;
import modelo.usuario.Mesero;
import modelo.usuario.Cocinero;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;
import modelo.usuario.Administrador;

import modelo.producto.Juego;
import modelo.producto.JuegoDificil;
import modelo.producto.Producto;
import modelo.producto.Bebida;
import modelo.producto.Platillo;

public class Consola {
	private PersistenciaCafeJson persistenciaCafe;
	private PersistenciaOperacionesJson persistenciaOps;
	private Cafe miCafe;
	private Scanner lector;
	private Random aleatorio;
	private int opcion = 0;

	public Consola() {
		this.miCafe = new Cafe(50);
		this.lector = new Scanner(System.in);
		this.aleatorio = new Random();
		this.persistenciaCafe = new PersistenciaCafeJson();
		this.persistenciaOps = new PersistenciaOperacionesJson();
		
		cargarDatosBase();
	}

	private void cargarDatosBase() {
		try {
			System.out.println("Buscando archivos de guardado previos...");
			// Intentamos cargar la infraestructura y luego las operaciones
			persistenciaCafe.cargarCafe("cafe.json",miCafe);
			persistenciaOps.cargarOperaciones("operaciones.json", miCafe);
			System.out.println(" Datos cargados exitosamente.");
			
		} catch (Exception e) {
			System.out.println(" No se encontraron datos previos o hubo un error. Inicializando local por defecto...");
			
			Juego juegoInicial = new Juego(501, 150000, "Catan", 1995, "Devir", 4, "apto 5 anios", "Tablero");
			Bebida bebidaInicial = new Bebida(201, 12000, "Café Americano", "Caliente", false);
			Platillo platilloInicial = new Platillo(101, 25000, "Sandwich de Pavo", "Gluten, Lácteos");
			
			miCafe.getJuegosVenta().add(juegoInicial);
			miCafe.getJuegosPrestamo().add(juegoInicial);
			miCafe.getMenuBebidas().add(bebidaInicial);
			miCafe.getMenuPlatillos().add(platilloInicial);
			
			registrarAdministrador();
			inicializarMesas();
		}
	}

	private void inicializarMesas() {
		int capacidadRestante = miCafe.getCapacidad(); 
		int contadorMesa = 1;

		System.out.println("--- CONFIGURANDO DISTRIBUCIÓN DEL LOCAL ---");

		while (capacidadRestante > 0) {
			int sillasMesa = aleatorio.nextInt(15);

			if (sillasMesa > capacidadRestante) {
				sillasMesa = capacidadRestante; // Ajustamos la última mesa al espacio sobrante
			}

			// Creamos la mesa y la añadimos al café
			// Asumiendo que el constructor de Mesa es: Mesa(id, numSillas)
			Mesa nuevaMesa = new Mesa(contadorMesa, sillasMesa, true);
			miCafe.getMesas().add(nuevaMesa);

			System.out.println("Mesa #" + contadorMesa + " creada con " + sillasMesa + " sillas.");

			capacidadRestante -= sillasMesa;
			contadorMesa++;
		}

		System.out.println("✅ Local configurado al 100% de su capacidad.");
	}

	public void registrarAdministrador() {
		System.out.println("Registro Administrador ");
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
		if (miCafe.getAdmin() != null) {
			System.out.println("Hubo un cambio exitoso de la administración");
		}
		miCafe.cambiarAdmin(new Administrador(id, login, password, nombre, miCafe));
		System.out.println("Registro exitoso con el login: " + login);
	}

	public void registrarUsuarioNuevo() {
		System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
		System.out.println("1. Cliente | 2. Mesero | 3. Cocinero ");
		System.out.print("Seleccione: ");
		int tipo = lector.nextInt();
		lector.nextLine();

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

		// 2. Creación según el tipo
		switch (tipo) {
		case 1:
			System.out.print("Edad: ");
			int edad = lector.nextInt();
			lector.nextLine();
			System.out.print("Alérgenos: ");
			String alergenos = lector.nextLine();

			Cliente nuevoC = new Cliente(id, login, password, nombre, edad, alergenos);
			miCafe.agregarUsuario(nuevoC);
			break;

		case 2:
			miCafe.getEmpleados().add(new Mesero(id, login, password, nombre));
			break;

		case 3:
			miCafe.getEmpleados().add(new Cocinero(id, login, password, nombre));
			break;

		default:
			System.out.println("❌ Opción inválida.");
			return;
		}

		System.out.println("Registro exitoso con el login: " + login);
	}

	public void cambioContraseña() {
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

	private Usuario buscarUsuario(String login) {
		for (Cliente c : miCafe.getClientes()) {
			if (c.getLogin().equals(login))
				return c;
		}
		for (Empleado e : miCafe.getEmpleados()) {
			if (e.getLogin().equals(login))
				return e;
		}
		return null;
	}

	public void ingresarJuegoFav() {
		System.out.println("\n--- AGREGAR JUEGO A FAVORITOS ---");
		System.out.print("Ingrese su login de usuario: ");
		String loginBusqueda = lector.nextLine();

		// 1. Buscamos al usuario usando la función auxiliar
		Usuario usuarioEncontrado = buscarUsuario(loginBusqueda);

		if (usuarioEncontrado != null) {
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

				if (usuarioEncontrado instanceof Cliente) {
					Cliente c = (Cliente) usuarioEncontrado;
					c.agregarJuegoFavorito(juegoElegido);
				} else if (usuarioEncontrado instanceof Empleado) {
					Empleado e = (Empleado) usuarioEncontrado;
					e.agregarJuegoFavorito(juegoElegido);
				}

				System.out.println(juegoElegido.getNombre() + " ha sido añadido a los favoritos de "
						+ usuarioEncontrado.getNombre());
			} else {
				System.out.println("Opción de juego no válida.");
			}
		} else {
			System.out.println(" Error: No se encontró ningún usuario con el login: " + loginBusqueda);
		}
	}

	public void simularCompra() {
		System.out.println("\n--- SIMULACIÓN DE COMPRA INTERACTIVA ---");
		System.out.print("Ingrese su login: ");
		String login = lector.nextLine();
		Usuario u = buscarUsuario(login);

		if (u == null) {
			System.out.println("Usuario no encontrado. Por favor, regístrese:");
			registrarUsuarioNuevo();
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
				mostrarYAgregar(miCafe.getJuegosVenta(), carrito);
			} else if (cat == 2) {
				List<Producto> menuCompleto = new ArrayList<>();
				menuCompleto.addAll(miCafe.getMenuPlatillos());
				menuCompleto.addAll(miCafe.getMenuBebidas());
				mostrarYAgregar(menuCompleto, carrito);
			} else if (cat == 3) {
				if (carrito.isEmpty()) {
					System.out.println("El carrito está vacío. Compra cancelada.");
					return;
				}
				comprando = false;
			}
		}

		// 2. Validación de Amistad (Solo para Clientes)
		if (u instanceof Cliente) {
			Cliente c = (Cliente) u;
			System.out.print("¿Es amigo de algún empleado? (si/no): ");
			if (lector.nextLine().equalsIgnoreCase("si")) {
				if (verificarSiEsAmigo(c)) {
					c.nuevoAmigo();
					System.out.println("✨ Descuento de amigo ACTIVADO.");
				} else {
					System.out.println("❌ No estás en la lista de amigos oficial.");
				}
			}
		}

		// 3. Generación y Registro
		int idT = aleatorio.nextInt(10000);
		Transaccion t = null;
		if (u instanceof Cliente)
			t = ((Cliente) u).generarTransaccion(carrito, idT);
		else if (u instanceof Empleado)
			t = ((Empleado) u).generarTransaccion(carrito, idT);

		if (t != null) {
			miCafe.getHistorialTransaccion().add(t);
			imprimirFacturaDetallada(t, u);
		}
	}

	private boolean verificarSiEsAmigo(Cliente cliente) {
		for (Empleado e : miCafe.getEmpleados()) {
			if (e.getAmigos().contains(cliente)) {
				return true;
			}
		}
		return false;
	}

	private void imprimirFacturaDetallada(Transaccion t, Usuario u) {
		String verde = "\u001B[32m";
		String cursiva = "\u001B[3m";
		String reset = "\u001B[0m";

		System.out.println("\n========================================");
		System.out.println("           FACTURA DE VENTA           ");
		System.out.println("          ID: " + t.getId());
		System.out.println("========================================");
		System.out.println("Fecha: " + t.getFecha().getTime());
		System.out.println("Cliente: " + u.getNombre());
		System.out.println("----------------------------------------");

		double subtotalNeto = 0;
		double totalImpuestos = 0;

		// Listar productos comprados
		for (Producto p : t.getProductos()) {
			double precioBase = p.getPrecio();
			double tasa = p.getTasaImpuesto(); // IVA o Impoconsumo
			double impuestoProducto = precioBase * tasa;

			System.out.printf("- %-18s | $%d (Imp: %.0f%%)\n", p.getNombre(), (int) precioBase, tasa * 100);

			subtotalNeto += precioBase;
			totalImpuestos += impuestoProducto;
		}

		// Cálculos de totales
		double totalConImpuestos = subtotalNeto + totalImpuestos;
		int totalPagar = t.calcularTotal(); // Este ya trae los descuentos aplicados
		double ahorro = totalConImpuestos - totalPagar;

		System.out.println("----------------------------------------");
		System.out.println("Subtotal (Base):     $" + (int) subtotalNeto);
		System.out.println("Total Impuestos:     $" + (int) totalImpuestos);

		if (ahorro > 0) {
			System.out.println(cursiva + "Ahorro aplicado:    -$" + (int) ahorro + reset);
		}

		System.out.println("----------------------------------------");
		System.out.println(verde + "TOTAL A PAGAR:       $" + totalPagar + reset);
		System.out.println("========================================\n");
	}

	// Método genérico para mostrar cualquier lista de productos y agregarlos al
	// carrito
	private void mostrarYAgregar(List<? extends Producto> lista, List<Producto> carrito) {
		if (lista.isEmpty()) {
			System.out.println("No hay productos en esta categoría.");
			return;
		}

		for (int i = 0; i < lista.size(); i++) {
			Producto p = lista.get(i);
			System.out.println(i + ". " + p.getNombre() + " ($" + p.getPrecio() + ")");
		}

		System.out.print("Seleccione el número del producto para agregar (o -1 para volver): ");
		int sel = lector.nextInt();
		lector.nextLine();

		if (sel >= 0 && sel < lista.size()) {
			carrito.add(lista.get(sel));
			System.out.println("✅ " + lista.get(sel).getNombre() + " añadido al carrito.");
		}
	}

	private void afiliarAmigo() {
		{
			System.out.println("\n--- AFILIACIÓN DE AMIGO DE EMPLEADO ---");

			// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

			Empleado empleadoActivo = autenticarEmpleado();
			if (empleadoActivo == null) {
				return;
			}

			// 2. Búsqueda del Cliente
			System.out.print("Ingrese el login del Cliente a afiliar: ");
			String loginCli = lector.nextLine();
			Usuario buscado = buscarUsuario(loginCli);

			Cliente clienteAAfiliar = null;

			if (buscado instanceof Cliente) {
				clienteAAfiliar = (Cliente) buscado;
			} else {
				System.out.println("El cliente no existe. Iniciando registro...");
				registrarUsuarioNuevo();
				// Después de registrar, intentamos recuperarlo (sería el último de la lista)
				List<Cliente> clientes = miCafe.getClientes();
				if (!clientes.isEmpty()) {
					clienteAAfiliar = clientes.get(clientes.size() - 1);
				}
			}

			// 3. Registro de la amistad
			if (clienteAAfiliar != null) {
				// Agregamos el cliente a la lista del empleado
				empleadoActivo.agregarAmigos(clienteAAfiliar);

				// Cambiamos el atributo booleano del cliente a true
				clienteAAfiliar.nuevoAmigo();

				System.out.println("\u001B[32m" + "¡Éxito! " + clienteAAfiliar.getNombre() + " ahora es amigo de "
						+ empleadoActivo.getNombre() + ". Ahora recibirá descuentos en sus compras." + "\u001B[0m");
			}

		}
	}

	public void hacerReserva() {
		System.out.println("\n---  PROCESO DE RESERVA ---");
		System.out.print("¿Para cuántas personas es la reserva?: ");
		int numPersonas = lector.nextInt();
		lector.nextLine();

		List<Cliente> listaClientesReserva = new ArrayList<>();

		for (int i = 1; i <= numPersonas; i++) {
			System.out.print("No escriba dos veces el mismo login\n");
			System.out.print("Ingrese login del cliente " + i + " (o escriba 'nuevo' para registrarlo): ");
			String entrada = lector.nextLine();

			Usuario u = buscarUsuario(entrada);

			if (entrada.equalsIgnoreCase("nuevo") || u == null || !(u instanceof Cliente)) {
				System.out.println("No se encontró el cliente. Procediendo a registro obligatorio...");
				registrarUsuarioNuevo();
				u = miCafe.getClientes().get(miCafe.getClientes().size() - 1);
			}

			listaClientesReserva.add((Cliente) u);
		}

		Calendar fechaReserva = Calendar.getInstance();
		Reserva nuevaReserva = new Reserva(listaClientesReserva, numPersonas, fechaReserva);
		int totalAntes = miCafe.getReservasPrevias().size();

		miCafe.registrarNuevaReserva(nuevaReserva);

		if (miCafe.getReservasPrevias().size() > totalAntes) {
			System.out.println("\u001B[32m" + " ¡Reserva Exitosa!" + "\u001B[0m");
			System.out.println("Mesa asignada: " + nuevaReserva.getMesa().getId());
			System.out.println("Total de reservas actuales en el café: " + miCafe.getReservasPrevias().size());
		} else {
			System.out.println("❌ No se pudo completar la reserva. Verifique disponibilidad de capacidad o mesas.");
			System.out.println("Total de reservas actuales en el café: " + miCafe.getReservasPrevias().size());
		}
	}

	public void solicitudesReserva() {
		System.out.println("\n--- GESTIÓN DE SOLICITUDES EN MESA ---");
		System.out.print("Ingrese el número de la mesa: ");
		int numMesa = lector.nextInt();
		lector.nextLine();

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
			if (e instanceof Mesero)
				meserosDisponibles.add((Mesero) e);
		}

		// SI LA RESERVA NO TIENE MESERO, LE ASIGNAMOS UNO
		if (reservaEncontrada.getMeseroAsignado() == null && !meserosDisponibles.isEmpty()) {
			Mesero inicial = meserosDisponibles.get(aleatorio.nextInt(meserosDisponibles.size()));
			reservaEncontrada.cambiarMesero(inicial);
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
			System.out.println("1. Pedir Platillo\n2. Pedir Bebida\n3. Prestar Juego\n4. Cambiar Mesero\n5. Salir");
			int op = lector.nextInt();
			lector.nextLine();

			switch (op) {
			case 1:
				pedirYServirPlatillo(reservaEncontrada, meseroActual);
				break;

			case 2:
				pedirYServirBebida(reservaEncontrada, meseroActual);
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

					System.out.print("Elija el número del juego que desea: ");
					int seleccion = lector.nextInt();
					lector.nextLine(); // Limpiar buffer

					// 2. Validar selección y solicitar autorización al mesero
					if (seleccion >= 0 && seleccion < juegosLibres.size()) {
						Juego juegoElegido = juegosLibres.get(seleccion);

						// El mesero ejecuta su lógica de validación interna
						boolean exito = meseroActual.autorizarPrestamo(reservaEncontrada, juegoElegido);

						if (exito) {
							System.out.println(
									" El mesero " + meseroActual.getNombre() + " ha entregado el juego a la mesa.");
						} else {
							System.out.println(
									"❌ El mesero denegó el préstamo (posiblemente por edad o capacidad del juego).");
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

	public void consultarTurno() {
		System.out.println("\n--- AGREGAR TURNO A EMPLEADO ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

		Empleado empleadoActivo = autenticarEmpleado();
		if (empleadoActivo == null) {
			return;
		}

		List<Calendar> turno = empleadoActivo.getTurno();
		for (Calendar jornada : turno) {
			System.out.println(jornada.getTime());
			return;
		}
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
	public void sugerirPlatillo(Scanner lectorMenu) {
		autenticarEmpleado();
		System.out.println("Ingrese el nombre del producto: ");
		String nombre = lectorMenu.nextLine();
		System.out.println("Ingrese el precio del producto: ");
		try {
			int precio = lectorMenu.nextInt();
			lectorMenu.nextLine();
			System.out.println("Ingrese los alergenos del producto, si no hay : ");
			String alergenos= lectorMenu.nextLine();
			System.out.println("Ingrese el id del producto: ");
			try {
				int id = lectorMenu.nextInt();
				lectorMenu.nextLine();
				Platillo sugerencia = new Platillo(id, precio, nombre, alergenos);
				miCafe.agregarSugerencias(sugerencia);
				System.out.println("Sugerencia agregada exitosamente.");
				return;
			} catch (Exception a) {
				System.out.println(" Error: Por favor ingrese un número válido.");
				lectorMenu.nextLine();
				return;
			}
	
		} catch (Exception e) {
			System.out.println(" Error: Por favor ingrese un número válido.");
			lectorMenu.nextLine();
			return;
		}
		
	}

	// Métodos de apoyo para limpiar el código principal
	private void pedirYServirPlatillo(Reserva r, Mesero mes) {
		List<Platillo> menu = miCafe.getMenuPlatillos();
		for (int i = 0; i < menu.size(); i++)
			System.out.println(i + ". " + menu.get(i).getNombre());

		int sel = lector.nextInt();
		if (sel >= 0 && sel < menu.size()) {
			mes.servirPlatillos(r, menu.get(sel));
			System.out.println(" Verificando alérgenos y sirviendo...");
		}
	}

	private void pedirYServirBebida(Reserva r, Mesero mes) {
		List<Bebida> menuB = miCafe.getMenuBebidas();
		for (int i = 0; i < menuB.size(); i++)
			System.out.println(i + ". " + menuB.get(i).getNombre());

		int sel = lector.nextInt();
		if (sel >= 0 && sel < menuB.size()) {
			mes.servirBebidas(r, menuB.get(sel));
			System.out.println(" Validando restricciones de edad/seguridad y sirviendo...");
		}
	}

	private void cambiarMeseroDeReserva(Reserva r, List<Mesero> lista) {
		System.out.println("Meseros disponibles:");
		for (int i = 0; i < lista.size(); i++)
			System.out.println(i + ". " + lista.get(i).getNombre());

		int sel = lector.nextInt();
		if (sel >= 0 && sel < lista.size()) {
			r.cambiarMesero(lista.get(sel));
			System.out.println(" Mesero cambiado. Ahora atiende: " + r.getMeseroAsignado().getNombre());
		}
	}

	public Empleado autenticarEmpleado() {
		// 1. Autenticación del Empleado
		System.out.print("Login del Empleado: ");
		String loginEmp = lector.nextLine();
		System.out.print("Contraseña del Empleado: ");
		String passEmp = lector.nextLine();

		Usuario auth = buscarUsuario(loginEmp);

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida
		if (auth instanceof Empleado && auth.getPassword().equals(passEmp)) {
			Empleado empleadoActivo = (Empleado) auth;
			return empleadoActivo;
		}
		System.out.println("El inicio de sesion ha fallado, intente de nuevo.");
		return null;
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


	public void solicitarJuego() {
    System.out.println("\n--- PRÉSTAMO DE JUEGOS ---");
    Empleado empleadoActivo = autenticarEmpleado();
    if (empleadoActivo == null) return;
    Calendar hoy = Calendar.getInstance();

    //  Mostrar juegos disponibles en el café
    List<Juego> juegosParaPrestamo = miCafe.getJuegosPrestamo();
    if (juegosParaPrestamo.isEmpty()) {
        System.out.println("❌ No hay juegos registrados para préstamo en el sistema.");
        return;
    }

    System.out.println("Seleccione el juego a prestar:");
    for (int i = 0; i < juegosParaPrestamo.size(); i++) {
        Juego j = juegosParaPrestamo.get(i);
        String estado = j.isPrestado() ? "[PRESTADO]" : "[DISPONIBLE]";
        System.out.println(i + ". " + j.getNombre() + " " + estado);
    }

    System.out.print("Ingrese el número del juego: ");
    try {
        int indice = Integer.parseInt(lector.nextLine());
        
        if (indice >= 0 && indice < juegosParaPrestamo.size()) {
            Juego juegoElegido = juegosParaPrestamo.get(indice);

            // Verificar si el juego ya está prestado físicamente
            if (juegoElegido.isPrestado()) {
                System.out.println(" Error: Este juego ya se encuentra en uso.");
                return;
            }

            boolean exito = empleadoActivo.aptoPrestamo(miCafe.getAdmin(), juegoElegido, hoy);

            if (exito) {
                System.out.println("\n¡Préstamo autorizado!");
                System.out.println("El juego '" + juegoElegido.getNombre() + "' ha sido entregado.");
                System.out.println("Registro creado en el historial del café por " + empleadoActivo.getNombre());
            } else {
                System.out.println("\n El préstamo fue denegado.");
                System.out.println("Razones posibles: Estás en tu turno de trabajo y hay gente o el juego tiene una reserva previa.");
            }

        } else {
            System.out.println(" Selección inválida.");
        }
    } catch (NumberFormatException e) {
        System.out.println(" Error: Ingrese un número válido.");
    }
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
	    
	
	
	public void terminarReserva() {
	    Scanner sc = new Scanner(System.in);
	    System.out.println("--- Finalizar Reserva y Generar Factura ---");
	    System.out.print("Ingrese el número de la mesa: ");
	    int numMesa = sc.nextInt();

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

	        // 5. Crear e instanciar la Transacción usando tu constructor
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
	
	
	public void guardarDatos() {
		try {
			System.out.println("\nGuardando la información del día...");
			persistenciaCafe.salvarCafe("cafe.json", miCafe);
			persistenciaOps.salvarOperaciones("operaciones.json", miCafe);
			System.out.println("¡Información guardada correctamente en formato JSON!");
		} catch (Exception e) {
			System.out.println(" Ocurrió un error crítico al guardar los archivos: " + e.getMessage());
		}
	}
	public static void main(String[] args) {
		Consola consola = new Consola();
		Scanner lectorMenu = new Scanner(System.in);
		int opcion = 0;

		System.out.println("BIENVENIDO A DULCES N DADOS ");

		do {
			System.out.println("\n--- MENÚ PRINCIPAL ---");
			System.out.println("0. Registrar nuevo admin");
			System.out.println("1. Registrar nuevo usuario");
			System.out.println("2. Ver total de clientes");
			System.out.println("3. Cambiar Contraseña");
			System.out.println("4. Ingreso de juegos favoritos");
			System.out.println("5. Comprar Productos");
			System.out.println("6. Afiliar un Amigo");
			System.out.println("7. Hacer una Reserva");
			System.out.println("8. Hacer solicitudes para la Mesa");
			System.out.println("9. Pagar Reserva");
			System.out.println("10. Gestionar turnos");
			System.out.println("11. Sugerir platillo");
			System.out.println("12. Solicitar prestamo");
			System.out.println("13. Gestionar Juegos");
			System.out.println("14. Terminar Reserva");
			System.out.println("15. Aceptar Platillos");
			System.out.println("16. Ver  Finanzas");
			System.out.println("17. Salir");
			System.out.print("Seleccione una opción: ");

			try {
				opcion = lectorMenu.nextInt();
				lectorMenu.nextLine();

				switch (opcion) {
				case 0:
					consola.registrarAdministrador();
					break;
				case 1:
					consola.registrarUsuarioNuevo();
					break;
				case 2:
					int total = consola.miCafe.getClientes().size();
					System.out.println("Actualmente hay " + total + " clientes en el sistema.");
					break;
				case 3:
					consola.cambioContraseña();
					break;
				case 4:
					consola.ingresarJuegoFav();
					break;
				case 5:
					consola.simularCompra();
					break;
				case 6:
					consola.afiliarAmigo();
					break;
				case 7:
					consola.hacerReserva();
					break;
				case 8:
					consola.solicitudesReserva();
					break;
				case 9:
					System.out.println("¡Muchas gracias por su pago!");
					System.out.println(" Saliendo del sistema... ¡Hasta luego!");
					break;
				case 10:
					consola.gestionarTurno(lectorMenu);
					break;
				case 11:
					consola.sugerirPlatillo(lectorMenu);
					break;
				case 12:
					consola.solicitarJuego();
					break;
				case 13:
					consola.gestionarJuego();
					break;
				case 14:
					consola.terminarReserva();
					break;
				case 15:
					consola.aceptarPlatillo();
					break;
				case 16:
					consola.verFinanzas();
					break;
				case 17:
					consola.guardarDatos(); 
					System.out.println("Saliendo del sistema... ¡Hasta luego!");
					return;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
				}
			} catch (Exception e) {
				System.out.println(" Error: Por favor ingrese un número válido.");
				lectorMenu.nextLine();
				opcion = 0;
			}

		} while (opcion != 3);

		lectorMenu.close();
	}
}