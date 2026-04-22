package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.producto.Juego;
import modelo.usuario.Empleado;
import modelo.usuario.Mesero;
import modelo.usuario.Cliente;
import modelo.usuario.Cocinero;
import modelo.usuario.Administrador;
import modelo.usuario.Usuario;
import modelo.producto.Platillo;
import modelo.producto.Bebida;
import modelo.producto.Producto;

public class Cafe {

	private int capacidad;
	private Administrador admin;
	private ArrayList<Mesa> mesas;
	private ArrayList<Cliente> clientes;
	private ArrayList<Empleado> empleados;
	private ArrayList<Reserva> reservasPrevias;
	public ArrayList<Juego> juegosPrestamo;
	public ArrayList<Juego> juegosVenta;
	private HashMap<Calendar, HashMap<Usuario,Juego>> historialUsoJuegos;
	private HashMap<Integer, ArrayList<Juego>> juegosCliente;
	private ArrayList<Transaccion> historialTransaccion;
	private ArrayList<Platillo> menuPlatillos;
	private ArrayList<Bebida> menuBebidas;
	private Map<Calendar, Empleado> turnoEmpleados;
	private List<Platillo> sugerenciasPendientes;


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
		this.sugerenciasPendientes = new ArrayList<Platillo>();
		this.turnoEmpleados = new HashMap<Calendar, Empleado>();
		this.historialTransaccion = new ArrayList<Transaccion>();
		this.juegosCliente = new HashMap<Integer, ArrayList<Juego>>();
		this.historialUsoJuegos = new HashMap<Calendar, HashMap<Usuario, Juego>>();
		
		
	}

	// Getters

	public int getCapacidad() {
		return capacidad;
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

	public ArrayList<Cliente> getClientes() {
		return clientes;
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

	public ArrayList<Transaccion> getHistorialTransaccion() {
		return historialTransaccion;
	}

	public Map<Calendar, Empleado> getTurnoEmpleados() {
		return turnoEmpleados;
	}

	public ArrayList<Juego> getJuegosPrestamo() {
		return juegosPrestamo;
	}

	public ArrayList<Juego> getJuegosVenta() {
		return juegosVenta;
	}

	public ArrayList<Platillo> getMenuPlatillos(){
		return menuPlatillos;
	}
	
	public ArrayList<Bebida> getMenuBebidas(){
		return menuBebidas;
	}
	
	public List<Platillo> getSugerenciasPendientes(){
		return sugerenciasPendientes;
	}
	
	
	// Métodos
	public void cambiarAdmin(Administrador adminNuevo) {
		admin= adminNuevo;
	}
	
	public void agregarSugerencias(Platillo p) {
		sugerenciasPendientes.add(p);
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

	public boolean verificarDisponibilidad(Calendar fecha, int numPersonas) {
		if ((numPersonas <= capacidad || numPersonas > 0)) {
			return true;
		}
		return false;
	}

	public boolean aptoApertura(Calendar fechaConsulta) {
		int cocineros = 0;
		int meseros = 0;

		for (Map.Entry<Calendar, Empleado> entrada : turnoEmpleados.entrySet()) {
			Calendar fechaTurno = entrada.getKey();
			Empleado e = entrada.getValue();
			if (esMismaFecha(fechaTurno, fechaConsulta)) {
				if (e instanceof Mesero) {
					meseros++;
				} else if (e instanceof Cocinero) {
					cocineros++;
				}
			}
		}
		return (cocineros >= 1 && meseros >= 2);
	}

	private boolean esMismaFecha(Calendar cal1, Calendar cal2) {
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
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

	public void agregarEmpleado(Empleado e) {
		this.empleados.add(e);
		// Calendar turno = e.getTurno();
		// this.turnoEmpleados.put(turno, e);
	}

	public void agregarMesa(Mesa mesa) {
		this.mesas.add(mesa);
	}

	public void agregarUsuario(Cliente cliente) {
		this.clientes.add(cliente);
	}

	public void agregarJuegoPrestamo(Juego juego) {
		this.juegosPrestamo.add(juego);
	}

	public void agregarJuegoVenta(Juego juego) {
		this.juegosVenta.add(juego);
	}
	public void sugerirPlatillo(Platillo platillo) {
		this.sugerenciasPendientes.add(platillo);
	}
	
	public boolean reservarJuego(Juego juego, Reserva r) {
	    if (!juegosPrestamo.contains(juego)) {
	        return false;
	    }

	    Calendar fecha = r.getFecha();
	    int numPersonas = r.getNumPersonas();
	    List<Cliente> integrantes = r.getClientes();
	    
	    if (integrantes == null || integrantes.isEmpty()) return false;
	    
	    // Identificamos al cliente que lidera la reserva (el primero de la lista)
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
	
	// Dentro de la clase Cafe
	public void registrarProductoEnTransaccion(Transaccion t, Producto p) {
	    t.agregarProducto(p);
	    // Si la transacción no estaba en el historial, la agregamos (evita duplicados)
	    if (!historialTransaccion.contains(t)) {
	        historialTransaccion.add(t);
	    }
	}

	public Cocinero turnoCocineros(Calendar fecha) {
	    Empleado empleadoAsignado = turnoEmpleados.get(fecha);
	    if (empleadoAsignado != null && empleadoAsignado instanceof Cocinero) {
	        return (Cocinero) empleadoAsignado;
	    }

	    return null;
	}
}
