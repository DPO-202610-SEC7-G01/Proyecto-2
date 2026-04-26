package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.FileNotFoundException;
import modelo.usuario.*;
import modelo.producto.*;
import persistencia.*;

public class Cafe {

	private int capacidad;
	private Administrador admin; //Persistencia Implementada
	private ArrayList<Mesa> mesas; // 
	private ArrayList<Cliente> clientes; //
	private ArrayList<Empleado> empleados; //
	private ArrayList<Reserva> reservasPrevias; // 
	public ArrayList<Juego> juegosPrestamo; //
	public ArrayList<Juego> juegosVenta; // 
	private HashMap<Calendar, HashMap<Usuario,Juego>> historialUsoJuegos;// 
	private HashMap<Integer, ArrayList<Juego>> juegosCliente;  //Estos juegos está en la reserva  
	private ArrayList<Transaccion> historialTransaccion; //
	public ArrayList<Platillo> menuPlatillos; //
	public ArrayList<Bebida> menuBebidas; //
	private Map<Empleado, Turno> turnoEmpleados; 
	private ArrayList<Producto> sugerenciasPendientes; //


	// Constructor
	public Cafe(int capacidad) {
		super();
		this.admin = null;
		this.capacidad = capacidad;
		this.mesas = new ArrayList<Mesa>();
		this.clientes = new ArrayList<Cliente>();
		this.juegosVenta = new ArrayList<Juego>();
		this.menuBebidas = new ArrayList<Bebida>();
		this.empleados = new ArrayList<Empleado>();
		this.juegosPrestamo = new ArrayList<Juego>();
		this.menuPlatillos = new ArrayList<Platillo>();
		this.reservasPrevias = new ArrayList<Reserva>();
		this.sugerenciasPendientes = new ArrayList<Producto>();
		this.turnoEmpleados = new HashMap<Empleado, Turno>();
		this.historialTransaccion = new ArrayList<Transaccion>();
		this.juegosCliente = new HashMap<Integer, ArrayList<Juego>>();
		this.historialUsoJuegos = new HashMap<Calendar, HashMap<Usuario, Juego>>();
		
		
	}

	// Getters y Setters
	
	public int getCapacidad() {
		return capacidad;
	}
	
	public void SetCapacidad(int Capacidad) {
		this.capacidad = Capacidad;
	}
	
	public Administrador getAdmin() {
		return admin;
	}

	public void actualizarCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public ArrayList<Mesa> getMesas() {
		return mesas;
	}
	public void agregarMesa(Mesa mesa) {
		this.mesas.add(mesa);
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void agregarUsuario(Cliente cliente) {
		this.clientes.add(cliente);
	}

	
	public ArrayList<Empleado> getEmpleados() {
		return empleados;
	}

	public ArrayList<Reserva> getReservasPrevias() {
		return reservasPrevias;
	}

	public HashMap<Calendar, HashMap<Usuario, Juego>> getHistorialUsoJuegos() {
		return historialUsoJuegos; 
	} 
	
	public void registrarJuegoEnHistorial(Calendar fecha, Usuario usuario, Juego juego) {
	    if (!historialUsoJuegos.containsKey(fecha)) {
	        historialUsoJuegos.put(fecha, new HashMap<Usuario, Juego>());
	    }
	    
	    HashMap<Usuario, Juego> mapUsuarioJuego = historialUsoJuegos.get(fecha);
	    mapUsuarioJuego.put(usuario, juego);
	}
	
	
	public ArrayList<Transaccion> getHistorialTransaccion() {
		return historialTransaccion;
	}
	
	public void agregarTransaccion(Transaccion transaccion){
		historialTransaccion.add(transaccion);
	}

	public Map<Empleado, Turno> getTurnoEmpleados() {
		return turnoEmpleados;
	}

	public ArrayList<Juego> getJuegosPrestamo() {
		return juegosPrestamo;
	}
	
	public void agregarJuegoPrestamo(Juego juego) {
		this.juegosPrestamo.add(juego);
	}

	public ArrayList<Juego> getJuegosVenta() {
		return juegosVenta;
	}
	
	public void agregarJuegoVenta(Juego juego) {
		this.juegosVenta.add(juego);
	}
	
	public ArrayList<Platillo> getMenuPlatillos(){
		return menuPlatillos;
	}
	
	public ArrayList<Bebida> getMenuBebidas(){
		return menuBebidas;
	}
	
	public ArrayList<Producto> getSugerenciasPendientes(){
		return sugerenciasPendientes;
	}
	
	public void agregarSugerencia(Producto producto) {
	    sugerenciasPendientes.add(producto);
	}
	

	public void cambiarAdmin(Administrador adminNuevo) {
		admin= adminNuevo;
	}
	
	
	public void agregarEmpleado(Empleado e) {
		this.empleados.add(e);
		// Calendar turno = e.getTurno();
		// this.turnoEmpleados.put(turno, e);
	}

	
	//Persistencia
	//Carga de Datos Iniciales
	public void descargarDatos(String juegosPrestamoArchivo, String juegosVentaArchivo, String juegosDificilesArchivo,
				String bebidasArchivo, String platillosArchivo, String administradorArchivo,
				String cocinerosArchivo, String meserosArchivo, String clientesArchivo,
				String reservasArchivo, String  historialPrestamosArchivo, String sugerenciasPendientesArchivo,
				String transaccionesArchivo,String mesasArchivo, String turnosArchivo) throws IOException, FileNotFoundException { 
		
		PersistenciaProductos.descargarProductos(juegosPrestamoArchivo,juegosVentaArchivo, juegosDificilesArchivo,
						bebidasArchivo,platillosArchivo, this);
		PersistenciaUsuarios.descargarUsuarios(administradorArchivo, cocinerosArchivo, meserosArchivo, clientesArchivo,  this);
		PersistenciaCafe.descargarCafe(reservasArchivo,historialPrestamosArchivo,sugerenciasPendientesArchivo,
				transaccionesArchivo,mesasArchivo, turnosArchivo, this);
			
		}
		
	
	// Métodos	
	public void sugerirPlatillo(Platillo platillo) {
		this.sugerenciasPendientes.add(platillo);
	}
	
	public boolean verificarDisponibilidad(Calendar fecha, int numPersonas) {
		if ((numPersonas <= capacidad || numPersonas > 0)) {
			return true;
		}
		return false;
	}
	
	public void registrarProductoEnTransaccion(Transaccion t, Producto p) {
	    t.agregarProducto(p);
	    if (!historialTransaccion.contains(t)) {
	        historialTransaccion.add(t);
	    }
	}
	
	public Cocinero turnoCocineros(Calendar fecha) {
	    for (Map.Entry<Empleado, Turno> entry : turnoEmpleados.entrySet()) {
	        Empleado empleado = entry.getKey();
	        Turno turno = entry.getValue();
	        if (empleado instanceof Cocinero && turno != null && turno.isActivo() && turno.esMismaFecha(fecha)) {
	            return (Cocinero) empleado;
	        }
	    }
	    return null;
	}
	
	public void registrarNuevaReserva(Reserva r) {
	    if (verificarDisponibilidad(r.getFecha(), r.getNumPersonas()) && asignarMesa(r)) {
	    	reservasPrevias.add(r);
	        int puntosPorReserva = 10; 
	        for (Cliente c : r.getClientes()) {
	            c.sumarPuntosFidelidad(puntosPorReserva);
	        }
	    } 
	}

	public boolean aptoApertura(Calendar fechaConsulta) {
	    int cocineros = 0;
	    int meseros = 0;

	    for (Map.Entry<Empleado, Turno> entrada : turnoEmpleados.entrySet()) {
	        Empleado e = entrada.getKey();
	        Turno turno = entrada.getValue();
	        if (turno != null && turno.esMismaFecha(fechaConsulta) && turno.isActivo()) {
	            if (e instanceof Mesero) {
	                meseros++;
	            } else if (e instanceof Cocinero) {
	                cocineros++;
	            }
	        }
	    }
	    return (cocineros >= 1 && meseros >= 2);
	}


	public boolean asignarMesa(Reserva r) {
		for (Mesa mesita : mesas) {
			if (mesita.isDisponible() && r.getNumPersonas() <= mesita.getNumSillas()) {
				mesita.setDisponible(false);
				r.setMesa(mesita);
				return true;
			}
		}
		return false;
	}

	
	public boolean reservarJuego(Juego juego, Reserva r) {
	    if (!juegosPrestamo.contains(juego)) {
	        return false;
	    }

	    Calendar fecha = r.getFecha();
	    int numPersonas = r.getNumPersonas();
	    List<Cliente> integrantes = r.getClientes();
	    
	    if (integrantes == null || integrantes.isEmpty()) return false;
	    
	    Cliente clientePrincipal = integrantes.get(0);
	    int idPrincipal = clientePrincipal.getId();

	    int edadMinimaJuego = extraerEdadMinima(juego.getRestriccionEdad());
	    for (Cliente c : integrantes) {
	        if (c.getEdad() < edadMinimaJuego) {
	            return false;
	        }
	    }

	    if (juego.getNumJugadores() < numPersonas) {
	        return false;
	    }

	    historialUsoJuegos.putIfAbsent(fecha, new HashMap<Usuario, Juego>());
	    juegosCliente.putIfAbsent(idPrincipal, new ArrayList<Juego>());

	    HashMap<Usuario, Juego> registrosFecha = historialUsoJuegos.get(fecha);
	    ArrayList<Juego> listaJuegosDelCliente = juegosCliente.get(idPrincipal);

	   
	    if (registrosFecha.containsValue(juego)) {
	        return false;
	    }

	    if (listaJuegosDelCliente.size() >= 2) {
	        return false;
	    }

	    
	    listaJuegosDelCliente.add(juego);
	    registrosFecha.put(clientePrincipal, juego);
	    juego.setPrestado(true);
	    
	    return true;
	}

	
	private int extraerEdadMinima(String restriccionEdad) {
	    String texto = restriccionEdad.toLowerCase();

	    if (texto.contains("adultos")) {
	        return 18;
	    }

	    String numeros = texto.replaceAll("[^0-9]", "");

	    if (!numeros.isEmpty()) {
	        return Integer.parseInt(numeros);
	    }

	    return 0;
	}
	
	public double calcularIngresosTotales(Calendar fechaInicio, Calendar fechaFin) {
		double total = 0;
		for (Transaccion t : historialTransaccion) {
			Calendar fecha = t.getFecha();
			if ((fecha.equals(fechaInicio) || fecha.after(fechaInicio))
					&& (fecha.equals(fechaFin) || fecha.before(fechaFin))) {
				total += t.calcularTotal();
			}
		}
		return total;
	}
	
	


}
