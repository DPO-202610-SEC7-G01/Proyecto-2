package modelo.usuario;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import modelo.*;
import modelo.producto.*;

public class Empleado extends Usuario {
    private int puntosFidelidad;
    private ArrayList<Turno> turnos; 
    private ArrayList<Cliente> amigos;
    private ArrayList<Juego> juegosFavoritos;
    private Cafe miCafe;
    
    // Constructor
    public Empleado(int id, String login, String password, String nombre) {
        super(id, login, password, nombre);
        this.puntosFidelidad = 0;
        this.turnos = new ArrayList<>();
        this.amigos = new ArrayList<>();
        this.juegosFavoritos = new ArrayList<>();
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
    
    // Métodos    
    public void sugerencias(Producto producto) { 
        miCafe.agregarSugerencia(producto);
    }
    
    public boolean aptoPrestamo(Administrador admin, Juego juego, Calendar fechaConsulta) {
        boolean trabajaEnFecha = trabajaEnFecha(fechaConsulta);
        
        if (!trabajaEnFecha) {
            return true;  // Si no trabaja ese día, puede pedir préstamo
        }
        return admin.gestionarPrestamo(this, juego, fechaConsulta);
    }
    
    public Transaccion generarTransaccion(List<Producto> productosComprados, int idNuevaTransaccion) {
        Calendar hoy = Calendar.getInstance();
        Transaccion factura = new Transaccion(idNuevaTransaccion, hoy, productosComprados, this, false);    
        this.sumarPuntosFidelidad(productosComprados.size()); 
        return factura;
    }
    
    // FUNCIONES DE TURNO
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
}
