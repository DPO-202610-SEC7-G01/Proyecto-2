package modelo.usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import modelo.producto.Juego;
import modelo.producto.Producto;
import modelo.Transaccion;

public class Cliente extends Usuario {
	private int edad;
	private String alergenos;
	private int puntosFidelidad;
	private boolean amigos;
	private List<Juego> juegosFavoritos;
	
	//Constructor
	public Cliente(int id, String login, String password, String nombre, int edad,String alergenos) {
		super(id, login, password, nombre);
		this.edad = edad;
		this.puntosFidelidad = 0;
		this.alergenos= alergenos;
		this.juegosFavoritos = new ArrayList<Juego>();
		this.amigos = false;
	}
	
	//Getters Y Setter
	public int getPuntosFidelidad() {
		return puntosFidelidad;
	}
	public int getEdad() {
		return edad;
	}
	public List<Juego> getJuegosFavoritos() {
		return juegosFavoritos;
	}
	
	public String getAlergenos() {
		return alergenos;
	}
	//Métodos
	public void nuevoAmigo() {
		amigos = true;
	}
	
	public void agregarJuegoFavorito(Juego juego) {
		this.juegosFavoritos.add(juego);
	}
	
	public void sumarPuntosFidelidad(int puntosFidelidad) {
		this.puntosFidelidad += puntosFidelidad;
	}
	
	public Transaccion generarTransaccion(List<Producto> productosComprados, int idNuevaTransaccion) {
	    Calendar hoy = Calendar.getInstance();
	    Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, this, this.amigos);    
	    this.sumarPuntosFidelidad(productosComprados.size() ); 
	    return factura; // Existe un método que calcula el monto final c: 
	}
	
	
}
