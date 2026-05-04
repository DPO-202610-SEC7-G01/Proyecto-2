package modelo.usuario;

import java.util.ArrayList;

import exceptions.InvalidCredentialsException;
import exceptions.UsuariosException;
import modelo.producto.Bebida;
import modelo.producto.Platillo;

public class Cocinero extends Empleado{
	private ArrayList<Platillo> platillosConocidos;
	private ArrayList<Bebida> bebidasConocidas;
	
	//Constructor
	public Cocinero(int id, String login, String password, String nombre) throws InvalidCredentialsException, UsuariosException {
		super(id, login, password, nombre);
		this.bebidasConocidas = new ArrayList<Bebida>();
		this.platillosConocidos = new ArrayList<Platillo>();
		
	}
	
	//Getters y Setters
	public ArrayList<Platillo> getPlatillosConocidos() {
		return platillosConocidos;
	}
	public void aprenderPlatillo(Platillo platillo) {
	    if (platillo == null) {
	        throw new IllegalArgumentException("El platillo no puede ser nulo");
	    }
	    this.platillosConocidos.add(platillo);
	}
	
	public ArrayList<Bebida> getBebidasConocidas() {
		return bebidasConocidas;
	}
	public void aprenderBebida(Bebida bebida) {
	    if (bebida == null) {
	        throw new IllegalArgumentException("La bebida no puede ser nula");
	    }
	    this.bebidasConocidas.add(bebida);
	}

}
