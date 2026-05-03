package modelo.usuario;

import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidCredentialsException;
import modelo.producto.Bebida;
import modelo.producto.Platillo;

public class Cocinero extends Empleado{
	private List<Platillo> platillosConocidos;
	private List<Bebida> bebidasConocidas;
	
	//Constructor
	public Cocinero(int id, String login, String password, String nombre) throws InvalidCredentialsException {
		super(id, login, password, nombre);
		this.bebidasConocidas = new ArrayList<Bebida>();
		this.platillosConocidos = new ArrayList<Platillo>();
		
	}
	
	//Getters y Setters
	public List<Platillo> getPlatillosConocidos() {
		return platillosConocidos;
	}
	public void aprenderPlatillo(Platillo platillo) {
	    if (platillo == null) {
	        throw new IllegalArgumentException("El platillo no puede ser nulo");
	    }
	    this.platillosConocidos.add(platillo);
	}
	
	public List<Bebida> getBebidasConocidas() {
		return bebidasConocidas;
	}
	public void aprenderBebida(Bebida bebida) {
	    if (bebida == null) {
	        throw new IllegalArgumentException("La bebida no puede ser nula");
	    }
	    this.bebidasConocidas.add(bebida);
	}

}
