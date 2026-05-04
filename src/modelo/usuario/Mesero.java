package modelo.usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//exceptions
import exceptions.*;

//modelo
import modelo.producto.*;
import modelo.*;


public class Mesero extends Empleado{
	
	private Cafe miCafe;
	private ArrayList<Reserva> reservasAsignadas;
	private ArrayList<JuegoDificil> juegosConocidos;
	
	//Constructor
	public Mesero(int id, String login, String password, String nombre) throws UsuariosException {
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
	public boolean conoceJuego(Juego juego) {
	    for (JuegoDificil juegoConocido : juegosConocidos) {
	        if (juegoConocido.getId() == juego.getId() || 
	            juegoConocido.getNombre().equals(juego.getNombre())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public ArrayList<Reserva> getReservasAsignadas() {
		return reservasAsignadas;
	}
	public void nuevaReserva(Reserva reserva, Calendar fecha) {
		if (!reservasAsignadas.contains(reserva) && libreParaReserva(fecha)) {
		reservasAsignadas.add(reserva);
		}
	}
		
	//Métodos
	public boolean libreParaReserva(Calendar fecha) {
		return reservasAsignadas.size() < 2 && trabajaEnFecha(fecha) ;
	}

	//PRESTAR JUEGOS
	public void autorizarPrestamo(Reserva r, Juego juego) throws JuegoNoAptoException {
	    if (r.getNumPersonas() > juego.getNumJugadores()) {
	        throw new JuegoNoAptoException("El juego requiere " + juego.getNumJugadores() + " jugadores o menos.");
	    }
	    if (juego.getRestriccionEdad().equals("Adultos") && r.edadMinima() < 18) {
	        throw new JuegoNoAptoException("Juego para adultos con menores presentes");
	    }
	    if (juego.getCategoria().equals("Acción") && r.tieneBebidasCalientes()) {
	        throw new JuegoNoAptoException("No se pueden servir juegos de acción con bebidas calientes");
	    }
	    
	    if (r.getJuegosPrestados().size() >= 2) {
	        throw new JuegoNoAptoException("La reserva ya tiene el máximo de 2 juegos");
	    }
	    if (miCafe.estaJuegoReservadoEnFecha(juego, r.getFecha())) {
	        throw new JuegoNoAptoException("El juego ya está reservado en esta fecha");
	    }
	    
	    if (juego instanceof JuegoDificil) {
	        if (!this.juegosConocidos.contains(juego)) { // En un futuro se espera que se pueda imprimir las instrucciones si es dificil
	            r.pedirCambioMesero(r.getFecha(), juego);
	        }
	    }
	}
	
	
	//SERVIR COMIDA
	public void servirPlatillos(Reserva r, Platillo p) {
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    if (cocineroDeTurno == null || !cocineroDeTurno.getPlatillosConocidos().contains(p)) {
	        return;
	    }

	    boolean aptoParaTodos = true;
	    for (Cliente c : r.getClientes()) {
	    	
	        for (String ingrediente : p.getAlergeneos()) {
	            if (c.getAlergenos().contains(ingrediente)) {
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

	    if (r.edadMinima()<18 && b.isTieneAlcohol()) {
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
