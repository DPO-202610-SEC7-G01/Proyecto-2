package modelo;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import modelo.producto.*;
import modelo.usuario.*;

public class Reserva {
	private Mesa mesa;
	private Cafe miCafe;
	private Mesero meseroAsignado;

	
	private int numPersonas;
	private Calendar fecha;
	private List<Cliente> clientes;
	private List<Producto> transaccion;
	private List<Juego> juegosPrestados; 
	private double totalFactura;

	//Constructor
	public Reserva(List<Cliente> clientes, int numPersonas, Calendar fecha) {
		super();
		this.clientes = clientes;
		this.numPersonas = numPersonas;
		this.fecha = fecha;
		this.transaccion = new ArrayList<Producto>();
		this.juegosPrestados = new ArrayList<>();
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
	public void setMesa(Mesa nuevaMesa) {
		this.mesa= nuevaMesa;
	}
	
	public List<Producto> getFactura() {
		return transaccion;
	}
	public void setFactura(ArrayList<Producto> nuevaTransaccion) {
		this.transaccion=  nuevaTransaccion;
	}
	
	public double getTotalFactura() {
		return totalFactura;
	}
	public void setTotalFactura(int factura) {
		this.totalFactura= factura;
	}

	public List<Juego> getJuegosPrestados(){
		return juegosPrestados;
	}
		
	public Mesero getMeseroAsignado() {
	    return meseroAsignado;
	}
	
	//Métodos
	public void cambiarMesero(Calendar fecha) {
	    ArrayList<Empleado> empleados = miCafe.getEmpleados();
	    
	    for (Empleado empleado : empleados) {
	        if (empleado instanceof Mesero) {
	            Mesero posibleMesero = (Mesero) empleado;
	            
	            if (posibleMesero.libreParaReserva(fecha)) {
	                posibleMesero.nuevaReserva(this, fecha);  
	                this.meseroAsignado = posibleMesero;
	                break;  
	            }
	        }
	    }
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
	        juegosPrestados.add(juego);
	        Usuario usuario = (clientes != null && !clientes.isEmpty()) ? clientes.get(0) : null;
	        if (usuario != null) {
	            miCafe.registrarJuegoEnHistorial(fecha, usuario, juego);
	        }
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
		for (Juego j : juegosPrestados) {
            j.setPrestado(false);
        }
        this.juegosPrestados.clear();
        
        if (this.mesa != null) {
            this.mesa.setDisponible(true); 
        }
    }
}
