package modelo.producto;

import exceptions.*;

public class JuegoDificil extends Juego{
	
	
	private String instrucciones;
		
	public JuegoDificil(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, int numJugadores,
			String restriccionEdad, String categoria,String instrucciones) throws ProductosException{
		super(id, precio, nombre, anioPublicacion, empresMatriz, numJugadores, restriccionEdad, categoria);
		 if (instrucciones == null || instrucciones.trim().isEmpty()) {
		        throw new ProductosException(this, "instrucciones", 
		            "Las instrucciones no pueden estar vacías.");
		    }
		    this.instrucciones = instrucciones;
	}
	
	//Getters Y Setters
	public String getInstrucciones() {
		return instrucciones;
	}
	
	public void setInstrucciones(String instrucciones) throws ProductosException {
	    if (instrucciones == null || instrucciones.trim().isEmpty()) {
	        throw new ProductosException(this, "instrucciones", 
	            "Las instrucciones no pueden estar vacías.");
	    }
	    this.instrucciones = instrucciones;
	}
	
	//Métodos
	@Override
	public boolean requiereInstructor() {
		return true;
	}
	

}
