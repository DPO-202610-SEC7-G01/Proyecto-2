package modelo.usuario;

import java.util.ArrayList;
import java.util.List;

import modelo.producto.Bebida;
import modelo.producto.Platillo;

public class Cocinero extends Empleado{
	private List<Platillo> platillosConocidos;
	private List<Bebida> bebidasConocidas;
	
	//Constructor
	public Cocinero(int id, String login, String password, String nombre) {
		super(id, login, password, nombre);
		this.bebidasConocidas = new ArrayList<Bebida>();
		this.platillosConocidos = new ArrayList<Platillo>();
		
	}
	
	//Getters y Setters
	public List<Platillo> getPlatillosConocidos() {
		return platillosConocidos;
	}
	public List<Bebida> getBebidasConocidas() {
		return bebidasConocidas;
	}
	
	//Métodos
	public void aprenderPLatillo(Platillo platillo) {
		this.platillosConocidos.add(platillo);
	}
	
	public void aprenderBebida(Bebida bebida) {
		this.bebidasConocidas.add(bebida);
	}
	

}
