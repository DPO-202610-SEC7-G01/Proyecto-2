package consola.interfaz;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import modelo.Reserva;
import modelo.Transaccion;
import modelo.producto.Producto;
import modelo.usuario.Usuario;

public class ConsolaCliente {

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
	
}
