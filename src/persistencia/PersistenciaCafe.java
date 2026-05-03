package persistencia;

//util
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

//exceptions
import exceptions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONException;

//modelo
import modelo.*;
import modelo.usuario.*;
import modelo.producto.*;

public class PersistenciaCafe extends PersistenciaCentral{

	public static void descargarCafe(String reservasArchivo, String historialPrestamosArchivo,String sugerenciasPendientesArchivo,
			String transaccionesArchivo, String mesasArchivo, String  turnosArchivo, Cafe miCafe) throws IOException, FileNotFoundException, 
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, JSONException, InvalidCredentialsException{
		
		//Aun que el café empieza con 0 de disponibilidad entonces voy a aumentarlo acorde a la mesa
		descargarMesas(mesasArchivo,miCafe);
		descargarReservas(reservasArchivo,miCafe);
		descargarTransaccion(transaccionesArchivo,miCafe);
		descargarHistorialPrestamos(historialPrestamosArchivo,miCafe);
		descargarSugerenciasPendientes(sugerenciasPendientesArchivo, miCafe);
		descargarTurnos(turnosArchivo, miCafe);
		}
	
	public static void descargarMesas(String mesasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
		int capacidad = 0;
		JSONArray jMesas = leerArchivoJSON(mesasArchivo);
	    
		for (int i = 0; i < jMesas.length(); i++) {
            JSONObject jMesa = jMesas.getJSONObject(i);
            
            Mesa nuevaMesa = new Mesa(
            		jMesa.getInt("id"),
            		jMesa.getInt("numSillas"),
            		jMesa.getBoolean("disponible"));
            
            capacidad += jMesa.getInt("numSillas");            
            miCafe.getMesas().add(nuevaMesa);
		}
	     miCafe.SetCapacidad(capacidad);  
	}
	
	public static void descargarReservas(String reservasArchivo, Cafe miCafe) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
	    JSONArray jReservas = leerArchivoJSON(reservasArchivo);
	    for (int i = 0; i < jReservas.length(); i++) {
	        JSONObject jReserva = jReservas.getJSONObject(i);
	        
	        JSONArray jClientes = jReserva.optJSONArray("clientes");
	        ArrayList<Cliente> clientes = new ArrayList<>();
	        if (jClientes != null) {
	            for (int j = 0; j < jClientes.length(); j++) {
	                Cliente cliente = PersistenciaUsuarios.descargarClientes(jClientes.getJSONObject(j));
	                clientes.add(cliente);
	            }
	        }
	        
	        JSONArray jProductos = jReserva.optJSONArray("productos");
	        ArrayList<Producto> productos = PersistenciaProductos.descargarProductos(jProductos);
	        
	        Reserva reserva = new Reserva(
	            clientes, 
	            jReserva.getInt("numPersonas"),
	            fechaEnCalendar(jReserva.getString("fecha"))
	        ); 
	        
	        reserva.setFactura(productos);
	        if (jReserva.has("totalFactura")) {
	            reserva.setTotalFactura(jReserva.getInt("totalFactura"));
	        }
	        miCafe.getReservasPrevias().add(reserva);
	    }
	}

	public static void descargarTransaccion(String transaccionesArchivo, Cafe miCafe) throws IOException, FileNotFoundException, NumeroJugadoresExcedidoException,
	RestriccionEdadInvalidaException, CategoriaInvalidaException, JSONException, InvalidCredentialsException {
	    JSONArray jTransacciones = leerArchivoJSON(transaccionesArchivo);
	    
	    for (int i = 0; i < jTransacciones.length(); i++) {
	        JSONObject jTransaccion = jTransacciones.getJSONObject(i);
	        
	        JSONArray jProductos = jTransaccion.optJSONArray("productos");
	        ArrayList<Producto> productos = PersistenciaProductos.descargarProductos(jProductos);
	        
	        JSONObject jClienteFinal = jTransaccion.getJSONObject("cliente_final");
	        Cliente clienteFinal = PersistenciaUsuarios.descargarClientes(jClienteFinal);
	        
	        Transaccion transaccion = new Transaccion(
	            jTransaccion.getInt("id"),
	            fechaEnCalendar(jTransaccion.getString("fecha")),
	            productos,
	            clienteFinal,
	            jTransaccion.getBoolean("amigoEmpleado")
	        );
	        
	        miCafe.agregarTransaccion(transaccion);
	    }
	}
	
	public static void descargarHistorialPrestamos(String historialPrestamosArchivo, Cafe miCafe) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
	    JSONObject jHistorialCompleto = new JSONObject(new String(Files.readAllBytes(new File(historialPrestamosArchivo).toPath())));
	    JSONArray jHistorialPrestamos = jHistorialCompleto.getJSONArray("historialUsoJuegos");
	    
	    for (int i = 0; i < jHistorialPrestamos.length(); i++) {
	        JSONObject jRegistro = jHistorialPrestamos.getJSONObject(i);
	        
	        String fechaString = jRegistro.getString("fecha");
	        Calendar fecha = fechaEnCalendar(fechaString);
	        JSONObject jRegistroInterno = jRegistro.getJSONObject("registro");
	        
	        Cliente usuario = PersistenciaUsuarios.descargarClientes(jRegistroInterno.getJSONObject("usuario"));
	        Juego juego = PersistenciaProductos.descargarJuegos(jRegistroInterno.getJSONObject("juego"));
	        
	        miCafe.registrarJuegoEnHistorial(fecha, usuario, juego);
	        juego.setPrestado(true);
	    }
	}
	
	public static void descargarSugerenciasPendientes(String sugerenciasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
	    JSONObject jSugerenciasCompleto = new JSONObject(new String(Files.readAllBytes(new File(sugerenciasArchivo).toPath())));
	    JSONArray jPlatillos = jSugerenciasCompleto.optJSONArray("platillos");
	    
	    if (jPlatillos != null) {
	        for (int i = 0; i < jPlatillos.length(); i++) {
	            Platillo platillo = PersistenciaProductos.descargarPlatillos(jPlatillos.getJSONObject(i));
	            miCafe.agregarSugerencia(platillo);
	        }
	    }
	    
	    JSONArray jBebidas = jSugerenciasCompleto.optJSONArray("bebidas");
	    if (jBebidas != null) {
	        for (int i = 0; i < jBebidas.length(); i++) {
	            Bebida bebida = PersistenciaProductos.descargarBebidas(jBebidas.getJSONObject(i));
	            miCafe.agregarSugerencia(bebida);
	        }
	    }
	}
		
	public static void descargarTurnos(String turnosArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
	    JSONArray jTurnos = leerArchivoJSON(turnosArchivo);
	    
	    for (int i = 0; i < jTurnos.length(); i++) {
	        JSONObject jRegistro = jTurnos.getJSONObject(i);
	        
	        JSONObject jEmpleado = jRegistro.getJSONObject("empleado");
	        int idEmpleado = jEmpleado.getInt("id");
	        
	        for (Empleado empleado : miCafe.getEmpleados()) {
	            if (empleado.getId() == idEmpleado) {
	                JSONObject jTurnoAsignado = jRegistro.getJSONObject("turno_asignado");
	                String fechaString = jTurnoAsignado.getString("fecha");
	                Calendar fecha = fechaEnCalendar(fechaString);
	                boolean activo = jTurnoAsignado.getBoolean("activo");
	                
	                empleado.getTurnos().add(new Turno(fecha, activo));
	                miCafe.getTurnoEmpleados().put(empleado ,new Turno(fecha, activo));
	                break;
	            }
	        }
	    }
	}
	
	
	public void salvarCafe(String reservasArchivo, String historialPrestamosArchivo,String sugerenciasPendientesArchivo,
			String transaccionesArchivo, String mesasArchivo,String turnosArchivo, Cafe miCafe) 
					throws IOException, FileNotFoundException{
		
		salvarMesas(mesasArchivo,miCafe);
		salvarReservas(reservasArchivo,miCafe);
		salvarTransacciones(transaccionesArchivo,miCafe);
		salvarHistorialPrestamos(historialPrestamosArchivo,miCafe);
		salvarSugerenciasPendientes(sugerenciasPendientesArchivo, miCafe);
		salvarTurnos(turnosArchivo, miCafe);
		}
	
	public static void salvarMesas(String mesasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONArray jMesas = new JSONArray();
        for (Mesa mesa : miCafe.getMesas()) {
            JSONObject jMesa = new JSONObject();
            jMesa.put("id", mesa.getId());         
            jMesa.put("numSillas", mesa.getNumSillas());
            jMesa.put("disponible", mesa.isDisponible());
            jMesas.put(jMesa);
        }
        guardarArchivoJSON(mesasArchivo, jMesas);
    }
	
	public static void salvarReservas(String reservasArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONArray jReservas = new JSONArray();
        for (Reserva reserva : miCafe.getReservasPrevias()) {
            JSONObject jReserva = new JSONObject();

    
            JSONArray jClientes = new JSONArray();
            for (Cliente c : reserva.getClientes()) {
                jClientes.put(PersistenciaUsuarios.AsalvarClientes(c));
            }
            jReserva.put("clientes", jClientes);

            JSONArray jProductos = new JSONArray();
            for (Producto p : reserva.getFactura()) {
                if (p instanceof Platillo) {
                    jProductos.put(PersistenciaProductos.AsalvarPlatillos((Platillo) p));
                } else if (p instanceof Bebida) {
                    jProductos.put(PersistenciaProductos.AsalvarBebidas((Bebida) p));
                } else if (p instanceof Juego) {
                    jProductos.put(PersistenciaProductos.AsalvarJuegos((Juego) p));
                }
            }
            jReserva.put("productos", jProductos);

            jReserva.put("numPersonas", reserva.getNumPersonas());
            jReserva.put("fecha", calendarToString(reserva.getFecha()));
            jReserva.put("totalFactura", reserva.getTotalFactura());

            if (reserva.getMesa() != null) {
                jReserva.put("mesaId", reserva.getMesa().getId());
            }

            jReservas.put(jReserva);
        }
        guardarArchivoJSON(reservasArchivo, jReservas);
    }
	
	public static void salvarTransacciones(String transaccionesArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONArray jTransacciones = new JSONArray();
        for (Transaccion t : miCafe.getHistorialTransaccion()) {
            JSONObject jTrans = new JSONObject();
            jTrans.put("id", t.getId());
            jTrans.put("fecha", calendarToString(t.getFecha()));
            jTrans.put("amigoEmpleado", t.isAmigoEmpleado());

            JSONArray jProductos = new JSONArray();
            for (Producto p : t.getProductos()) {
                if (p instanceof Platillo) {
                    jProductos.put(PersistenciaProductos.AsalvarPlatillos((Platillo) p));
                } else if (p instanceof Bebida) {
                    jProductos.put(PersistenciaProductos.AsalvarBebidas((Bebida) p));
                } else if (p instanceof Juego) {
                    jProductos.put(PersistenciaProductos.AsalvarJuegos((Juego) p));
                }
            }
            jTrans.put("productos", jProductos);

            Usuario u = t.getCliente_final();
            if (u instanceof Cliente) {
                jTrans.put("cliente_final", PersistenciaUsuarios.AsalvarClientes((Cliente) u));
            } else {
                JSONObject jUser = new JSONObject();
                jUser.put("id", u.getId());
                jUser.put("login", u.getLogin());
                jUser.put("nombre", u.getNombre());
                jTrans.put("cliente_final", jUser);
            }

            jTransacciones.put(jTrans);
        }
        guardarArchivoJSON(transaccionesArchivo, jTransacciones);
    }
	
	public static void salvarHistorialPrestamos(String historialPrestamosArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONObject root = new JSONObject();
        JSONArray jHistorial = new JSONArray();

        HashMap<Calendar, HashMap<Usuario, Juego>> historial = miCafe.getHistorialUsoJuegos();
        for (Entry<Calendar, HashMap<Usuario, Juego>> entry : historial.entrySet()) {
            Calendar fecha = entry.getKey();
            HashMap<Usuario, Juego> mapaUsuarioJuego = entry.getValue();

            for (Entry<Usuario, Juego> subEntry : mapaUsuarioJuego.entrySet()) {
                Usuario usuario = subEntry.getKey();
                Juego juego = subEntry.getValue();

                JSONObject jRegistro = new JSONObject();
                jRegistro.put("fecha", calendarToString(fecha));

                JSONObject jRegistroInterno = new JSONObject();

                JSONObject jUsuario = new JSONObject();
                jUsuario.put("id", usuario.getId());
                jUsuario.put("login", usuario.getLogin());
                jUsuario.put("nombre", usuario.getNombre());
                jRegistroInterno.put("usuario", jUsuario);

                jRegistroInterno.put("juego", PersistenciaProductos.AsalvarJuegos(juego));

                jRegistro.put("registro", jRegistroInterno);
                jHistorial.put(jRegistro);
            }
        }

        root.put("historialUsoJuegos", jHistorial);
        guardarArchivoJSON(historialPrestamosArchivo, root);
    }
	
	public static void salvarSugerenciasPendientes(String sugerenciasPendientesArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        JSONObject root = new JSONObject();
        JSONArray jPlatillos = new JSONArray();
        JSONArray jBebidas = new JSONArray();

        for (Producto p : miCafe.getSugerenciasPendientes()) {
            if (p instanceof Platillo) {
                jPlatillos.put(PersistenciaProductos.AsalvarPlatillos((Platillo) p));
            } else if (p instanceof Bebida) {
                jBebidas.put(PersistenciaProductos.AsalvarBebidas((Bebida) p));
            }
        }
        root.put("platillos", jPlatillos);
        root.put("bebidas", jBebidas);
        guardarArchivoJSON(sugerenciasPendientesArchivo, root);
    }
	
	public static void salvarTurnos(String turnosArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
	    JSONArray jTurnos = new JSONArray();
	    for (Map.Entry<Empleado, Turno> entrada : miCafe.getTurnoEmpleados().entrySet()) {
	        JSONObject jRegistro = new JSONObject();
	        jRegistro.put("empleado", salvarEmpleadoParaTurno(entrada.getKey()));
	        
	        JSONObject jTurnoAsignado = new JSONObject();
	        jTurnoAsignado.put("fecha", calendarToString(entrada.getValue().getFecha()) + "T08:00:00Z");
	        jTurnoAsignado.put("activo", entrada.getValue().isActivo());
	        jRegistro.put("turno_asignado", jTurnoAsignado);
	        
	        jTurnos.put(jRegistro);
	    }
	    guardarArchivoJSON(turnosArchivo, jTurnos);
	}

	private static JSONObject salvarEmpleadoParaTurno(Empleado empleado) throws IOException, FileNotFoundException {
	    JSONObject jEmpleado = new JSONObject();
	    jEmpleado.put("id", empleado.getId());
	    jEmpleado.put("login", empleado.getLogin());
	    jEmpleado.put("password", empleado.getPassword());
	    jEmpleado.put("nombre", empleado.getNombre());
	    jEmpleado.put("puntosFidelidad", empleado.getPuntosFidelidad());
	    
	    JSONArray juegosArray = new JSONArray();
	    for (Juego juego : empleado.getJuegosFavoritos()) {
	        juegosArray.put(juego.getNombre());
	    }
	    jEmpleado.put("juegosFavoritos", juegosArray);
	    
	    JSONArray amigosArray = new JSONArray();
	    for (Cliente amigo : empleado.getAmigos()) {
	        amigosArray.put(amigo.getId());
	    }
	    jEmpleado.put("amigos", amigosArray);
	    
	    if (empleado instanceof Mesero) {
	        JSONArray juegosConocidosArray = new JSONArray();
	        for (Juego juego : ((Mesero) empleado).getJuegosConocidos()) {
	            juegosConocidosArray.put(juego.getNombre());
	        }
	        jEmpleado.put("juegosConocidos", juegosConocidosArray);
	    }
	    
	    if (empleado instanceof Cocinero) {
	        JSONArray platillosArray = new JSONArray();
	        for (Platillo platillo : ((Cocinero) empleado).getPlatillosConocidos()) {
	            platillosArray.put(platillo.getNombre());
	        }
	        jEmpleado.put("platillosConocidos", platillosArray);
	        
	        JSONArray bebidasArray = new JSONArray();
	        for (Bebida bebida : ((Cocinero) empleado).getBebidasConocidas()) {
	            bebidasArray.put(bebida.getNombre());
	        }
	        jEmpleado.put("bebidasConocidas", bebidasArray);
	    }
	    
	    jEmpleado.put("turnos", new JSONArray());
	    return jEmpleado;
	}
}
