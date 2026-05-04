package modelo.usuario;

//utils
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;


//exceptions
import exceptions.*;

//modelo
import modelo.*;
import modelo.producto.*;

public class Empleado extends Usuario {
	private Cafe miCafe;
    private int puntosFidelidad;
    private ArrayList<Turno> turnos; 
    private ArrayList<Cliente> amigos;
    private ArrayList<Juego> juegosFavoritos;
    private ArrayList<Torneo> torneosInscritos;
    
    // Constructor
    public Empleado(int id, String login, String password, String nombre) throws UsuariosException {
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
    public void sumarPuntosFidelidad(int puntosFidelidad) throws UsuariosException {
	    if (puntosFidelidad <= 0) {
	        throw new UsuariosException(this, "puntosFidelidad", 
	            "Los puntos a sumar deben ser positivos. Valor recibido: " + puntosFidelidad);
	    }
	    this.puntosFidelidad += puntosFidelidad;
	}
    
    public ArrayList<Cliente> getAmigos() {
        return amigos;
    }
    public void agregarAmigo(Cliente cliente) {
    	cliente.nuevoAmigo(); // Esto debería tener una prueba de integración (IGNORAR POR EL MOMENTO)
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
    public void sugerencias(Producto producto) { 
        miCafe.agregarSugerencia(producto);
    }
    
    public boolean verificarSiEsAmigo(Cliente supuesto){
        for(Cliente amigo: amigos){
            if(amigo.getId() == supuesto.getId()){
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Torneo>  getTorneosInscritos() {
		return torneosInscritos;
	}

	//PRESTAMO DE JUEGOS
    public boolean aptoPrestamo(Juego juego, Calendar fechaConsulta) {
        boolean trabajaEnFecha = trabajaEnFecha(fechaConsulta);
        
        HashMap<Calendar, HashMap<Usuario, Juego>> historial = miCafe.getHistorialUsoJuegos();
        if (historial.containsKey(fechaConsulta) && historial.get(fechaConsulta).containsValue(juego)) {
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
    
    // FUNCIONES DE TURNO 
    public boolean pedirCambioTurno(Administrador admin, 
    		Calendar miFecha, Calendar nuevaFecha, Empleado companero) {
        return admin.procesarCambioTurno(this, companero, miFecha, nuevaFecha); //Esta también es una prueba de integración porque cambia parámetros de esta clase a través de admin (IGNORAR POR EL MOMENTO)
    }

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
    
    
    public ArrayList<Calendar> getDiasTrabajo() {
        ArrayList<Calendar> diasTrabajo = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.isActivo()) {
                diasTrabajo.add(turno.getFecha());
            }
        }
        return diasTrabajo;
    }
    
    public ArrayList<Calendar> getListaFechas(){
        ArrayList<Calendar> fechas = new ArrayList<>();
        for(Turno turno: turnos){
            fechas.add(turno.getFecha());
        }
        return fechas;
    }
    
    //TORNEO
    public void inscribirseTorneo(String nombreTorneo, Cafe miCafe) throws UsuariosException, CafeException {
	    Torneo torneo = null;
	    
	    if (miCafe.getTorneosActivos() != null) {
	        for (Torneo t : miCafe.getTorneosActivos()) {
	            if (t.getJuego().getNombre().equalsIgnoreCase(nombreTorneo) && t.isActivo()) {
	                torneo = t;
	                break;
	            }
	        }
	    }
	    
	    if (torneo == null) {
	        throw new UsuariosException(this, "torneo", 
	            "Torneo no encontrado para el juego: " + nombreTorneo);
	    }
	    
	    if (torneosInscritos.size() >= 3) {
	        throw new UsuariosException(this, "torneosInscritos", 
	            "El usuario excede el límite máximo de 3 torneos");
	    }
	    
	    if (torneosInscritos.contains(torneo)) {
	        throw new UsuariosException(this, "torneosInscritos", 
	            "El usuario ya está inscrito en el torneo: " + nombreTorneo);
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
    
    
   
}
