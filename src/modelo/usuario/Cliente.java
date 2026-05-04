package modelo.usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//exceptions
import exceptions.*;

//modelos
import modelo.producto.*;
import modelo.*;

public class Cliente extends Usuario {
	private int edad;
	private String premio;
	private boolean amigos;
	private int puntosFidelidad;
	private ArrayList<String> alergenos;
	private ArrayList<Juego> juegosFavoritos;
	private ArrayList<Torneo> torneosInscritos;
	
	public Cliente(int id, String login, String password, String nombre, int edad, ArrayList<String> alergenos) 
            throws UsuariosException {
        super(id, login, password, nombre);
        
        if (alergenos != null) {
            for (String alergeno : alergenos) {
                if (alergeno == null || alergeno.trim().isEmpty()) {
                    throw new UsuariosException(this, "alergenos", 
                        "El alergeno no puede ser nulo o estar vacío.");
                }
                if (alergeno.matches(".*\\d.*")) {
                    throw new UsuariosException(this, "alergenos", 
                        "El alergeno no puede contener números. Valor recibido: '" + alergeno + "'");
                }
            }
            this.alergenos = new ArrayList<>(alergenos);
        } else {
            this.alergenos = new ArrayList<>();
        }
        
        if (edad <= 0) {
            throw new UsuariosException(this, "edad", 
                "La edad debe ser un número positivo mayor a cero. Valor recibido: " + edad);
        }
        
        this.edad = edad;
        
        this.juegosFavoritos = new ArrayList<Juego>();
        this.torneosInscritos = new ArrayList<>();
        this.puntosFidelidad = 0;
        this.amigos = false;
        this.premio = "";
    }
	
	//Getters Y Setter
	public int getPuntosFidelidad() {
		return puntosFidelidad;
	}
	public void sumarPuntosFidelidad(int puntosFidelidad) throws UsuariosException {
	    if (puntosFidelidad <= 0) {
	        throw new UsuariosException(this, "puntosFidelidad", 
	            "Los puntos a sumar deben ser positivos. Valor recibido: " + puntosFidelidad);
	    }
	    this.puntosFidelidad += puntosFidelidad;
	}
	
	public int getEdad() {
		return edad;
	}
	public ArrayList<Juego> getJuegosFavoritos() {
		return juegosFavoritos;
	}
	public void agregarJuegoFavorito(Juego juego) {
		this.juegosFavoritos.add(juego);
	}
	
	public ArrayList<String> getAlergenos() {
		return alergenos;
	}
	
	public Boolean getAmigos() {
		return this.amigos;
	}
	
	public void nuevoAmigo() {// acá hay que ver que funcione la integración con los empleados  (NO HACER POR EL MOMENTO) 
		amigos = true;
	}

	public void setAmigos(boolean amigos) {
	    this.amigos = amigos;
	}
	
	public ArrayList<Torneo> getTorneosInscritos() {
		return torneosInscritos;
	}

	public String getPremio() {
		return premio;
	}
	public void agregarPremio(String premio) {
		this.premio= premio;
	}
	
	//Métodos
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
	    
	    torneo.agregarParticipantes(this); 
	    torneosInscritos.add(torneo);
	}
	
	public void desinscribirseDeTodosLosTorneos() {
	    for (Torneo torneo : torneosInscritos) {
	        torneo.eliminarParticipante(this);
	    }
	    torneosInscritos.clear();
	}
	public Transaccion generarTransaccion(List<Producto> productosComprados, int idNuevaTransaccion) { // esto no debería ir acá  (IGNORAR POR EL MOMENTO)
		Calendar hoy = Calendar.getInstance();
		Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, this, this.amigos);
		return factura; // Existe un método que calcula el monto final c:
	}
}
