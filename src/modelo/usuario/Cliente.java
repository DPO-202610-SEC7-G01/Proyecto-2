package modelo.usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import exceptions.*;
import modelo.producto.*;
import modelo.*;

public class Cliente extends Usuario {
	private int edad;
	private ArrayList<String> alergenos;
	private int puntosFidelidad;
	private boolean amigos;
	private ArrayList<Juego> juegosFavoritos;
	private String premio;
	private ArrayList<Torneo> torneosInscritos;
	
	//Constructor
	public Cliente(int id, String login, String password, String nombre, int edad,ArrayList <String> alergenos) {
		super(id, login, password, nombre);
		this.edad = edad;
		this.puntosFidelidad = 0;
		this.alergenos= new ArrayList<String>();
		this.juegosFavoritos = new ArrayList<Juego>();
		this.amigos = false;
		this.premio = "";
		this.torneosInscritos = new ArrayList<>();

	}
	
	//Getters Y Setter
	public int getPuntosFidelidad() {
		return puntosFidelidad;
	}
	public int getEdad() {
		return edad;
	}
	public ArrayList<Juego> getJuegosFavoritos() {
		return juegosFavoritos;
	}
	
	public ArrayList<String> getAlergenos() {
		return alergenos;
	}
	
	public Boolean getAmigos() {
		return this.amigos;
	}
	
	public void setAmigos(boolean amigo) {
		this.amigos= amigo;
	}
	
		
	public ArrayList<Torneo> getTorneosInscritos() {
		return torneosInscritos;
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
	
	public String getPremio() {
		return premio;
	}
	public void agregarPremio(String premio) {
		this.premio= premio;
	}
	
	//Métodos
	//TORNEOS
	public void inscribirseTorneo(String nombreTorneo, Cafe miCafe) throws TorneoException {
	    Torneo torneo = null;
	    
	    for (Torneo t : miCafe.getTorneosActivos()) {
	        if (t.getJuego().getNombre().equalsIgnoreCase(nombreTorneo) && t.isActivo()) {
	            torneo = t;
	            break;
	        }
	    }
	    
	    if (torneo == null) {
	        throw TorneoException.torneoNoEncontrado(nombreTorneo);
	    }
	    
	    if (torneosInscritos.size() >= 3) {
	        throw TorneoException.excesoTorneos(3);
	    }
	    
	    if (torneosInscritos.contains(torneo)) {
	        throw TorneoException.yaInscrito(nombreTorneo);
	    }
	    
	    torneo.agregarParticipantes(this); // Hay que verificar que si hayan cupos 
	    torneosInscritos.add(torneo);
	}
	
	public void desinscribirseDeTodosLosTorneos() {
	    for (Torneo torneo : torneosInscritos) {
	        torneo.eliminarParticipante(this);
	    }
	    torneosInscritos.clear();
	}
	
	public boolean esFanatico(Juego juego) {
	    for (Juego juegoFav : juegosFavoritos) {
	        if (juegoFav.getId() == juego.getId()) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	
}
