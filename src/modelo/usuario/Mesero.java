package modelo.usuario;

import java.util.ArrayList;
import java.util.List;
import modelo.producto.JuegoDificil;
import modelo.producto.Platillo;
import modelo.producto.Bebida;
import modelo.producto.Juego;
import modelo.Reserva;
import modelo.Cafe;


public class Mesero extends Empleado{
	private List<JuegoDificil> juegosConocidos;
	private Cafe miCafe;
	
	//Constructor
	public Mesero(int id, String login, String password, String nombre) {
		super(id, login, password, nombre);
		this.juegosConocidos= new ArrayList<>();
	}
	
	//Métodos
	public void aprenderJuegoDificil(JuegoDificil juego) {
		juegosConocidos.add(juego);
	}
	
	public boolean autorizarPrestamo(Reserva r, Juego juego) {
	    // 1. Validar número de jugadores
	    if (r.getNumPersonas() < juego.getNumJugadores() || r.getNumPersonas() > juego.getNumJugadores()) {
	        return false;
	    }

	    // 2. Validar restricción de edad
	    if (juego.getRestriccionEdad().equals("apto adultos") && r.tieneMenoresDeEdad()) {
	        return false;
	    }

	    // 3. Validar bebidas calientes vs Juego de Acción
	    if (juego.getCategoria().equals("Acción") && r.tieneBebidasCalientes()) {
	        return false;
	    }

	    r.agregarAlPrestamo(juego);
	    return true;
	}
	
	public void servirPlatillos(Reserva r, Platillo p) {
	    // 1. Obtener cocinero de turno desde la instancia actual (this)
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    
	    // Validaciones silenciosas
	    if (cocineroDeTurno == null || !cocineroDeTurno.getPlatillosConocidos().contains(p)) {
	        return;
	    }

	    // 2. Validación de alérgenos
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

	    // 3. Ejecución de la transacción
	    if (aptoParaTodos) {
	        r.addTransaccion(p);
	    } 
	}

	public void servirBebidas(Reserva r, Bebida b) {
	    // 1. Obtener cocinero de turno
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    
	    if (cocineroDeTurno == null || !cocineroDeTurno.getBebidasConocidas().contains(b)) {
	        return;
	    }

	    // 2. Regla de alcohol y menores
	    if (r.tieneMenoresDeEdad() && b.isTieneAlcohol()) {
	        return;
	    }

	    // 3. Regla de seguridad (Juegos de Acción + Bebidas Calientes)
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

	    // 4. Ejecución de la transacción
	    r.addTransaccion(b);
	}
	

}
