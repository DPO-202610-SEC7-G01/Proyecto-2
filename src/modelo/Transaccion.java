package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import exceptions.UsuariosException;
import modelo.producto.*;
import modelo.usuario.*;

public class Transaccion {
	private int id;
	private Calendar fecha;
	private Cafe miCafe;
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
		if ( cliente_final instanceof Empleado || cliente_final instanceof Cliente ) { 
			this.cliente_final = cliente_final;
		}
		
		this.amigoEmpleado = amigoEmpleado;
	}
	
	public Transaccion(){
	}
	
	//Getters y Setters
	public Usuario getCliente_final() {
		return cliente_final;
	}

	public boolean isAmigoEmpleado() {  // aunque parece prueba de integracion vemos que es solo cliente 
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
	private void registrarTransaccion(Transaccion transaccion) {
		miCafe.agregarTransaccion(transaccion);
		
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
	    
	    if (usuario instanceof Cliente) {
	        Cliente cliente = (Cliente) usuario;
	        tieneAmigos = cliente.getAmigos();
	    } else if (usuario instanceof Empleado) {
	        tieneAmigos = false;
	    }
	    
	    Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, usuario, tieneAmigos);
	    return factura;
	}
	
	public int calcularTotal() throws UsuariosException {
	    if (productos == null || productos.isEmpty()) {
	        return 0; // como tal no es un error sino que es un factura sin cosas entonces un ese return está bien
	    }
	    
	    if (cliente_final == null) {
	        throw new UsuariosException(cliente_final,"transaccion ","No se puede calcular el total sin un cliente asociado a la transacción.");
	    }
	    
	    double total = 0;
	    
	    for (Producto producto : productos) {
	            total += producto.calcularPrecioFinal();       
	    }
	    
	    int puntosFidelidad = (int) (total * 0.01);
	    
	    if (cliente_final instanceof Empleado) {
	        Empleado empleado = (Empleado) cliente_final;
	        empleado.sumarPuntosFidelidad(puntosFidelidad);
	    } else if (cliente_final instanceof Cliente) {
	        Cliente cliente = (Cliente) cliente_final;
	        cliente.sumarPuntosFidelidad(puntosFidelidad);
	    }
	    
	    
	    boolean tienePremio50 = false;
	    if (cliente_final instanceof Cliente) {
	        Cliente cliente = (Cliente) cliente_final;
	        String premio = cliente.getPremio();
	        tienePremio50 = (premio != null && premio.contains("50"));
	        
	        if (tienePremio50) {
	            double descuento = total * 0.5;
	            total -= descuento;
	            cliente.agregarPremio("");
	        }
	    }
	    
	    if (!tienePremio50) {
	        if (cliente_final instanceof Empleado) {
	            double descuento = total * this.DESCUENTO_EMPLEADO;
	            total -= descuento;
	        } else if (this.amigoEmpleado) {
	            double descuento = total * this.DESCUENTO_AMIGO_EMPLEADO;
	            total -= descuento;
	        }
	    }
	    
	    registrarTransaccion(this);
	    
	    return (int) total;
	}
	
}
