package modelo.producto;

import java.util.ArrayList;

public class Platillo extends Producto{

	protected ArrayList<String> alergeneos; 
	// Constructor
	public Platillo(int id, int precio, String nombre, ArrayList<String> alergenos) {
		super(id, precio, nombre);
		this.alergeneos = alergenos;
	}
	
	//Getters y Setters
	public ArrayList<String> getAlergeneos() {
		return alergeneos;
	}
	public void setAlergeneos(ArrayList<String> alergeneos) {
		this.alergeneos = alergeneos;
	}

	//TRANSACCIÓN
	@Override
	public double getTasaImpuesto() {
	    return super.IMPUESTOCONSUMO; 
	}
	
}
