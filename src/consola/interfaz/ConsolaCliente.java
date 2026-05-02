package consola.interfaz;

import java.util.*;

import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class ConsolaCliente extends consolaAbstract{
	
	public ConsolaCliente(Cafe cafe){
		super(cafe);
	}

	public void registrarUsuarioNuevo(int id, String nombre, String login, String password, Cafe miCafe) {
		System.out.print("Edad: ");
		int edad = lector.nextInt();
		lector.nextLine();
		System.out.print("Alérgenos: ");
		String alergenos = lector.nextLine();
		ArrayList<String> alergenosLista;
		if(alergenos.isBlank()) {
			alergenosLista = new ArrayList<>();
		}
		else {
			String[] alergenoslista = alergenos.split("\\s*,\\s*");
			alergenosLista = new ArrayList<>(Arrays.asList(alergenoslista));
		}
		Cliente nuevoC = new Cliente(id, login, password, nombre, edad, alergenosLista);
		miCafe.getClientes().add(nuevoC);
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
	
	private boolean verificarSiEsAmigo(Cliente cliente) {
		for (Empleado e : miCafe.getEmpleados()) {
			if (e.getAmigos().contains(cliente)) {
				return true;
			}
		}
		return false;
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
