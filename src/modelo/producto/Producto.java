package modelo.producto;

public  abstract class Producto {
	private int id;
	private int precio;
	private String nombre;
	public  final double IVA = 0.19;
    public final double IMPUESTOCONSUMO = 0.08;
	

	//Constructor
	public Producto(int id, int precio,String nombre) {
		this.id = id;
		this.precio = precio;
		this.nombre = nombre;
	}
	
	//Getter y Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	//métodos
	public abstract double getTasaImpuesto();
	
	public double calcularPrecioFinal() {
        return this.precio * (1 + getTasaImpuesto());
    }

	
	public abstract String getCategoriaProducto();
	
}
