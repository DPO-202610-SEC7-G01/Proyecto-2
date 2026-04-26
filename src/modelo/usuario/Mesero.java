package modelo.usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import modelo.producto.*;
import modelo.*;


public class Mesero extends Empleado{
	private ArrayList<JuegoDificil> juegosConocidos;
	private Cafe miCafe;
	private ArrayList<Reserva> reservasAsignadas;
	
	//Constructor
	public Mesero(int id, String login, String password, String nombre) {
		super(id, login, password, nombre);
		this.juegosConocidos= new ArrayList<>();
		this.reservasAsignadas = new ArrayList<>();
	}
	
	//Getters y Setters	
	public List<JuegoDificil> getJuegosConocidos() {
		return juegosConocidos;
	}
	public void aprenderJuegoDificil(JuegoDificil juego) {
		juegosConocidos.add(juego);
	}

	public ArrayList<Reserva> getReservasAsignadas() {
		return reservasAsignadas;
	}
	public void nuevaReserva(Reserva reserva, Calendar fecha) {
		if (libreParaReserva(fecha)) {
		reservasAsignadas.add(reserva);
		}
	}
		
	//Métodos
	public boolean libreParaReserva(Calendar fecha) {
		return reservasAsignadas.size() < 2 && trabajaEnFecha(fecha) ;
	}
	
	
	public boolean autorizarPrestamo(Reserva r, Juego juego) {
	    if (r.getNumPersonas() < juego.getNumJugadores() || r.getNumPersonas() > juego.getNumJugadores()) {
	        return false;
	    }

	    if (juego.getRestriccionEdad().equals("apto adultos") && r.tieneMenoresDeEdad()) {
	        return false;
	    }

	    if (juego.getCategoria().equals("Acción") && r.tieneBebidasCalientes()) {
	        return false;
	    }

	    r.agregarAlPrestamo(juego);
	    return true;
	}
	
	public void servirPlatillos(Reserva r, Platillo p) {
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    if (cocineroDeTurno == null || !cocineroDeTurno.getPlatillosConocidos().contains(p)) {
	        return;
	    }

	    boolean aptoParaTodos = true;
	    for (Cliente c : r.getClientes()) {
	        for (String ingrediente : p.getAlergeneos().split(",")) {
	            if (c.getAlergenos().toLowerCase().contains(ingrediente.trim().toLowerCase())) {
	                aptoParaTodos = false;
	                break; 
	            }
	        }
	        if (!aptoParaTodos) break;
	    }

	    if (aptoParaTodos) {
	        r.addTransaccion(p);
	    } 
	}

	public void servirBebidas(Reserva r, Bebida b) {
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    
	    if (cocineroDeTurno == null || !cocineroDeTurno.getBebidasConocidas().contains(b)) {
	        return;
	    }

	    if (r.tieneMenoresDeEdad() && b.isTieneAlcohol()) {
	        return;
	    }

	    boolean tieneJuegoAccion = false;
	    for (Juego j : r.getJuegosPrestados()) {
	        if (j.getCategoria().equalsIgnoreCase("Acción")) {
	            tieneJuegoAccion = true;
	            break;
	        }
	    }

	    if (tieneJuegoAccion && b.getTemperatura().equalsIgnoreCase("Caliente")) {
	        return;
	    }
	    r.addTransaccion(b);
	}
	

}
