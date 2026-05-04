package modelo.usuario;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import exceptions.InvalidCredentialsException;
import exceptions.TorneoException;
import exceptions.UsuariosException;

import java.util.ArrayList;

import modelo.*;
import modelo.producto.*;

public class Empleado extends Usuario {
    private int puntosFidelidad;
    private ArrayList<Turno> turnos; 
    private ArrayList<Cliente> amigos;
    private ArrayList<Juego> juegosFavoritos;
    private Cafe miCafe;
    private ArrayList<Torneo> torneosInscritos;
    
    // Constructor
    public Empleado(int id, String login, String password, String nombre) throws InvalidCredentialsException, UsuariosException {
        super(id, login, password, nombre); 
        this.puntosFidelidad = 0; 
        this.turnos = new ArrayList<>(); 
        this.amigos = new ArrayList<>();
        this.juegosFavoritos = new ArrayList<>();
        this.torneosInscritos = new ArrayList<>();
    }
    
    // Getters y Setters
    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }
    public void sumarPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad += puntosFidelidad;
    }
    
    public ArrayList<Cliente> getAmigos() {
        return amigos;
    }
    public void agregarAmigo(Cliente cliente) {
        this.amigos.add(cliente);
    }
    
    public ArrayList<Juego> getJuegosFavoritos() {
        return juegosFavoritos;
    }
    
    public void agregarJuegoFavorito(Juego juegoFav) {
        juegosFavoritos.add(juegoFav);
    }
    
    public ArrayList<Turno> getTurnos() {
		return turnos;
	}
    public void agregarTurno(Turno e) {
    	turnos.add(e);
    }
    // Métodos
    public boolean verificarSiEsAmigo(Cliente supuesto){
        for(Cliente amigo: amigos){
            if(amigo.getId() == supuesto.getId()){
                return true;
            }
        }
        return false;
    }
    //PRESTAMO DE JUEGOS
    public boolean aptoPrestamo(Juego juego, Calendar fechaConsulta) {
        boolean trabajaEnFecha = trabajaEnFecha(fechaConsulta);
        
        HashMap<Calendar, HashMap<Usuario, Juego>> historial = miCafe.getHistorialUsoJuegos();
        if (historial.containsKey(fechaConsulta) && historial.get(fechaConsulta).containsValue(juego)) { //Acá vemos que si  está en el historial para ese día NO estará disponible
            return false;
        }
        
        if (trabajaEnFecha) {
            return false;
        }
        juego.setPrestado(true);
        historial.putIfAbsent(fechaConsulta, new HashMap<>());
        historial.get(fechaConsulta).put(this, juego);
        return true;
    }
    
    
    
	public void sugerencias(Producto producto) { 
        miCafe.agregarSugerencia(producto);
    }
    
    public Transaccion generarTransaccion(List<Producto> productosComprados, int idNuevaTransaccion) {
        Calendar hoy = Calendar.getInstance();
        Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, this, false);    
        return factura;
    }
    
    // FUNCIONES DE TURNO en EMPLEADO
    public Turno getTurnoPorFecha(Calendar fecha) {
        for (Turno turno : turnos) {
            if (turno.esMismaFecha(fecha)) {
                return turno;
            }
        }
        return null;
    }
    
    
    public void cambiarFechaTurno(Calendar fechaAntigua, Calendar fechaNueva) {
        for (Turno turno : turnos) {
            if (turno.esMismaFecha(fechaAntigua) && turno.isActivo()) {
                turno.setFecha(fechaNueva);
                break;
            }
        }
    }
    
    public boolean trabajaEnFecha(Calendar fechaConsulta) {
        for (Turno turno : this.turnos) {
            if (turno.esMismaFecha(fechaConsulta) && turno.isActivo()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean pedirCambioTurno(Administrador admin, Calendar miFecha, Calendar nuevaFecha, Empleado companero) {
        return admin.procesarCambioTurno(this, companero, miFecha, nuevaFecha);
    }

    public ArrayList<Calendar> getDiasTrabajo() {
        ArrayList<Calendar> diasTrabajo = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.isActivo()) {
                diasTrabajo.add(turno.getFecha());
            }
        }
        return diasTrabajo;
    }
    //TORNEO
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
	    
	    if (!trabajaEnFecha(torneo.getFecha())) {
	    	String fechaStr = torneo.getFecha().get(Calendar.DAY_OF_MONTH) + "/" + 
                    (torneo.getFecha().get(Calendar.MONTH) + 1) + "/" + 
                    torneo.getFecha().get(Calendar.YEAR);
	    	throw new TorneoException("El empleado " + getNombre() + " está en turno el día " + fechaStr, "EMPLEADO_EN_TURNO");
	    }
	    
	    torneo.agregarParticipantes(this);
	    torneosInscritos.add(torneo);
	}
    

    public ArrayList<Calendar> getListaFechas(){
        ArrayList<Calendar> fechas = new ArrayList<>();
        for(Turno turno: turnos){
            fechas.add(turno.getFecha());
        }
        return fechas;
    }
}
