package persistencia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.FileNotFoundException;
import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;

public class PersistenciaUsuarios {
	
	public static void descargarUsuarios(String administradorArchivo, String cocinerosArchivo,
			String meserosArchivo, String clientesArchivo, Cafe miCafe) throws FileNotFoundException, IOException{
		
		descargarAdministrador(administradorArchivo, miCafe);
		ArrayList<Cliente> clientes = descargarClientes(clientesArchivo);
		for (Cliente  cliente:clientes) {
	    	miCafe.getClientes().add(cliente);
	        }
		
        ArrayList<Cocinero> cocineros = descargarCocineros(cocinerosArchivo); 
        for (Empleado  empleado:cocineros) {
	    	miCafe.getEmpleados().add(empleado);
	        }
        
	}
	
	public static void descargarAdministrador(String administradorArchivo, Cafe miCafe) throws FileNotFoundException, IOException {
		File archivoAdmin = new File(administradorArchivo);
        if (!archivoAdmin.exists()) {
            throw new FileNotFoundException( administradorArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoAdmin.toPath()));
        JSONArray jAdmins = new JSONArray(contenido);
        
        if (jAdmins.length() > 0) {
            JSONObject jAdmin = jAdmins.getJSONObject(0); 
            
            int id = jAdmin.getInt("id");
            String nombre = jAdmin.getString("nombre");
            String login = jAdmin.getString("login");
            String password = jAdmin.getString("password");
            
            Administrador nuevoAdmin;
            nuevoAdmin = new Administrador(id, login, password, nombre,miCafe);
            miCafe.cambiarAdmin(nuevoAdmin);
        }
        
	}
	
	public static ArrayList<Cliente> descargarClientes(String clientesArchivo)  throws  IOException,FileNotFoundException {
        ArrayList<Cliente> clientesCargados = new ArrayList<>();
        
        File archivoCliente = new File(clientesArchivo);
        if (!archivoCliente.exists()) {
            throw new FileNotFoundException( clientesArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoCliente.toPath()));
        JSONArray jClientes = new JSONArray(contenido);
        
        for (int i = 0; i < jClientes.length(); i++) {
            JSONObject jCliente = jClientes.getJSONObject(i);
            
            int id = jCliente.getInt("id");
            String nombre = jCliente.getString("nombre");
            String login = jCliente.getString("login");
            String password = jCliente.getString("password");
            int edad = jCliente.getInt("edad");
            String alergenos = jCliente.getString("alergenos");
            
            Cliente nuevoCliente;
            nuevoCliente = new Cliente(id,login,password,nombre,edad,alergenos);            
            clientesCargados.add(nuevoCliente);  
        }
        
        return clientesCargados;  
    }
	
	public static ArrayList<Cocinero> descargarCocineros(String cocinerosArchivo)  throws  IOException,FileNotFoundException {
		ArrayList<Cocinero> chefsCargados = new ArrayList<>();

		File cocinerosEmpleado = new File(cocinerosArchivo);
        if (!cocinerosEmpleado.exists()) {
            throw new FileNotFoundException( cocinerosArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(cocinerosEmpleado.toPath()));
        JSONArray jEmpleados = new JSONArray(contenido);
        
        for (int i = 0; i < jEmpleados.length(); i++) {
            JSONObject jEmpleado = jEmpleados.getJSONObject(i);
            
            int id = jEmpleado.getInt("id");
            String nombre = jEmpleado.getString("nombre");
            String login = jEmpleado.getString("login");
            String password = jEmpleado.getString("password");
            Cocinero nuevoChef = new Cocinero(id, login, password, nombre);
            
        	JSONArray bebidasArray = jEmpleado.optJSONArray("bebidasConocidas");
        	JSONArray platillosArray = jEmpleado.optJSONArray("platillosConocidos");

        	ArrayList<Platillo> platillos = PersistenciaProductos.descargarPlatillos(platillosArray);
        	ArrayList<Bebida> bebidas = PersistenciaProductos.descargarBebidas(bebidasArray);

        	for (Platillo platillo : platillos) {
        		nuevoChef.getPlatillosConocidos().add(platillo);
        	}
        	for (Bebida bebida : bebidas) {
        		nuevoChef.getBebidasConocidas().add(bebida);
        	}
        }
		return chefsCargados;
		
	}
	
	public static ArrayList<Mesero> descargarMeseros(String meserosArchivo)  throws  IOException,FileNotFoundException {
        ArrayList<Mesero> empleadosCargados = new ArrayList<>();
        
        File archivoMesero = new File(meserosArchivo);
        if (!archivoMesero.exists()) {
            throw new FileNotFoundException( meserosArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoMesero.toPath()));
        JSONArray jEmpleados = new JSONArray(contenido);
        
        for (int i = 0; i < jEmpleados.length(); i++) {
            JSONObject jEmpleado = jEmpleados.getJSONObject(i);
            
            int id = jEmpleado.getInt("id");
            String nombre = jEmpleado.getString("nombre");
            String login = jEmpleado.getString("login");
            String password = jEmpleado.getString("password");
            
            Mesero nuevoEmpleado = new Mesero(id,login,password,nombre);
            empleadosCargados.add(nuevoEmpleado);  
            JSONArray juegosArray = jEmpleado.optJSONArray("juegosConocidos");
            
            ArrayList<Juego> juegos = PersistenciaProductos.descargarJuegos(juegosArray);

            for (Juego juego : juegos) {// Hay que hacer una excepción para cuando esto no es cierto
            	 JuegoDificil juegoD = (JuegoDificil) juego;  
                 nuevoEmpleado.aprenderJuegoDificil(juegoD);
            }
        }
        
        return empleadosCargados;  
    }
	
	public void salvarUsuarios(String administradorArchivo, String empleadosArchivo,
			String clientesArchivo, Cafe micafe){
		
		// El string que pasa por parámetro es la ruta de los archivos que debería ser
		// "data/bebidas.json" o lo que sea pero se le debe poner  "data/ ... " 
		
		
		}

}
