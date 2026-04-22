package modelo.producto;

public class Platillo extends Producto{

	protected String alergeneos; 
	// Constructor
	public Platillo(int id, int precio, String nombre, String alergenos) {
		super(id, precio, nombre);
		this.alergeneos = alergenos;
	}
	
	//Getters y Setters
	
	
	public String getAlergeneos() {
		return alergeneos;
	}
	public void setAlergeneos(String alergeneos) {
		this.alergeneos = alergeneos;
	}

	@Override
	public double getTasaImpuesto() {
	    return super.IMPUESTOCONSUMO; 
	}

	@Override
	public String getCategoriaProducto() {
		return "Platillo";
	}
	
	
	//Métodos
	
	
}
