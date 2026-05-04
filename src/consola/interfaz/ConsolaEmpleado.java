package consola.interfaz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import exceptions.InvalidCredentialsException;
import exceptions.UsuariosException;
import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class ConsolaEmpleado extends ConsolaAbstract{
	public ConsolaEmpleado(Cafe cafe) {
        super(cafe);
	}


	public void registrarUsuarioNuevo() {
		Empleado empleadoActivo = autenticarUsuario();
		if (empleadoActivo == null) {
			return;
		}
		registrarUsuarioNuevoSinAutenticacion();
	}

	private void registrarUsuarioNuevoSinAutenticacion() {
		System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
		System.out.println("1. Cliente | 2. Mesero | 3. Cocinero ");
		int tipo = leerEntero("Seleccione opcion: ");
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
				ArrayList<String> alergenosLista = leerAlergenos(alergenos);
				try {
					Cliente nuevoC = new Cliente(id, login, password, nombre, edad, alergenosLista);
					miCafe.agregarUsuario(nuevoC);
				}
				catch(Exception e){
					System.out.println("Error al registrar el cliente.");
				}
				break;

			case 2:
				try {
					registrarMeseroSinAutenticacion(id, nombre, login, password, miCafe);
					break;
				}
				catch(Exception e){
					System.out.println("Error al registrar el mesero.");
				}

			case 3:
				try{
					registrarCocineroSinAutenticacion(id, nombre, login, password, miCafe);
				} catch (InvalidCredentialsException | UsuariosException e) {
					System.out.println("Error al registrar el cocinero.");
                }

                break;

			default:
				System.out.println("❌ Opción inválida.");
				return;
		}

		System.out.println("Registro exitoso con el login: " + login);
	}
	public Empleado autenticarUsuario() {
		// 1. Autenticación del Empleado
		System.out.print("Login del Empleado: ");
		String loginEmp = lector.nextLine();
		System.out.print("Contraseña del Empleado: ");
		String passEmp = lector.nextLine();

		Usuario auth = buscarUsuario(loginEmp);

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida
		if (auth instanceof Empleado && auth.getPassword().equals(passEmp)) {
			return (Empleado) auth;
		}
		System.out.println("El inicio de sesion ha fallado, intente de nuevo.");
		return null;
	}

	public void registrarMesero(int id, String nombre, String login, String password, Cafe miCafe) throws InvalidCredentialsException {
		Empleado empleadoActivo = autenticarUsuario();
		if (empleadoActivo == null) {
			return;
		}
		registrarMeseroSinAutenticacion(id, nombre, login, password, miCafe);
	}

	private void registrarMeseroSinAutenticacion(int id, String nombre, String login, String password, Cafe miCafe) throws InvalidCredentialsException {
		Mesero nuevoM = new Mesero(id, login, password, nombre);
		miCafe.getEmpleados().add(nuevoM);
	}

	public void registrarCocinero(int id, String nombre, String login, String password, Cafe miCafe) throws InvalidCredentialsException, UsuariosException {
		Empleado empleadoActivo = autenticarUsuario();
		if (empleadoActivo == null) {
			return;
		}
		registrarCocineroSinAutenticacion(id, nombre, login, password, miCafe);
	}

	private void registrarCocineroSinAutenticacion(int id, String nombre, String login, String password, Cafe miCafe) throws InvalidCredentialsException, UsuariosException {
		Cocinero nuevoC = new Cocinero(id, login, password, nombre);
		miCafe.getEmpleados().add(nuevoC);
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
		Empleado empleadoActivo = autenticarUsuario();
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
			String estado = j.getEstado();
			System.out.println(i + ". " + j.getNombre() + " " + estado);
		}

		System.out.print("Ingrese el número del juego: ");
		try {
			int indice = Integer.parseInt(lector.nextLine());

			if (indice >= 0 && indice < juegosParaPrestamo.size()) {
				Juego juegoElegido = juegosParaPrestamo.get(indice);

				// Verificar si el juego ya está prestado físicamente
				if (juegoElegido.estaDisponible() != true) {
					System.out.println(" Error: Este juego ya se encuentra en uso.");
					return;
				}

				boolean exito = empleadoActivo.aptoPrestamo(juegoElegido, hoy);

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
		public void sugerirPlatillo (){
			Empleado empleadoActivo = autenticarUsuario();
			if (empleadoActivo == null) {
				return;
			}
			System.out.println("Ingrese el nombre del producto: ");
			String nombre = this.lector.nextLine();
			try {
				int precio = leerEntero("Ingrese el precio del producto: ");
				System.out.println("Ingrese los alergenos del producto separados por comas, si no hay pulse enter: ");
				String alergenos = lector.nextLine();
				ArrayList<String> alergenosLista = leerAlergenos(alergenos);
				System.out.println("Ingrese el id del producto: ");
				try {
					int id = lector.nextInt();
					lector.nextLine();
					Platillo sugerencia = new Platillo(id, precio, nombre, alergenosLista);
					miCafe.agregarSugerencia(sugerencia);
					System.out.println("Sugerencia agregada exitosamente.");
					return;
				} catch (Exception a) {
					System.out.println(" Error: Por favor ingrese un número válido.");
					lector.nextLine();
				}

			} catch (Exception e) {
				System.out.println(" Error: Por favor ingrese un número válido.");
				lector.nextLine();
			}

		}

		protected void pedirYServirPlatillo (Reserva r, Mesero mes){
			List<Platillo> menu = miCafe.getMenuPlatillos();
			for (int i = 0; i < menu.size(); i++)
				System.out.println(i + ". " + menu.get(i).getNombre());

			int sel = lector.nextInt();
			if (sel >= 0 && sel < menu.size()) {
				mes.servirPlatillos(r, menu.get(sel));
				System.out.println(" Verificando alérgenos y sirviendo...");
			}
		}

		protected void pedirYServirBebida (Reserva r, Mesero mes){
			List<Bebida> menuB = miCafe.getMenuBebidas();
			for (int i = 0; i < menuB.size(); i++)
				System.out.println(i + ". " + menuB.get(i).getNombre());

			int sel = lector.nextInt();
			if (sel >= 0 && sel < menuB.size()) {
				mes.servirBebidas(r, menuB.get(sel));
				System.out.println(" Validando restricciones de edad/seguridad y sirviendo...");
			}
		}

	public void consultarTurno() {
		System.out.println("\n--- Consultar Turnos de Empleado ---");

		// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

		Empleado empleadoActivo = autenticarUsuario();
		if (empleadoActivo == null) {
			return;
		}

		List<Turno> turnos = empleadoActivo.getTurnos();
		for (Turno jornada : turnos) {
			System.out.println(jornada.getFecha());
			return;
		}
	}

	public void ingresarJuegoFav () {
		System.out.println("\n--- AGREGAR JUEGO A FAVORITOS ---");
		// 1. Buscamos al Empleado usando la función auxiliar
		Empleado e = autenticarUsuario();

		if (e != null) {
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
				e.agregarJuegoFavorito(juegoElegido);
				System.out.println(juegoElegido.getNombre() + " ha sido añadido a los favoritos de "
						+ e.getNombre());
			} else {
				System.out.println("Opción de juego no válida.");
			}
		}
	}

	private void afiliarAmigo() {
		{
			System.out.println("\n--- AFILIACIÓN DE AMIGO DE EMPLEADO ---");

			// Validamos que el usuario exista, sea un Empleado y la contraseña coincida

			Empleado empleadoActivo = autenticarUsuario();
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
				registrarUsuarioNuevoSinAutenticacion();
				// Después de registrar, intentamos recuperarlo (sería el último de la lista)
				List<Cliente> clientes = miCafe.getClientes();
				if (!clientes.isEmpty()) {
					clienteAAfiliar = clientes.get(clientes.size() - 1);
				}
			}

			// 3. Registro de la amistad
			if (clienteAAfiliar != null) {
				// Agregamos el cliente a la lista del empleado
				empleadoActivo.agregarAmigo(clienteAAfiliar);

				// Cambiamos el atributo booleano del cliente a true
				clienteAAfiliar.nuevoAmigo();

				System.out.println("\u001B[32m" + "¡Éxito! " + clienteAAfiliar.getNombre() + " ahora es amigo de "
						+ empleadoActivo.getNombre() + ". Ahora recibirá descuentos en sus compras." + "\u001B[0m");
			}

		}
	}

	public void simularCompra() {
		System.out.println("\n--- SIMULACIÓN DE COMPRA INTERACTIVA ---");
		Empleado e = autenticarUsuario();
		if(e== null){
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

		// 2. Generación y Registro
		int idT = aleatorio.nextInt(10000);
		Transaccion t = null;
		t = e.generarTransaccion(carrito, idT);

		if (t != null) {
			miCafe.getHistorialTransaccion().add(t);
			imprimirFactura(t, e);
		}
	}
	
	public static void main(Cafe miCafe) {
		Scanner lectorMenu = new Scanner(System.in);
		ConsolaEmpleado consola = new ConsolaEmpleado(miCafe);

		int opcion = 0;
		
		do {
			System.out.println("\n--- Bienvenido Empleado ---");
			System.out.println("0.  Registrarse Primera Vez ");
			System.out.println("1. Sugerir platillo.");
			System.out.println("2. Consultar turno.");
			System.out.println("3. Ingresar juego favorito.");
			System.out.println("4. Afiliar amigo.");
			System.out.println("5. Comprar productos.");
			System.out.println("6. Salir");
			System.out.print("Seleccione una opción: ");

			try {
				opcion = lectorMenu.nextInt();
				lectorMenu.nextLine();

				switch (opcion) {
					case 0:
						System.out.println("Recuerde que necesita que otro empleado lo registre.");
						consola.registrarUsuarioNuevo();
						break;
					case 1:
						consola.sugerirPlatillo();
						break;
					case 2:
						consola.consultarTurno();
						break;
					case 3:
						consola.ingresarJuegoFav();
						break;
					case 4:
						consola.afiliarAmigo();
						break;
					case 5:
						consola.simularCompra();
						break;
					case 6:
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
