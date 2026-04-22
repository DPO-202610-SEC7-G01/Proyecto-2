package modelo;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import modelo.producto.Juego;
import modelo.producto.Producto;
import modelo.producto.Bebida;
import modelo.producto.Platillo;
import modelo.usuario.Cliente;
import modelo.usuario.Mesero;

public class Reserva {
	private Mesa mesa;
	private int numPersonas;
	private Calendar fecha;
	private List<Cliente> clientes;
	private List<Producto> transaccion;
	private List<Juego> juegosPrestados; 
	private Cafe miCafe;
	private Mesero meseroAsignado;
	private double totalFactura;

	
	
	//Constructor
	public Reserva(List<Cliente> clientes, int numPersonas, Calendar fecha) {
		super();
		this.clientes = clientes;
		this.numPersonas = numPersonas;
		this.fecha = fecha;
		this.transaccion = new ArrayList<Producto>();
		this.totalFactura =0.0;
		
	}
	
	//Getters y Setters
	public int getNumPersonas() {
		return numPersonas;
	}
	
	public Calendar getFecha() {
		return fecha;
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}
	public Mesa getMesa() {
		return mesa;
	}
	public List<Producto> getFactura() {
		return transaccion;
	}
	
	public double getTotalFactura() {
		return totalFactura;
	}

	public List<Juego> getJuegosPrestados(){
		return juegosPrestados;
	}
	
	public void setMesa(Mesa nuevaMesa) {
		this.mesa= nuevaMesa;
	}
	

	public Mesero getMeseroAsignado() {
	    return meseroAsignado;
	}
	//Métodos
	public void cambiarMesero(Mesero nuevoMesero) {
	    this.meseroAsignado = nuevoMesero;
	}
	
    public void addTransaccion(Producto p) {
        this.transaccion.add(p); 
        double impuesto = p.getPrecio() * p.getTasaImpuesto();
        this.totalFactura += (p.getPrecio() + impuesto);
    }
	

	
	public boolean tieneMenoresDeEdad() {
	    for (Cliente c : this.clientes) { 
	        if (c.getEdad() < 18) {
	            return true; 
	        }
	    }
	    return false; 
	}
	
	public boolean tieneBebidasCalientes() {
	    for (Producto p : this.transaccion) {
	        if (p instanceof Bebida) {
	            Bebida b = (Bebida) p; 
	            if (b.getTemperatura().equalsIgnoreCase("Caliente")) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	

	public void agregarAlPrestamo(Juego juego) {
        if (miCafe.reservarJuego(juego, this)) {
            this.juegosPrestados.add(juego);
        }
        
	} 


	public void pedirPlatillo(Platillo platillo) {
	    if (this.meseroAsignado != null) {
	        this.meseroAsignado.servirPlatillos(this, platillo);
	    } 
	}

	public void pedirBebida(Bebida bebida) {
	    if (this.meseroAsignado != null) {
	        this.meseroAsignado.servirBebidas(this, bebida);
	    } 
	}
	
	public void finalizarReserva() {
        System.out.println("Finalizando reserva... Liberando recursos.");
        
        // 1. Los juegos pasan de estar prestados a disponibles
        for (Juego j : juegosPrestados) {
            j.setPrestado(false);
        }
        
        // 2. Limpiamos la lista de juegos prestados de la reserva
        this.juegosPrestados.clear();
        
        // 3. Liberamos la mesa si estaba asignada
        if (this.mesa != null) {
            this.mesa.setDisponible(true); // Suponiendo que Mesa tiene este atributo
        }
        }
   
	
	
	
}
