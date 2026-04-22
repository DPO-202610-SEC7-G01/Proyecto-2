package modelo;

import java.util.Calendar;
import java.util.List;

import modelo.producto.Producto;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

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
	public Transaccion(int id, Calendar fecha, List<Producto> productos, Usuario usuario, boolean amigoEmpleado) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.productos = productos;
		this.cliente_final = usuario;
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
	
	public int calcularTotal() {
		double total = 0;
		for (int i = 0; i < this.productos.size(); i++) {
			Producto producto = this.productos.get(i);
			total += producto.calcularPrecioFinal();
		}
		
		if ( cliente_final instanceof Empleado) {
			double descuento  = total * this.DESCUENTO_EMPLEADO;
			total -= descuento;
		}
		else if(this.amigoEmpleado) {
			double descuento  = total * this.DESCUENTO_AMIGO_EMPLEADO;
			total -= descuento;
		}
		
		return (int) total;
	}
	
	
	
}
