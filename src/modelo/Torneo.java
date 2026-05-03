package modelo;

import java.util.ArrayList;
import java.util.Calendar;

import exceptions.*;
import modelo.producto.Juego;
import modelo.usuario.*;

public class Torneo {
	private int numParticipantes;
    private String tipo;
    private Juego juego;
    private int precio;
    private boolean activo;
	private Cafe miCafe;
	private Calendar fecha;
	private String nombre;
	private String premio;
	private ArrayList<Usuario> participantes;
	private ArrayList<Usuario> fanaticos;
	
	
	
	
	//Constructor
    public Torneo(String tipo, String nombre, Juego juego, int numParticipantes, int precio)
    		throws NumeroJugadoresExcedidoException {
        this.tipo = tipo; // solo puede ser amistoso o competitivo
        this.nombre = nombre; // cualqquiera 
        this.juego = juego; 
        this.precio = precio; // si es amistoso debe ser gratis
        this.activo = true; // obvio
        
        this.fanaticos = new ArrayList<Usuario>();
        this.participantes = new ArrayList<Usuario>();
        this.fecha = Calendar.getInstance(); // debe ser el día de hoy 
        this.premio = "";
        validarYAsignarParticipantes(juego, numParticipantes, miCafe);
        agregarFanaticosDelJuego();
        
    
    }
    
    // Getters y Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Juego getJuego() { return juego; }
    public void setJuego(Juego juego) { this.juego = juego; }
    
    public int getNumParticipantes() { return numParticipantes; }
    public void setNumParticipantes(int numParticipantes) { this.numParticipantes = numParticipantes; }
    
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public boolean esAmistoso() {
        return tipo.equalsIgnoreCase("Amistoso");
    }
    
    public boolean esCompetitivo() {
        return tipo.equalsIgnoreCase("Competitivo");
    }
    
    public Calendar getFecha() {
		return fecha;
	}

	public ArrayList<Usuario> getParticipantes() {
		return participantes;
	}
    
    public void agregarParticipantes(Usuario participante) throws TorneoException {
        if (participantes.contains(participante)) {
            throw new TorneoException("El participante ya está inscrito en este torneo", "YA_INSCRITO");
        }
        
        if (!hayCupoDisponible(participante)) { 
            boolean fanatico = esFanatico(participante);
            String tipoCupo = fanatico ? "fanáticos" : "normales";
            throw new TorneoException("No hay cupos " + tipoCupo + " disponibles", "SIN_CUPOS");
        }
        
        participantes.add(participante);
    }
    
	public void eliminarParticipante(Usuario participante) { 
		participantes.remove(participante);
	}
	
	
	//Métodos   

	private void agregarFanaticosDelJuego() { 	//Agregar los fanáticos del juego 	
        if (miCafe.getClientes() != null) {
            for (Cliente cliente : miCafe.getClientes()) {
                if (cliente.getJuegosFavoritos() != null && cliente.getJuegosFavoritos().contains(juego)) {
                    if (!fanaticos.contains(cliente)) {
                        fanaticos.add(cliente);
                    }
                }
            }
        }
        if (miCafe.getEmpleados() != null) {
            for (Empleado empleado : miCafe.getEmpleados()) {
                if (empleado.getJuegosFavoritos() != null && empleado.getJuegosFavoritos().contains(juego)) {
                    if (!fanaticos.contains(empleado)) {
                        fanaticos.add(empleado);
                    }
                }
            }
        }
	}
	
	
	
	 private boolean esFanatico(Usuario usuario) {
	   ArrayList<Juego> juegosFavoritos = null;
	        
	   if (usuario instanceof Cliente) {
	            juegosFavoritos = ((Cliente) usuario).getJuegosFavoritos();
	   } else if (usuario instanceof Empleado) {
	            juegosFavoritos = ((Empleado) usuario).getJuegosFavoritos();
	   }
	   if (juegosFavoritos != null) {
	      for (Juego juegoFav : juegosFavoritos) {
	           if (juegoFav.getId() == this.juego.getId()) {
	                return true;
	            }
	        }
	    }
	        return false;
	    }
	
	 
	 
	//Verificar Cupos Disponibles
	private boolean hayCupoDisponible(Usuario participante) {
	    int cuposFanaticos = (int) Math.ceil(numParticipantes * 0.2);
	    int cuposFanaticosUsados = 0;
	    
	    for (Usuario u : participantes) {
	        if (esFanatico(u)) {
	            cuposFanaticosUsados++;
	        }
	    }
	    
	    int cuposNormales = numParticipantes - cuposFanaticos;
	    int cuposNormalesUsados = participantes.size() - cuposFanaticosUsados;
	    
	    boolean fanatico = esFanatico(participante);
	    
	    if (fanatico) {
	        if (cuposFanaticosUsados < cuposFanaticos) {
	            return true;
	        }
	        return cuposNormalesUsados < cuposNormales;
	    } else {
	        return cuposNormalesUsados < cuposNormales;
	    }
	}
	
	
    //definición del método para ver que hayan suficienes copias para la cantidad de personas 
    private void validarYAsignarParticipantes(Juego juego, int numParticipantes, Cafe miCafe) 
            throws NumeroJugadoresExcedidoException {
        int maxPorCopia = juego.getNumJugadores();
        
        if (numParticipantes < 2) {   
            throw new NumeroJugadoresExcedidoException(
                "Número de participantes inválido: " + numParticipantes + ". Mínimo 2 participantes."
            );
        }
        
        if (numParticipantes <= maxPorCopia) {    // Caso 1: Cabe en una sola copia
            this.numParticipantes = numParticipantes;
            return;
        }
       
        int copiasDisponibles = 0;
        for (Juego j : miCafe.getJuegosPrestamo()) { // Caso 2: Buscar copias adicionales
            if (j.getId() == juego.getId()) {
                copiasDisponibles++;
            }
        }
        
        int maxTotal = maxPorCopia * copiasDisponibles;
        
        if (numParticipantes <= maxTotal) {
            this.numParticipantes = numParticipantes;
        } else {
            throw new NumeroJugadoresExcedidoException(
                "Número de participantes inválido: " + numParticipantes + 
                ". Máximo por copia: " + maxPorCopia + 
                ", Copias disponibles: " + copiasDisponibles + 
                ", Máximo total posible: " + maxTotal
            );
        }
    }
    
   // para definir el premio
    public void definirPremio() {
        int totalRecaudado = this.numParticipantes * this.precio;
        String premio = "";
        
        if (this.tipo.equalsIgnoreCase("Amistoso")) {
            premio = "Bono de descuento 50%";
        } else {
            if (totalRecaudado < 50000) {
                premio = "Placa metálica simple";
            } else if (totalRecaudado < 100000) {
                premio = "Placa metálica de bronce";
            } else if (totalRecaudado < 200000) {
                premio = "Placa metálica de plata";
            } else {
                premio = "Placa metálica de oro";
            }
        }
        
        this.premio = premio;
    }
    
    //Definir ganador y premdio
    public void ganador(Usuario usuario) {
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            cliente.agregarPremio(premio);
        } 
        
        else if (usuario instanceof Empleado) {
        }
       
        String registroGanador = usuario.getNombre() + " - " + this.nombre;
        miCafe.nuevoGanadores(registroGanador);
    }
    
   
    
}