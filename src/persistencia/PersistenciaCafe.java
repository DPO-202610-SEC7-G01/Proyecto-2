package persistencia;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.*;
import modelo.*;
import modelo.usuario.*;
import modelo.producto.*;

public class PersistenciaCafe extends PersistenciaCentral{

	public static void descargarCafe(String reservasArchivo, String historialPrestamosArchivo, String transaccionesArchivo,
			String mesasArchivo, Cafe miCafe) throws IOException, FileNotFoundException{
		
		//Aun que el café empieza con 0 de disponibilidad entonces voy a aumentarlo acorde a la mesa
		descargarMesas(mesasArchivo,miCafe);
		descargarReservas(reservasArchivo,miCafe);
		descargarTransaccion(transaccionesArchivo,miCafe);
		
		}
	
	public static void descargarMesas(String mesasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
		int capacidad = 0;
		JSONArray jMesas = leerArchivoJSON(mesasArchivo);
	    
		for (int i = 0; i < jMesas.length(); i++) {
            JSONObject jMesa = jMesas.getJSONObject(i);
            
            Mesa nuevaMesa = new Mesa(
            		jMesa.getInt("id"),
            		jMesa.getInt("numSillas"),
            		jMesa.getBoolean("disponible"));
            
            capacidad += jMesa.getInt("numSillas");            
            miCafe.getMesas().add(nuevaMesa);
		}
	     miCafe.SetCapacidad(capacidad);  
	}
	
	public static void descargarReservas(String reservasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
	    JSONArray jReservas = leerArchivoJSON(reservasArchivo);
	    for (int i = 0; i < jReservas.length(); i++) {
	        JSONObject jReserva = jReservas.getJSONObject(i);
	        
	        JSONArray jClientes = jReserva.optJSONArray("clientes");
	        ArrayList<Cliente> clientes = new ArrayList<>();
	        if (jClientes != null) {
	            for (int j = 0; j < jClientes.length(); j++) {
	                Cliente cliente = PersistenciaUsuarios.descargarClientes(jClientes.getJSONObject(j));
	                clientes.add(cliente);
	            }
	        }
	        
	        JSONArray jProductos = jReserva.optJSONArray("productos");
	        ArrayList<Producto> productos = PersistenciaProductos.descargarProductos(jProductos);
	        
	        Reserva reserva = new Reserva(
	            clientes, 
	            jReserva.getInt("numPersonas"),
	            fechaEnCalendar(jReserva.getString("fecha"))
	        ); 
	        
	        reserva.setFactura(productos);
	        if (jReserva.has("totalFactura")) {
	            reserva.setTotalFactura(jReserva.getInt("totalFactura"));
	        }
	        miCafe.getReservasPrevias().add(reserva);
	    }
	}

	public static void descargarTransaccion(String transaccionesArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
	    JSONArray jTransacciones = leerArchivoJSON(transaccionesArchivo);
	    
	    for (int i = 0; i < jTransacciones.length(); i++) {
	        JSONObject jTransaccion = jTransacciones.getJSONObject(i);
	        
	        JSONArray jProductos = jTransaccion.optJSONArray("productos");
	        ArrayList<Producto> productos = PersistenciaProductos.descargarProductos(jProductos);
	        
	        // Procesar el cliente final directamente, sin crear un JSONArray adicional
	        JSONObject jClienteFinal = jTransaccion.getJSONObject("cliente_final");
	        Cliente clienteFinal = PersistenciaUsuarios.descargarClientes(jClienteFinal);
	        
	        Transaccion transaccion = new Transaccion(
	            jTransaccion.getInt("id"),
	            fechaEnCalendar(jTransaccion.getString("fecha")),
	            productos,
	            clienteFinal,
	            jTransaccion.getBoolean("amigoEmpleado")
	        );
	        
	        miCafe.agregarTransaccion(transaccion);
	    }
	}
	
	public void salvarCafe(String reservasArchivo, String historialPrestamosArchivo, String transaccionesArchivo,
			String mesasArchivo, Cafe micafe){
		
		
		
		}
	
}
