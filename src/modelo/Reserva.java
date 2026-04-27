package modelo;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import exceptions.JuegoNoAptoException;
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
		if (clientes != null) {
			this.clientes = clientes;
		}
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
	public void pedirCambioMesero(Calendar fecha, Juego juego) {
	    ArrayList<Empleado> empleados = miCafe.getEmpleados();
	    
	    for (Empleado empleado : empleados) {
	        if (empleado instanceof Mesero) {
	            Mesero posibleMesero = (Mesero) empleado;
	            
	            if (posibleMesero.libreParaReserva(fecha) && posibleMesero.conoceJuego(juego)) {
	                posibleMesero.nuevaReserva(this, fecha);  
	                this.meseroAsignado = posibleMesero;
	                return;
	            }
	        }
	    }
	    
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
	
	//Métodos
	
	//// PEDIR COSAS A LA MESA /////
	//RESERVAR JUEGOS
	public int edadMinima() {
		int menor = 1000;
		for (Cliente c : this.clientes) { 
	        if (c.getEdad() < menor) {
	        	menor = c.getEdad();
	        }
		}
		return menor;
		
	}
	
	public void agregarAlPrestamo(Juego juego) throws JuegoNoAptoException {
	    Cliente usuario = clientes.get(0);
	    
	    if(miCafe.reservarJuego( juego, usuario, this)) {//Mandamos la solicitud al sistema del café  y  un mesero de forma "manual " lo verificará
	    	juegosPrestados.add(juego);
	    }
	}
	
	//PEDIR COMIDA
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
	
	
    public void addTransaccion(Producto p) {
        this.transaccion.add(p); 
        double impuesto = p.getPrecio() * p.getTasaImpuesto();
        this.totalFactura += (p.getPrecio() + impuesto);
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
