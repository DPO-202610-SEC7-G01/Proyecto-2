package modelo.producto;

//Exceptions 
import exceptions.*;

public class Bebida extends Producto{
	
	
	private boolean tieneAlcohol;
	private String temperatura;

	//Constructor
	public Bebida(int id, int precio, String nombre, String temperatura, boolean alcohol) throws ProductosException {
		super(id, precio, nombre);
		 if (temperatura == null || temperatura.trim().isEmpty()) {
	            throw new ProductosException(this, "temperatura", 
	                "La temperatura no puede estar vacía.");
	        }
	        
	      if (!temperatura.replace("a", "o").equalsIgnoreCase("Frío") && !temperatura.equalsIgnoreCase("Caliente")) {
	            throw new ProductosException(this, "temperatura", 
	                "La temperatura debe ser 'Frío' o 'Caliente'. Valor recibido: " + temperatura );

	        }
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
	//TRANSACCIÓN
	@Override
	public double getTasaImpuesto() {
	    return super.IMPUESTOCONSUMO; 
	}

}
