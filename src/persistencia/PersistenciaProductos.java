package persistencia; // Ajusta el package según tu estructura

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import modelo.*;
import modelo.producto.*;
import exceptions.*;

public class PersistenciaProductos {
    
    public static  void descargarProductos(String juegosPrestamoArchivo, String juegosVentaArchivo, 
             String juegosDificilesArchivo, String bebidasArchivo,
             String platillosArchivo, Cafe miCafe) throws IOException, FileNotFoundException {
        
    	
    	ArrayList<Juego> juegosVenta = descargarJuegos(juegosVentaArchivo);
        ArrayList<Juego> juegosPrestamo = descargarJuegos(juegosPrestamoArchivo);
        ArrayList<Juego> juegosDificiles =  descargarJuegos(juegosDificilesArchivo);
        
	    for (Juego juego:juegosVenta) {
	    	miCafe.getJuegosVenta().add(juego);
	        }
	    
	    for (Juego juego:juegosPrestamo) {
	    	miCafe.getJuegosPrestamo().add(juego);
	        }
        
	    for (Juego juego:juegosDificiles) {
	    	miCafe.getJuegosPrestamo().add(juego);
	        }
	    
	    ArrayList<Platillo> platillos = descargarPlatillos(platillosArchivo);
	    ArrayList<Bebida> bebidas = descargarBebidas(bebidasArchivo);
	    
	    for (Bebida bebida:bebidas) {
	    	miCafe.getMenuBebidas().add(bebida);
	        }
	    
	    for (Platillo platillo:platillos) {
	    	miCafe.getMenuPlatillos().add(platillo);
	        }
	    
        }
    
    public static void salvarProductos(String juegosPrestamoArchivo, String juegosVentaArchivo, 
    		String juegosDificilesArchivo, String bebidasArchivo, String platillosArchivo, 
            Cafe miCafe) throws IOException {
    	salvarJuegos(juegosVentaArchivo,juegosPrestamoArchivo, juegosDificilesArchivo, miCafe);
    	salvarPlatillos(platillosArchivo, miCafe);
    	salvarBebidas(bebidasArchivo,miCafe);
    	
    }
    
    public static void salvarJuegos( String juegosPrestamoArchivo, String juegosVentaArchivo, 
        	String juegosDificilesArchivo, Cafe miCafe) throws  IOException {
        	
     JSONArray juegosPrestamoArray = new JSONArray();
     JSONArray juegosVentaArray = new JSONArray();
     JSONArray juegosDificilesArray = new JSONArray();
        	
     for (Juego juego : miCafe.juegosPrestamo) {
        	JSONObject jJuego = new JSONObject();
        	jJuego.put("id", juego.getId());
        	jJuego.put("nombre", juego.getNombre());
        	jJuego.put("precio", juego.getPrecio());
        	jJuego.put("anioPublicacion", juego.getAnioPublicacion());
        	jJuego.put("empresMatriz", juego.getEmpresMatriz());
        	jJuego.put("numJugadores", juego.getNumJugadores());
        	jJuego.put("restriccionEdad", juego.getRestriccionEdad());
        	jJuego.put("categoria", juego.getCategoria());
        		
        	if (juego instanceof JuegoDificil) {
        			JuegoDificil juegoDif = (JuegoDificil) juego;
        			jJuego.put("instrucciones", juegoDif.getInstrucciones());
        			juegosDificilesArray.put(jJuego);  
        		} else {
        			juegosPrestamoArray.put(jJuego);  
        		}   		
        } 

     for (Juego juego : miCafe.juegosVenta) {
        	JSONObject jJuego = new JSONObject();
        	jJuego.put("id", juego.getId());
        	jJuego.put("nombre", juego.getNombre());
        	jJuego.put("precio", juego.getPrecio());
        	jJuego.put("anioPublicacion", juego.getAnioPublicacion());
        	jJuego.put("empresMatriz", juego.getEmpresMatriz());
        	jJuego.put("numJugadores", juego.getNumJugadores());
        	jJuego.put("restriccionEdad", juego.getRestriccionEdad());
        	jJuego.put("categoria", juego.getCategoria());
        	juegosVentaArray.put(jJuego);  
        	} 
        	
     try (PrintWriter pwPrestamo = new PrintWriter(juegosPrestamoArchivo);
          PrintWriter pwDificiles = new PrintWriter(juegosDificilesArchivo);
          PrintWriter pwVenta = new PrintWriter(juegosVentaArchivo)) {
         
         pwPrestamo.print(juegosPrestamoArray.toString(4));
         pwDificiles.print(juegosDificilesArray.toString(4));
         pwVenta.print(juegosVentaArray.toString(4));
     }
    }
    
    
    
    public static void salvarPlatillos(String platillosArchivos, Cafe miCafe) throws IOException, FileNotFoundException {
    	JSONArray platillosArray = new JSONArray();
    	
    	for (Platillo platillo : miCafe.menuPlatillos) {
        	JSONObject jPlatillo = new JSONObject();
        	jPlatillo.put("id", platillo.getId());
        	jPlatillo.put("nombre", platillo.getNombre());
        	jPlatillo.put("precio", platillo.getPrecio());
        	jPlatillo.put("alergenos", platillo.getAlergeneos());
        	platillosArray.put(jPlatillo);  
        	} 
    	
    	try (PrintWriter pwPlatillo = new PrintWriter(platillosArchivos)){
    		pwPlatillo.print(platillosArray.toString(4));
    	}
    }
    
	public static void salvarBebidas(String bebidasArchivos, Cafe miCafe) throws IOException, FileNotFoundException {
		JSONArray bebidasArray = new JSONArray();
    	
    	for (Bebida bebida : miCafe.menuBebidas) {
        	JSONObject jBebidda = new JSONObject();
        	jBebidda.put("id", bebida.getId());
        	jBebidda.put("nombre", bebida.getNombre());
        	jBebidda.put("precio", bebida.getPrecio());
        	jBebidda.put("temperatura", bebida.getTemperatura());
        	jBebidda.put("esAlcoholica", bebida.isTieneAlcohol());
        	bebidasArray.put(jBebidda);  
        	} 
    	
    	try (PrintWriter pwPlatillo = new PrintWriter(bebidasArchivos)){
    		pwPlatillo.print(bebidasArray.toString(4));
    	}
	    }
    //
    public static ArrayList<Juego> descargarJuegos(String juegoArchivo) throws IOException, FileNotFoundException{
        ArrayList<Juego> juegosCargados = new ArrayList<>();
        
        File archivoJuego = new File(juegoArchivo);
        if (!archivoJuego.exists()) {
            throw new FileNotFoundException( juegoArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoJuego.toPath()));
        JSONArray jJuegos = new JSONArray(contenido);
        
        for (int i = 0; i < jJuegos.length(); i++) {
            JSONObject jJuego = jJuegos.getJSONObject(i);
            
            int id = jJuego.getInt("id");
            String nombre = jJuego.getString("nombre");
            int precio = jJuego.getInt("precio");
            int anioPublicacion = jJuego.getInt("anioPublicacion");
            String empresMatriz = jJuego.getString("empresMatriz");
            int numJugadores = jJuego.getInt("numJugadores");
            String restriccionEdad = jJuego.getString("restriccionEdad");
            String categoria = jJuego.getString("categoria");
            
            Juego nuevoJuego;
            if (jJuego.has("instrucciones")) {
                String instrucciones = jJuego.getString("instrucciones");
                nuevoJuego = new JuegoDificil(id, precio, nombre, anioPublicacion, 
                                              empresMatriz, numJugadores, restriccionEdad, 
                                              categoria, instrucciones);
            } else {
                nuevoJuego = new Juego(id, precio, nombre, anioPublicacion, 
                                       empresMatriz, numJugadores, restriccionEdad, categoria);
            }
            
            juegosCargados.add(nuevoJuego);  
        }
        
        return juegosCargados;  
    }
    
    public static ArrayList<Platillo> descargarPlatillos(String platilloArchivo)
    		throws FileNotFoundException, IOException{
    	ArrayList<Platillo> platillos = new ArrayList<>();
        
        File archivoPlatillo = new File(platilloArchivo);
        if (!archivoPlatillo.exists()) {
            throw new FileNotFoundException(platilloArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoPlatillo.toPath())); // Me da error esta línea de código
        JSONArray jPlatillos = new JSONArray(contenido);
        
        for (int i = 0; i < jPlatillos.length(); i++) {
            JSONObject jPlatillo = jPlatillos.getJSONObject(i);
            
            int id = jPlatillo.getInt("id");
            String nombre = jPlatillo.getString("nombre");
            int precio = jPlatillo.getInt("precio");
            String alergenos = jPlatillo.getString("alergenos");
            Platillo nuevoPlatillo = new Platillo(id, precio, nombre, alergenos);
            platillos.add(nuevoPlatillo);  
    	}
		return platillos;
    }

    public static ArrayList<Bebida> descargarBebidas(String bebidaArchivo)
    		throws FileNotFoundException, IOException{
    	ArrayList<Bebida> bebidas = new ArrayList<>();
        
        File archivoBebidas = new File(bebidaArchivo);
        if (!archivoBebidas.exists()) {
            throw new FileNotFoundException(bebidaArchivo);
        }
        
        String contenido = new String(Files.readAllBytes(archivoBebidas.toPath())); // Me da error esta línea de código
        JSONArray jBebidas= new JSONArray(contenido);
        
        for (int i = 0; i < jBebidas.length(); i++) {
            JSONObject jBebida = jBebidas.getJSONObject(i);
            
            int id = jBebida.getInt("id");
            String nombre = jBebida.getString("nombre");
            int precio = jBebida.getInt("temperatura");
            String temperatura = jBebida.getString("temperatura");
            boolean alcohol = jBebida.getBoolean("esAlcoholica");
            Bebida nuevaBebida= new Bebida(id, precio, nombre, temperatura, alcohol);
            bebidas.add(nuevaBebida);  
    	}
		return bebidas;
    }
    
   
    
    
}