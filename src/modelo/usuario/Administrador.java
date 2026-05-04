package modelo.usuario;

//Utils
import java.util.ArrayList;
import java.util.Calendar;

//Exceptions
import exceptions.*;

//Modelo
import modelo.*;
import modelo.producto.*;


public class Administrador extends Usuario {
	private Cafe miCafe;
	
	//Constructor
	public Administrador(int id, String login, String password, String nombre, Cafe cafe) throws UsuariosException {
		super(id, login, password, nombre);
		this.miCafe = cafe;
	}
	
	//Métodos
	//JUEGOS
	public void moverJuego(Juego juego) throws JuegoNoEncontradoException {
	    ArrayList<Juego> listaVenta = miCafe.getJuegosVenta();
	    ArrayList<Juego> listaPrestamo = miCafe.getJuegosPrestamo();
	    
	    if (listaVenta.contains(juego)) {
	        listaVenta.remove(juego);
	        listaPrestamo.add(juego);
	        juego.setPrestado(false);
	    } else {
	        throw new JuegoNoEncontradoException(juego.getNombre(), "ventas");
	    }
	} 
	
	public void comprarJuego(int id, int precio, String nombre, int anioPublicacion, String empresMatriz, 
            int numJugadores, String restriccionEdad, String categoria, boolean esParaVenta) 
            throws UsuariosException, ProductosException {

		if (anioPublicacion <= 0) {
		throw new IllegalArgumentException("El año de publicación debe ser mayor a 0");
		}
		
		Juego nuevoJuego = new Juego(id, precio, nombre, anioPublicacion, empresMatriz, 
		                     numJugadores, restriccionEdad, categoria);
		
		if (esParaVenta) {
		miCafe.getJuegosVenta().add(nuevoJuego);
		} else {
		miCafe.getJuegosPrestamo().add(nuevoJuego);
		}
		}
	
	public void comprarJuegoDificil(int id, int precio, String nombre, int anioPublicacion, String empresMatriz,
            int numJugadores, String restriccionEdad, String categoria, 
            String instrucciones, boolean esParaVenta) 
            throws UsuariosException, ProductosException {

			if (anioPublicacion <= 0) {
			throw new IllegalArgumentException("El año de publicación debe ser mayor a 0");
			}
			
			JuegoDificil nuevoJuego = new JuegoDificil(id, precio, nombre, anioPublicacion, empresMatriz,
			                           numJugadores, restriccionEdad, categoria, instrucciones);
			
			if (esParaVenta) {
			miCafe.getJuegosVenta().add(nuevoJuego);
			} else {
			miCafe.getJuegosPrestamo().add(nuevoJuego);
			}
		}
	
	public void repararJuego(Juego juegoDanado) throws JuegoNoEncontradoException {
	    ArrayList<Juego> listaVenta = miCafe.getJuegosVenta();
	    ArrayList<Juego> listaPrestamo = miCafe.getJuegosPrestamo();
	    
	    if (!listaPrestamo.contains(juegoDanado)) {
	        throw new JuegoNoEncontradoException(juegoDanado.getNombre(), "préstamo");
	    }
	    
	    if (listaVenta.isEmpty()) {
	        throw new JuegoNoEncontradoException("No hay juegos disponibles en venta para reemplazar el juego " + juegoDanado.getNombre());
	    }
	    
	    Juego juegoReemplazo = buscarJuegoCompatible(juegoDanado, listaVenta);
	    
	    if (juegoReemplazo != null) {
	        int index = listaPrestamo.indexOf(juegoDanado);
	        listaPrestamo.set(index, juegoReemplazo);
	        listaVenta.remove(juegoReemplazo);
	    } else {
	        throw new JuegoNoEncontradoException("No se encontró un juego compatible para reemplazar " + juegoDanado.getNombre());
	    }
	}
	
	private Juego buscarJuegoCompatible(Juego juegoDanado, ArrayList<Juego> listaVenta) {
	    for (Juego juego : listaVenta) {
	        if (juego.getNombre().equals(juegoDanado.getNombre()) || 
	            juego.getCategoria().equals(juegoDanado.getCategoria())) {
	            return juego;
	        }
	    }
	    return listaVenta.isEmpty() ? null : listaVenta.get(0);
	}
	
	public void marcarJuegoRobado(Juego juegoRobado){
	    ArrayList<Juego> listaPrestamo = miCafe.getJuegosPrestamo();
	    
	    if (listaPrestamo.contains(juegoRobado)) {
	        juegoRobado.setPrestado(false);
		    juegoRobado.setEstado("Desaparecido");;
	    }
	}
	
	//COMIDA
	public void crearPlatillo(int id, int precio, String nombre, ArrayList<String> alergenos)
			throws ProductosException {
	    Platillo nuevoPlatillo = new Platillo(id, precio, nombre, alergenos);
	    miCafe.getMenuPlatillos().add(nuevoPlatillo);
	}

	public void crearBebida(int id, int precio, String nombre, String temperatura, boolean alcohol)
			throws ProductosException {
	    Bebida nuevaBebida = new Bebida(id, precio, nombre, temperatura, alcohol);
	    miCafe.getMenuBebidas().add(nuevaBebida);
	}
	
	public void incluirSugerencia(Platillo platillo) {
		miCafe.getMenuPlatillos().add(platillo);
		miCafe.getSugerenciasPendientes().remove(platillo);
	}
	public void excluirSugerencia(Platillo platillo) {
		miCafe.getSugerenciasPendientes().remove(platillo);
	}
	
	public void incluirSugerencia(Bebida bebida) {
		miCafe.getMenuBebidas().add(bebida);
		miCafe.getSugerenciasPendientes().remove(bebida);
	}
	public void excluirSugerencia(Bebida bebida) {
		miCafe.getSugerenciasPendientes().remove(bebida);
	}
	
	//TURNOS
	public void asignarTurno(Empleado empleado, Calendar fecha, boolean activo) {
	    Turno nuevoTurno = new Turno(fecha, activo);
	    empleado.agregarTurno(nuevoTurno);
	    miCafe.turnoEmpleados.put(empleado, nuevoTurno);
	}
	
	public boolean procesarCambioTurno(Empleado solicitante, Empleado companero, Calendar fechaS, Calendar fechaC) {
	    Turno turnoSol = solicitante.getTurnoPorFecha(fechaS);
	    Turno turnoComp = companero.getTurnoPorFecha(fechaC);
	    if (turnoSol == null || !turnoSol.isActivo() || turnoComp == null || !turnoComp.isActivo()) {
	        return false;
	    }
	    if (!solicitante.getClass().equals(companero.getClass())) {
	        return false;
	    }
	    
	    Calendar fechaSolOriginal = turnoSol.getFecha();
	    Calendar fechaCompOriginal = turnoComp.getFecha();
	    turnoSol.setFecha(fechaC);
	    turnoComp.setFecha(fechaS);
	    
	    boolean esAptoS = this.miCafe.aptoApertura(fechaS);
	    boolean esAptoC = this.miCafe.aptoApertura(fechaC);
	    
	    if (esAptoS && esAptoC) {
	        return true;
	    } else {
	        turnoSol.setFecha(fechaSolOriginal);
	        turnoComp.setFecha(fechaCompOriginal);
	        return false;
	    }
	}
	
	
	//TORNEOS
	public void crearTorneo(String tipo,  String nombre, Juego juego, int numParticipantes, int precio) 
			throws IllegalArgumentException,UsuariosException, CafeException {
	    validarTorneo(tipo, juego, numParticipantes);
	    
	    Torneo nuevoTorneo = new Torneo(tipo, nombre, juego, numParticipantes, precio, miCafe);
	    miCafe.getTorneosActivos().add(nuevoTorneo);
	}

	private void validarTorneo(String tipo, Juego juego, int numParticipantes) throws IllegalArgumentException {
	    if (!tipo.equalsIgnoreCase("Amistoso") && !tipo.equalsIgnoreCase("Competitivo")) {
	        throw new IllegalArgumentException("El tipo debe ser 'Amistoso' o 'Competitivo'");
	    }
	    
	    if (juego == null) {
	        throw new IllegalArgumentException("Debe seleccionar un juego válido");
	    }
	    
	    if (numParticipantes <= 0) {
	        throw new IllegalArgumentException("El número de participantes debe ser mayor a 0");
	    }
	    
	    if (tipo.equalsIgnoreCase("Competitivo")) {
	        validarParticipantesCompetitivo(numParticipantes, juego);
	    }
	}

	private void validarParticipantesCompetitivo(int numParticipantes, Juego juego) throws IllegalArgumentException {
	    int maxJugadores = juego.getNumJugadores();
	    
	    if (numParticipantes % 2 != 0) {
	        throw new IllegalArgumentException("El número de participantes en torneo competitivo debe ser par");
	    }
	    
	    if (numParticipantes < 2) {
	        throw new IllegalArgumentException("El torneo competitivo requiere al menos 2 participantes");
	    }
	    
	    if (numParticipantes > maxJugadores * 4) {
	        throw new IllegalArgumentException("Demasiados participantes. Máximo sugerido: " + (maxJugadores * 4));
	    }
	}
	
	
	
	//FINANZAS
	public String verFinanzas(Calendar fechaInicial, Calendar fechaFinal) throws UsuariosException {
	    StringBuilder reporte = new StringBuilder();
	    ArrayList<Transaccion> historial = miCafe.getHistorialTransaccion();

	    reporte.append("--------------------------------------------------------------------------------\n");
	    reporte.append(String.format("%-15s | %-12s | %-10s | %-10s | %-10s\n", 
	                  "PRODUCTOS", "GRANULARIDAD", "P. BASE", "IMPUESTOS", "TOTAL FINAL"));
	    reporte.append("--------------------------------------------------------------------------------\n");

	    double granTotal = 0;

	    for (Transaccion t : historial) {
	        if ((t.getFecha().after(fechaInicial) || t.getFecha().equals(fechaInicial)) && 
	            (t.getFecha().before(fechaFinal) || t.getFecha().equals(fechaFinal))) {

	            Calendar f = t.getFecha();
	            String infoFecha = String.format("D:%d/S:%d/M:%d", 
	                                f.get(Calendar.DAY_OF_MONTH), 
	                                f.get(Calendar.WEEK_OF_YEAR), 
	                                f.get(Calendar.MONTH) + 1);

	            for (Producto p : t.getProductos()) {
	                double precioBase = p.getPrecio();
	                double impuesto = precioBase * p.getTasaImpuesto();
	                double precioFinalConImpuesto = p.calcularPrecioFinal();

	                reporte.append(String.format("%-15s | %-12s | %-10.2f | %-10.2f | %-10.2f\n",
	                        p.getNombre(), 
	                        infoFecha, 
	                        precioBase, 
	                        impuesto, 
	                        precioFinalConImpuesto));
	            }

	            granTotal += t.calcularTotal();
	        }
	    }

	    reporte.append("--------------------------------------------------------------------------------\n");
	    reporte.append(String.format("TOTAL NETO EN PERIODO (Con descuentos y propinas): $%.2f\n", granTotal));
	    
	    return reporte.toString();
	}
}	

