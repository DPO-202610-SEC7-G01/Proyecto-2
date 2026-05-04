package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import modelo.producto.*;
import modelo.usuario.*;

public class Transaccion {
	private int id;
	private Calendar fecha;
	private final double DESCUENTO_EMPLEADO = 0.2;
	private final double DESCUENTO_AMIGO_EMPLEADO = 0.1;
	private final double PROPINA_SUGERIDA = 0.1;
	private List<Producto> productos;
	private Usuario cliente_final;
	private boolean amigoEmpleado;
	
	//Constructor
	public Transaccion(int id, Calendar fecha, List<Producto> productos, Usuario cliente_final, boolean amigoEmpleado) {
		this.id = id;
		this.fecha = fecha;
		this.productos = productos;
		if ( cliente_final instanceof Empleado || cliente_final instanceof Cliente ) { // Nos aseguramos que los admins no pueden comprar
			this.cliente_final = cliente_final;
		}
		
		this.amigoEmpleado = amigoEmpleado;
	}
	
	//Getters y Setters
	public Usuario getCliente_final() {
		return cliente_final;
	}

	public boolean isAmigoEmpleado() {
		return amigoEmpleado;
	}
	public int getId() {
		return id;
	}
	public Calendar getFecha() {
		return fecha;
	}
	public double getDESCUENTO_EMPLEADO() {
		return DESCUENTO_EMPLEADO;
	}
	public double getDESCUENTO_AMIGO_EMPLEADO() {
		return DESCUENTO_AMIGO_EMPLEADO;
	}
	public double getPROPINA_SUGERIDA() {
		return PROPINA_SUGERIDA;
	}
	public List<Producto> getProductos() {
		return productos;
	}
	
	//Métodos
	public void agregarProducto(Producto p) {
	    if (p != null) {
	        this.productos.add(p);
	    }
	}
	
	public Transaccion generarTransaccion(ArrayList<Producto> productosComprados, 
	        int idNuevaTransaccion, Usuario usuario) {
	    
	    Calendar hoy = Calendar.getInstance();
	    boolean tieneAmigos = false;
	    
	    // Si el usuario es cliente, verificar si tiene amigos
	    if (usuario instanceof Cliente) {
	        Cliente cliente = (Cliente) usuario;
	        tieneAmigos = cliente.getAmigos();
	    } else if (usuario instanceof Empleado) {
	        // Si es empleado, se establece como false
	        tieneAmigos = false;
	    }
	    
	    Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, usuario, tieneAmigos);
	    return factura;
	}
	
	public int calcularTotal() {
		double total = 0;
		for (int i = 0; i < this.productos.size(); i++) {
			Producto producto = this.productos.get(i);
			total += producto.calcularPrecioFinal();
		}
		
		int puntosFidelidad = (int) ((int) total*0.01);
		if ( cliente_final instanceof Empleado ) { 
			Empleado empleado = (Empleado) cliente_final;
			empleado.sumarPuntosFidelidad(puntosFidelidad);
		}else {
			Cliente cliente = (Cliente) cliente_final;
			cliente.sumarPuntosFidelidad(puntosFidelidad);
		}
		
		if ( cliente_final instanceof Cliente &&  ((Cliente) cliente_final).getPremio().contains("50")) {
			double descuento = total* 0.5;
			total -=descuento;
			((Cliente) cliente_final).agregarPremio("");
			
		} else {
			if ( cliente_final instanceof Empleado) {
				double descuento  = total * this.DESCUENTO_EMPLEADO;
				total -= descuento;
			} else if(this.amigoEmpleado) {
				double descuento  = total * this.DESCUENTO_AMIGO_EMPLEADO;
				total -= descuento;
			}
			
		}
		
		return (int) total;
	}
	
	
	
}
