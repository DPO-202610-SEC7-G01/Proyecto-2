package persistencia;

//utils
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//exceptions
import exceptions.*;
import java.io.FileNotFoundException;
import java.io.IOException;

//modelo
import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class PersistenciaUsuarios  extends PersistenciaCentral{
	
	public static void descargarUsuarios(String administradorArchivo, String cocinerosArchivo,
			String meserosArchivo, String clientesArchivo, Cafe miCafe) 
			throws FileNotFoundException, IOException, JSONException, NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException{
		
		descargarAdministrador(administradorArchivo, miCafe);
		
		ArrayList<Cliente> clientes = descargarClientes(clientesArchivo);
		for (Cliente  cliente:clientes) {
	    	miCafe.getClientes().add(cliente);
	        }
		
        ArrayList<Cocinero> cocineros = descargarCocineros(cocinerosArchivo); 
        for (Empleado  empleado:cocineros) {
	    	miCafe.agregarEmpleado(empleado);
	        }
        
        ArrayList<Mesero> meseros = descargarMeseros(meserosArchivo); 
        for (Empleado  empleado:meseros) {
	    	miCafe.agregarEmpleado(empleado);
	        }
        
	}
	
	public static void salvarUsuarios(String administradorArchivo, String cocinerosArchivo,
			String meserosArchivo, String clientesArchivo, Cafe miCafe) 
			throws FileNotFoundException, IOException{
		
		salvarAdministrador(administradorArchivo,miCafe);
		salvarClientes(clientesArchivo,miCafe);
		salvarEmpleados(meserosArchivo,cocinerosArchivo,miCafe);
	}
		
	
	
	//descargar
	
	public static void descargarAdministrador(String administradorArchivo, Cafe miCafe) throws FileNotFoundException, IOException, JSONException, InvalidCredentialsException {      
	        JSONArray jAdmins = leerArchivoJSON(administradorArchivo);
	        
	        if (jAdmins.length() > 0) {
	            JSONObject jAdmin = jAdmins.getJSONObject(0); 
	            Administrador nuevoAdmin = new Administrador(
	            		jAdmin.getInt("id"), 
	            		jAdmin.getString("login"),
	            		jAdmin.getString("password"),
	            		jAdmin.getString("nombre"),miCafe);
	            
	            miCafe.cambiarAdmin(nuevoAdmin);
	        }
	        
		}
 
	public static ArrayList<Cliente> descargarClientes(String clientesArchivo) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {             
	    JSONArray jClientes = leerArchivoJSON(clientesArchivo);
	    ArrayList<Cliente> clientes = new ArrayList<>();
	    
	    for (int i = 0; i < jClientes.length(); i++) {
	        clientes.add(descargarClientes(jClientes.getJSONObject(i)));  
	    }
	    
	    return clientes;
	}

	public static Cliente descargarClientes(JSONObject jCliente) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
		
		ArrayList<String>  alergenos = new ArrayList<>();
		JSONArray jAlergenos = jCliente.optJSONArray("alergenos");
		 for (int j = 0; j < jAlergenos.length(); j++) {
			 String alergeno = jAlergenos.getJSONObject(j).toString();
        	 alergenos.add(alergeno);
        }	
		
	    Cliente nuevoCliente = new Cliente(
	        jCliente.getInt("id"),
	        jCliente.getString("login"),
	        jCliente.getString("password"),
	        jCliente.getString("nombre"),
	        jCliente.getInt("edad"),
	        alergenos
	    );
	    
	    
	    
	    if (jCliente.has("puntosFidelidad")) {
	        nuevoCliente.sumarPuntosFidelidad(jCliente.getInt("puntosFidelidad"));
	    }
	    
	    JSONArray juegosFavoritos = jCliente.optJSONArray("juegosFavoritos");
	    if (juegosFavoritos != null) {
	        for (int i = 0; i < juegosFavoritos.length(); i++) {
	            Juego juego = PersistenciaProductos.descargarJuegos(juegosFavoritos.getJSONObject(i));
	            nuevoCliente.getJuegosFavoritos().add(juego);
	        }
	    }
	    
	    nuevoCliente.setAmigos(jCliente.getBoolean("amigos"));
	  
	    
	    return nuevoCliente;
	}
	

	
	
	public static ArrayList<Cocinero> descargarCocineros(String cocinerosArchivo) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
	    ArrayList<Cocinero> chefsCargados = new ArrayList<>();
	    JSONArray jEmpleados = leerArchivoJSON(cocinerosArchivo);
	    
	    for (int i = 0; i < jEmpleados.length(); i++) {
	        JSONObject jEmpleado = jEmpleados.getJSONObject(i);
	        
	        Cocinero nuevoChef = new Cocinero(
	            jEmpleado.getInt("id"),
	            jEmpleado.getString("login"),
	            jEmpleado.getString("password"),
	            jEmpleado.getString("nombre")
	        );
	        
	        cargarPuntosFidelidad(jEmpleado, nuevoChef);
	        cargarJuegosFavoritos(jEmpleado, nuevoChef);
	        cargarAmigos(jEmpleado, nuevoChef);
	        
	        JSONArray bebidasArray = jEmpleado.optJSONArray("bebidasConocidas");
	        JSONArray platillosArray = jEmpleado.optJSONArray("platillosConocidos");

	        if (platillosArray != null) {
	            for (int j = 0; j < platillosArray.length(); j++) {
	            	 Platillo platillo =PersistenciaProductos.descargarPlatillos(platillosArray.getJSONObject(i));  
	            	 nuevoChef.aprenderPlatillo(platillo);
	            }	
	        }
	        
	        if (bebidasArray != null) {
	        	for (int j = 0; j < bebidasArray.length(); j++) {
	            	 Bebida bebida =PersistenciaProductos.descargarBebidas(bebidasArray.getJSONObject(i));  
	            	 nuevoChef.aprenderBebida(bebida);
	            }	   
	        }
	        
	        chefsCargados.add(nuevoChef);
	    }
	    return chefsCargados;
	}

	public static ArrayList<Mesero> descargarMeseros(String meserosArchivo) throws IOException, FileNotFoundException, JSONException, 
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
	    ArrayList<Mesero> empleadosCargados = new ArrayList<>();
	    JSONArray jEmpleados = leerArchivoJSON(meserosArchivo);
	    
	    for (int i = 0; i < jEmpleados.length(); i++) {
	        JSONObject jEmpleado = jEmpleados.getJSONObject(i);
	        
	        Mesero nuevoEmpleado = new Mesero(
	            jEmpleado.getInt("id"),
	            jEmpleado.getString("login"),
	            jEmpleado.getString("password"),
	            jEmpleado.getString("nombre")
	        );
	        
	        cargarPuntosFidelidad(jEmpleado, nuevoEmpleado);
	        cargarJuegosFavoritos(jEmpleado, nuevoEmpleado);
	        cargarAmigos(jEmpleado, nuevoEmpleado);
	        
	        JSONArray juegosArray = jEmpleado.optJSONArray("juegosConocidos");
	        if (juegosArray != null) {	            
	            for (int j = 0; j < juegosArray.length(); j++) {
	            	 Juego juego =PersistenciaProductos.descargarJuegos(juegosArray.getJSONObject(i)); 
	            	 if (juego instanceof JuegoDificil) {
	            	 nuevoEmpleado.aprenderJuegoDificil((JuegoDificil) juego);
	            	 }
	            }	
	         
	        
	        }
	            empleadosCargados.add(nuevoEmpleado);
	    }
	    return empleadosCargados;
	}
	
	private static void cargarPuntosFidelidad(JSONObject jEmpleado, Empleado empleado) {
	    if (jEmpleado.has("puntosFidelidad")) {
	        int puntosFidelidad = jEmpleado.getInt("puntosFidelidad");
	        empleado.sumarPuntosFidelidad(puntosFidelidad);
	    }
	}
	
	private static void cargarJuegosFavoritos(JSONObject jEmpleado, Empleado empleado) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException {
	    JSONArray juegosArray = jEmpleado.optJSONArray("juegosFavoritos");
	    if (juegosArray != null) {
            for (int j = 0; j < juegosArray.length(); j++) {
            	 Juego juego =PersistenciaProductos.descargarJuegos(juegosArray.getJSONObject(j));  
            	 empleado.getJuegosFavoritos().add(juego);
            }	
	    }
	}
	
	private static void cargarAmigos(JSONObject jEmpleado, Empleado empleado) throws IOException, FileNotFoundException, JSONException,
	NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, InvalidCredentialsException {
	    JSONArray amigosArray = jEmpleado.optJSONArray("amigos");
	    if (amigosArray != null) {
	    	for (int j=0; j< amigosArray.length(); j++) {
	    		Cliente amigo = descargarClientes(amigosArray.getJSONObject(j));
	    		empleado.agregarAmigo(amigo);
	    	}
	        
	    }
	}
	
	//salvar
		public static void salvarAdministrador(String administradorArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
		    JSONObject jAdmin = new JSONObject();
		    Administrador admin = miCafe.getAdmin();
		    
		    jAdmin.put("id", admin.getId());
		    jAdmin.put("login", admin.getLogin());
		    jAdmin.put("password", admin.getPassword());
		    jAdmin.put("nombre", admin.getNombre());
		    
		    guardarArchivoJSON(administradorArchivo,jAdmin);
		}
		
		public static void salvarClientes(String clientesArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
		    JSONArray jClientes = new JSONArray();
		    
		    for(Cliente cliente: miCafe.getClientes()) {
		    	jClientes.put(AsalvarClientes(cliente));
		    }
		    guardarArchivoJSON(clientesArchivo,jClientes);
		}
		
		public static JSONObject  AsalvarClientes(Cliente cliente) throws IOException, FileNotFoundException {
			JSONObject jCliente = new JSONObject();
			jCliente.put("id", cliente.getId());
			jCliente.put("login", cliente.getLogin());
			jCliente.put("password", cliente.getPassword());
			jCliente.put("nombre", cliente.getNombre());
			jCliente.put("edad", cliente.getEdad());
			jCliente.put("puntosFidelidad", cliente.getPuntosFidelidad());
			jCliente.put("alergenos", cliente.getAlergenos());
		    jCliente.put("amigos", cliente.getAmigos());  
		    
			
			JSONArray juegosArray = new JSONArray();
	        for (Juego juego : cliente.getJuegosFavoritos()) {
	            juegosArray.put(PersistenciaProductos.AsalvarJuegos(juego)); 
	        }
	        
	        jCliente.put("JuegosFavoritos", juegosArray); // No estoy segura de que esto si funciona 

	       JSONArray alergenos = new JSONArray();
	       for (String alergeno: cliente.getAlergenos()) {
	    	   alergenos.put(alergeno);
	       }
	        
	       jCliente.put("alergenos",alergenos);
	       
			return jCliente;
		}
		

		public static void salvarEmpleados(String meserosArchivo, String cocinerosArchivo, Cafe miCafe) 
				throws IOException, FileNotFoundException {
			JSONArray jCocinero = new JSONArray();
			JSONArray jMesero = new JSONArray();
			
		    for (Empleado empleado : miCafe.getEmpleados()) {
		        if (empleado instanceof Cocinero) {
		        	 Cocinero cocinero = (Cocinero) empleado;
		        	jCocinero.put(salvarCocinero(cocinero));
		        	}else {
		        	 Mesero mesero = (Mesero) empleado;	
		        	jMesero.put(salvarMesero(mesero));	
		        	}
		        }
		    
		    guardarArchivoJSON(cocinerosArchivo,jCocinero);
		    guardarArchivoJSON(meserosArchivo,jCocinero);
		}
		
		
		public static JSONObject salvarCocinero(Cocinero cocinero) throws IOException, FileNotFoundException {
			JSONObject jEmpleado = new JSONObject();
			
	        jEmpleado.put("id", cocinero.getId());
	        jEmpleado.put("login", cocinero.getLogin());
	        jEmpleado.put("password", cocinero.getPassword());
	        jEmpleado.put("nombre", cocinero.getNombre());
	        jEmpleado.put("puntosFidelidad", cocinero.getPuntosFidelidad());

	        guardarJuegosFavoritos(jEmpleado, cocinero);
	        guardarAmigos(jEmpleado, cocinero);
	        
	        JSONArray platillosArray = new JSONArray();
	        for (Platillo platillo : cocinero.getPlatillosConocidos()) {
	            platillosArray.put(PersistenciaProductos.AsalvarPlatillos(platillo)); 
	        }
	        jEmpleado.put("platillosConocidos", platillosArray);
	        
	        JSONArray bebidasArray = new JSONArray();
	        for (Bebida bebida : cocinero.getBebidasConocidas()) {
	            bebidasArray.put(PersistenciaProductos.AsalvarBebidas(bebida)); 
	        }
	        jEmpleado.put("bebidasConocidas", bebidasArray);
	        
	        return jEmpleado;
	    }
	   

		private static void guardarJuegosFavoritos(JSONObject jEmpleado, Empleado emplado) throws IOException, FileNotFoundException {
		    JSONArray juegosArray = new JSONArray();
		    for (Juego juego : emplado.getJuegosFavoritos()) {
		        juegosArray.put(PersistenciaProductos.AsalvarJuegos(juego));
		    }
		    jEmpleado.put("juegosFavoritos", juegosArray);
		}
		
		private static void guardarAmigos(JSONObject jEmpleado, Empleado empleado) throws IOException, FileNotFoundException {
		    JSONArray amigosArray = new JSONArray();
		    for (Cliente amigo : empleado.getAmigos()) {
		        amigosArray.put(AsalvarClientes(amigo));
		    }
		    jEmpleado.put("amigos", amigosArray);
		}


		public static JSONObject salvarMesero(Mesero mesero) throws IOException, FileNotFoundException {
			JSONObject jEmpleado = new JSONObject();
			
	        jEmpleado.put("id", mesero.getId());
	        jEmpleado.put("login", mesero.getLogin());
	        jEmpleado.put("password", mesero.getPassword());
	        jEmpleado.put("nombre", mesero.getNombre());
	        jEmpleado.put("puntosFidelidad", mesero.getPuntosFidelidad());

	        guardarJuegosFavoritos(jEmpleado, mesero); 
	        guardarAmigos(jEmpleado, mesero);
	        
	        JSONArray juegosArray = new JSONArray();
		    for (Juego juego : mesero.getJuegosFavoritos()) {
		        juegosArray.put(PersistenciaProductos.AsalvarJuegos(juego));
		    }
		    jEmpleado.put("juegosFavoritos", juegosArray);
		    
	        return jEmpleado;
	    }

}
