package consola.interfaz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class ConsolaEmpleado {
	static private Cafe miCafe;
	private Scanner lector;
	private Random aleatorio;

	public ConsolaEmpleado(Cafe cafe) {
		ConsolaEmpleado.miCafe = cafe;
	}

	public void registrarMesero(int id, String nombre, String login, String password, Cafe miCafe) {
		Mesero nuevoM = new Mesero(id, login, password, nombre);
		miCafe.getEmpleados().add(nuevoM);
	}

	public void registrarCocinero(int id, String nombre, String login, String password, Cafe miCafe) {
		Cocinero nuevoC = new Cocinero(id, login, password, nombre);
		miCafe.getEmpleados().add(nuevoC);
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
			String estado = j.getEstado();
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
		public void sugerirPlatillo (Scanner lectorMenu){
			autenticarEmpleado();
			System.out.println("Ingrese el nombre del producto: ");
			String nombre = lectorMenu.nextLine();
			System.out.println("Ingrese el precio del producto: ");
			try {
				int precio = lectorMenu.nextInt();
				lectorMenu.nextLine();
				System.out.println("Ingrese los alergenos del producto, si no hay : ");
				String alergenos = lectorMenu.nextLine();
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

		private void pedirYServirPlatillo (Reserva r, Mesero mes){
			List<Platillo> menu = miCafe.getMenuPlatillos();
			for (int i = 0; i < menu.size(); i++)
				System.out.println(i + ". " + menu.get(i).getNombre());

			int sel = lector.nextInt();
			if (sel >= 0 && sel < menu.size()) {
				mes.servirPlatillos(r, menu.get(sel));
				System.out.println(" Verificando alérgenos y sirviendo...");
			}
		}

		private void pedirYServirBebida (Reserva r, Mesero mes){
			List<Bebida> menuB = miCafe.getMenuBebidas();
			for (int i = 0; i < menuB.size(); i++)
				System.out.println(i + ". " + menuB.get(i).getNombre());

			int sel = lector.nextInt();
			if (sel >= 0 && sel < menuB.size()) {
				mes.servirBebidas(r, menuB.get(sel));
				System.out.println(" Validando restricciones de edad/seguridad y sirviendo...");
			}
		}
	}
		private void mostrarYAgregar (List < ? extends Producto > lista, List < Producto > carrito){
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


		public void ingresarJuegoFav () {
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
}
