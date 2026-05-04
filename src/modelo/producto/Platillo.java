package modelo.producto;

import java.util.ArrayList;

import exceptions.ProductosException;

public class Platillo extends Producto{

	protected ArrayList<String> alergeneos; 
	
	// Constructor
	public Platillo(int id, int precio, String nombre, ArrayList<String> alergenos) 
            throws ProductosException {
        super(id, precio, nombre);
        
        if (alergenos == null) {
            throw new ProductosException(this, "alergenos", 
                "La lista de alergenos no puede ser nula.");
        }
        
        for (int i = 0; i < alergenos.size(); i++) {
            String alergeno = alergenos.get(i);
            
            if (alergeno == null) {
                throw new ProductosException(this, "alergenos", 
                    "El alergeno en la posición " + i + " no puede ser nulo.");
            }
            
            if (alergeno.trim().isEmpty()) {
                throw new ProductosException(this, "alergenos", 
                    "El alergeno en la posición " + i + " no puede estar vacío.");
            }
            
            if (alergeno.matches(".*\\d.*")) {
                throw new ProductosException(this, "alergenos", 
                    "El alergeno '" + alergeno + "' no puede contener números.");
            }
        }
        
        this.alergeneos = new ArrayList<>(alergenos); 
    }
	
	//Getters y Setters
	public ArrayList<String> getAlergeneos() {
		return alergeneos;
	}


	//TRANSACCIÓN
	@Override
	public double getTasaImpuesto() {
	    return super.IMPUESTOCONSUMO; 
	}
	
}
