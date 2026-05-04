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
    
    // Constructor
    public Torneo(String tipo, String nombre, Juego juego, int numParticipantes, int precio, Cafe miCafe)
            throws CafeException {
        
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new CafeException(this, "tipo", "El tipo del torneo no puede estar vacío.");
        }
        if (!tipo.equalsIgnoreCase("Amistoso") && !tipo.equalsIgnoreCase("Competitivo")) {
            throw new CafeException(this, "tipo", 
                "El tipo del torneo solo puede ser 'Amistoso' o 'Competitivo'.");
        }
        this.tipo = tipo;
        
   
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CafeException(this, "nombre", "El nombre del torneo no puede estar vacío.");
        }
        this.nombre = nombre;
        
        if (juego == null) {
            throw new CafeException(this, "juego", "El juego del torneo no puede ser nulo.");
        }
        this.juego = juego;
        
        if (tipo.equalsIgnoreCase("Amistoso") && precio != 0) {
            throw new CafeException(this, "precio", 
                "Los torneos amistosos deben ser gratuitos (precio = 0).");
        }
        if (precio < 0) {
            throw new CafeException(this, "precio", "El precio no puede ser negativo.");
        }
        this.precio = precio;
        
        if (miCafe == null) {
            throw new CafeException(this, "miCafe", "El café no puede ser nulo.");
        }
        this.miCafe = miCafe;
        
        this.activo = true;
        this.fanaticos = new ArrayList<Usuario>();
        this.participantes = new ArrayList<Usuario>();
        this.fecha = Calendar.getInstance();
        this.premio = "";
        
        validarYAsignarParticipantes(juego, numParticipantes, miCafe);
        agregarFanaticosDelJuego();
        definirPremio();
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public String getTipo() { 
        return tipo;
    }

    public Juego getJuego() {
        return juego; 
    }
    
    public int getNumParticipantes() { 
        return numParticipantes; 
    }
    
    public int getPrecio() { 
        return precio;
    }
    
    public boolean isActivo() { 
        return activo; 
    }
    
    public void setActivo(boolean activo) { 
        this.activo = activo; 
    }
    
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
    
    public ArrayList<Usuario> getFanaticos() {
        return fanaticos;
    }
    
    public String getPremio() {
        return premio;
    }
    
    public void agregarParticipantes(Usuario participante) throws CafeException {
        if (participantes.contains(participante)) {
            throw new CafeException(this, "participantes", 
                "El participante ya está inscrito en este torneo");
        }
        
        if (!hayCupoDisponible(participante)) { 
            boolean fanatico = esFanatico(participante);
            String tipoCupo = fanatico ? "fanáticos" : "normales";
            throw new CafeException(this, "cupos", 
                "No hay cupos " + tipoCupo + " disponibles");
        }
        
        participantes.add(participante);
    }
    
    public void eliminarParticipante(Usuario participante) { 
        participantes.remove(participante);
    }
    
 
    private void agregarFanaticosDelJuego() {     
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
    
    private void validarYAsignarParticipantes(Juego juego, int numParticipantes, Cafe miCafe) 
            throws CafeException {
        int maxPorCopia = juego.getNumJugadores();
        
        if (numParticipantes < 2) {   
            throw new CafeException(this, "numParticipantes", 
                "Número de participantes inválido: " + numParticipantes + ". Mínimo 2 participantes.");
        }
        
        if (numParticipantes <= maxPorCopia) {
            this.numParticipantes = numParticipantes;
            return;
        }
       
        int copiasDisponibles = 0;
        for (Juego j : miCafe.getJuegosPrestamo()) {
            if (j.getId() == juego.getId()) {
                copiasDisponibles++;
            }
        }
        
        int maxTotal = maxPorCopia * copiasDisponibles;
        
        if (numParticipantes <= maxTotal) {
            this.numParticipantes = numParticipantes;
        } else {
            throw new CafeException(this, "numParticipantes", 
                "Número de participantes inválido: " + numParticipantes + 
                ". Máximo por copia: " + maxPorCopia + 
                ", Copias disponibles: " + copiasDisponibles + 
                ", Máximo total posible: " + maxTotal);
        }
    }
    
    public void definirPremio() {
        int totalRecaudado = this.numParticipantes * this.precio;
        
        if (this.tipo.equalsIgnoreCase("Amistoso")) {
            this.premio = "Bono de descuento 50%";
        } else {
            if (totalRecaudado < 50000) {
                this.premio = "Placa metálica simple";
            } else if (totalRecaudado < 100000) {
                this.premio = "Placa metálica de bronce";
            } else if (totalRecaudado < 200000) {
                this.premio = "Placa metálica de plata";
            } else {
                this.premio = "Placa metálica de oro";
            }
        }
    }
    
    public void ganador(Usuario usuario) {
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            cliente.agregarPremio(this.premio);
        }
        
        String registroGanador = usuario.getNombre() + " - " + this.nombre;
        miCafe.nuevoGanadores(registroGanador);
    }
}