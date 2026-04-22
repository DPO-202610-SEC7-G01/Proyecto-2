package modelo.producto;

public class Bebida extends Producto{
	
	
	private boolean tieneAlcohol;
	private String temperatura;

	//Constructor
	public Bebida(int id, int precio, String nombre, String temperatura, boolean alcohol) {
		super(id, precio, nombre);
		this.temperatura = temperatura ;
		this.tieneAlcohol = alcohol;
	}
	
	public Bebida(int id, int precio, String nombre) {
		super(id, precio, nombre);
		this.temperatura = "Frío" ;
		this.tieneAlcohol = false;
	}

	//Getters y Setters
	public boolean isTieneAlcohol() {
		return tieneAlcohol;
	}

	public void setTieneAlcohol(boolean tieneAlcohol) {
		this.tieneAlcohol = tieneAlcohol;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}
	
	// Métodos
	@Override
	public double getTasaImpuesto() {
	    return super.IMPUESTOCONSUMO; // IVA estándar para alimentos procesados/pastelería
	}

	@Override
	public String getCategoriaProducto() {
		return "Bebida";
	}
	
	
		
}
