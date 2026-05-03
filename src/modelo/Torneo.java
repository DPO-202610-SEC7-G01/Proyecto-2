package modelo;

import java.util.ArrayList;
import java.util.Calendar;

import exceptions.*;
import modelo.producto.Juego;
import modelo.usuario.*;

public class Torneo {
    private String tipo;
    private Juego juego;
    private int numParticipantes;
    private int precio;
    private boolean activo;
	private Cafe miCafe;
	private ArrayList<Usuario> participantes;
	private ArrayList<Usuario> fanaticos;
	private Calendar fecha;
	private String nombre;
	private String premio;
	
    public Torneo(String tipo, String nombre, Juego juego, int numParticipantes, int precio) throws NumeroJugadoresExcedidoException {
        this.tipo = tipo;
        this.nombre = nombre;
        this.juego = juego;
        this.precio = precio;
        this.activo = true;
        validarYAsignarParticipantes(juego, numParticipantes, miCafe);
        this.participantes = new ArrayList<Usuario>();
        this.fanaticos = new ArrayList<Usuario>();
        this.fecha = Calendar.getInstance();
        this.premio = "";
    
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
        
        if (!hayCupoDisponible(participante)) { //Miramos si no hay cupo para este usuario
            boolean fanatico = esFanatico(participante);
            String tipoCupo = fanatico ? "fanáticos" : "normales";
            throw new TorneoException("No hay cupos " + tipoCupo + " disponibles", "SIN_CUPOS");
        }
        
        participantes.add(participante);
    }
    
	public void eliminarParticipante(Usuario participante) { // hay que buscarlo en la lista de usuarios
		participantes.remove(participante);
	}
	
	
	//Métodos   
	//Agregar los fanáticos del juego 	
	private boolean esFanatico(Usuario usuario) {
		 if (usuario instanceof Cliente) {
			 if(((Cliente) usuario).esFanatico(juego)) {
				 fanaticos.add(usuario);
			 }
			 return((Cliente) usuario).esFanatico(juego) ;
		 } if(((Mesero) usuario).esFanatico(juego)) {
			 fanaticos.add(usuario);
		 }
		 return ((Empleado) usuario).esFanatico(juego);	    
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
        
        if (numParticipantes < 2) {   // Validar número mínimo
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
            //TODO manejo para empleados
        }
       
        String registroGanador = usuario.getNombre() + " - " + this.nombre;
        miCafe.nuevoGanadores(registroGanador);
    }
    
   
    
}