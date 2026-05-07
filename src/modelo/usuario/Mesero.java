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
	public void setCafe(Cafe cafe) {
	    this.miCafe = cafe;
	}
	
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
		return reservasAsignadas.size() < 2 && trabajaEnFecha(fecha) ; //Sino debe arrojar un error 
	}

	//PRESTAR JUEGOS
	public void autorizarPrestamo(Reserva r, Juego juego) throws UsuariosException {
	    
	    // Validación por número de jugadores
	    if (r.getNumPersonas() > juego.getNumJugadores()) {
	        throw new UsuariosException(juego, juego.getNumJugadores(), r.getNumPersonas());
	    }
	    
	    // Validación por edad (juego para adultos)
	    if (juego.getRestriccionEdad().equals("Adultos") && r.edadMinima() < 18) {
	        throw new UsuariosException(r.getClientes().get(0), juego, 18);
	    }
	    
	    // Validación por categoría Acción con bebidas calientes
	    if (juego.getCategoria().equals("Acción") && r.tieneBebidasCalientes()) {
	        throw new UsuariosException(juego, UsuariosException.RAZON_CATEGORIA, 
	            "No se pueden servir juegos de acción con bebidas calientes");
	    }
	    
	    if (r.getJuegosPrestados().size() >= 2) {
	        throw new UsuariosException(juego, UsuariosException.RAZON_LIMITE, 
	            "La reserva ya tiene el máximo de 2 juegos");
	    }
	    
	    if (miCafe.estaJuegoReservadoEnFecha(juego, r.getFecha())) {
	        throw new UsuariosException(juego, UsuariosException.RAZON_DISPONIBILIDAD, 
	            "El juego ya está reservado en esta fecha");
	    }
	    
	    if (juego instanceof JuegoDificil) {
	        if (!this.juegosConocidos.contains(juego)) {
	            r.pedirCambioMesero(r.getFecha(), juego);
	        }
	    }
	}
	
	
	//SERVIR COMIDA
	public void servirPlatillos(Reserva r, Platillo p) throws UsuariosException {
	    
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    if (cocineroDeTurno == null || !conocePlatillo(cocineroDeTurno, p)) {
	        cocineroDeTurno = buscarCocineroParaPlatillo(p);
	    }

	    if (cocineroDeTurno == null) {
	        throw new UsuariosException(r, UsuariosException.RAZON_COCINERO_NO_DISPONIBLE,
	            "No hay ningún cocinero disponible que prepare este platillo.");
	    }
	    
	    ArrayList<Cliente> clientesAfectados = new ArrayList<>();
	    ArrayList<String> ingredientesPeligrosos = new ArrayList<>();
	    
	    for (Cliente cliente : r.getClientes()) {
	        for (String ingrediente : p.getAlergeneos()) {
	            if (cliente.getAlergenos() != null && cliente.getAlergenos().contains(ingrediente)) {
	                if (!clientesAfectados.contains(cliente)) {
	                    clientesAfectados.add(cliente);
	                }
	                if (!ingredientesPeligrosos.contains(ingrediente)) {
	                    ingredientesPeligrosos.add(ingrediente);
	                }
	                break; 
	            }
	        }
	    }
	    
	    if (!clientesAfectados.isEmpty()) {
	        if (clientesAfectados.size() == 1 && ingredientesPeligrosos.size() == 1) {
	            throw new UsuariosException(clientesAfectados.get(0), p, 
	                ingredientesPeligrosos.get(0), r);
	        } 
	    }
	    r.addTransaccion(p);
	}
	
	public void servirBebidas(Reserva r, Bebida b) throws UsuariosException {
	    Cocinero cocineroDeTurno = miCafe.turnoCocineros(r.getFecha());
	    if (cocineroDeTurno == null || !conoceBebida(cocineroDeTurno, b)) {
	        cocineroDeTurno = buscarCocineroParaBebida(b);
	    }

	    if (cocineroDeTurno == null) {
	        throw new UsuariosException(r, UsuariosException.RAZON_COCINERO_NO_DISPONIBLE,
	            "No hay ningún cocinero disponible que prepare esta bebida.");
	    }	    
	    if (b.isTieneAlcohol() && r.edadMinima() < 18) {
	        throw new UsuariosException(r, UsuariosException.RAZON_EDAD, 
	            "No se pueden servir bebidas alcohólicas. La edad mínima de la reserva es " + 
	            r.edadMinima() + " años, se requieren 18 años.");
	    }
	    
	    boolean tieneJuegoAccion = false;
	    for (Juego j : r.getJuegosPrestados()) {
	        if (j.getCategoria().equalsIgnoreCase("Acción")) {
	            tieneJuegoAccion = true;
	            break;
	        }
	    }
	    
	    if (tieneJuegoAccion && b.getTemperatura().equalsIgnoreCase("Caliente")) {
	        throw new UsuariosException(b, UsuariosException.RAZON_CATEGORIA,
	            "No se pueden servir bebidas calientes mientras se juegan juegos de acción " +
	            "por riesgo de derrames y quemaduras.");
	    }
	    
	    r.addTransaccion(b);
	}

	private Cocinero buscarCocineroParaPlatillo(Platillo platillo) {
	    for (Empleado empleado : miCafe.getEmpleados()) {
	        if (empleado instanceof Cocinero) {
	            Cocinero cocinero = (Cocinero) empleado;
	            if (conocePlatillo(cocinero, platillo)) {
	                return cocinero;
	            }
	        }
	    }
	    return null;
	}

	private Cocinero buscarCocineroParaBebida(Bebida bebida) {
	    for (Empleado empleado : miCafe.getEmpleados()) {
	        if (empleado instanceof Cocinero) {
	            Cocinero cocinero = (Cocinero) empleado;
	            if (conoceBebida(cocinero, bebida)) {
	                return cocinero;
	            }
	        }
	    }
	    return null;
	}

	private boolean conocePlatillo(Cocinero cocinero, Platillo platillo) {
	    for (Platillo conocido : cocinero.getPlatillosConocidos()) {
	        if (conocido.getId() == platillo.getId() || conocido.getNombre().equalsIgnoreCase(platillo.getNombre())) {
	            return true;
	        }
	    }
	    return false;
	}

	private boolean conoceBebida(Cocinero cocinero, Bebida bebida) {
	    for (Bebida conocida : cocinero.getBebidasConocidas()) {
	        if (conocida.getId() == bebida.getId() || conocida.getNombre().equalsIgnoreCase(bebida.getNombre())) {
	            return true;
	        }
	    }
	    return false;
	}
	

}
