package persistencia; // Ajusta el package según tu estructura

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import modelo.Cafe;
import modelo.producto.*;

public class PersistenciaProductos {
    
	 /**
     * DESCARGA/ CARGA los productos desde archivos JSON hacia el café
     * @param juegosPrestamoArchivo Ruta del archivo JSON de juegos de préstamo (ej: "data/juegosPrestamo.json")
     * @param juegosVentaArchivo Ruta del archivo JSON de juegos de venta (ej: "data/juegosVenta.json")
     * @param juegosDificilesArchivo Ruta del archivo JSON de juegos difíciles (ej: "data/juegosDificiles.json")
     * @param bebidasArchivo Ruta del archivo JSON de bebidas (ej: "data/bebidas.json")
     * @param platillosArchivo Ruta del archivo JSON de platillos (ej: "data/platillos.json")
     * @param micafe El objeto Café donde se cargarán los productos
     */
    public static void descargarProductos(String juegosPrestamoArchivo, String juegosVentaArchivo, 
             String juegosDificilesArchivo, String bebidasArchivo,
             String platillosArchivo, Cafe micafe) {
        
    	
    	ArrayList<Juego> juegosVenta = descargarJuegos(juegosVentaArchivo,micafe);
        ArrayList<Juego> juegosPrestamo = descargarJuegos(juegosPrestamoArchivo, micafe);
        ArrayList<Juego> juegosDificiles =  descargarJuegos(juegosDificilesArchivo,micafe);
    	
           
        }
    
 
    public ArrayList<Juego> descargarJuegos(String juegoArchivo, Cafe micafe) {
        ArrayList<Juego> juegosCargados = new ArrayList<>();
        
        try {
            File archivoJuego = new File(juegoArchivo);
            if (archivoJuego.exists()) {
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
                    
                    micafe.juegosPrestamo.add(nuevoJuego);
                    juegosCargados.add(nuevoJuego);  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return juegosCargados;  
    }
    
    /**
     * GUARDAR los productos del café en archivos JSON
     * @param juegosPrestamoArchivo Ruta donde guardar juegos de préstamo
     * @param juegosVentaArchivo Ruta donde guardar juegos de venta
     * @param bebidasArchivo Ruta donde guardar bebidas
     * @param platillosArchivo Ruta donde guardar platillos
     * @param micafe El objeto Café del cual extraer los productos
     */
    public static void salvarProductos(String juegosPrestamoArchivo, String juegosVentaArchivo, 
                                        String bebidasArchivo, String platillosArchivo, 
                                        Cafe micafe) {
        
        try {
            // ==================== 1. GUARDAR JUEGOS DE PRÉSTAMO ====================
            JSONArray jJuegosPrestamo = new JSONArray();
            
            // Recorrer todos los juegos de préstamo del café
            for (Juego juego : micafe.juegosPrestamo) {
                JSONObject jJuego = new JSONObject();
                
                // Atributos comunes para todos los juegos
                jJuego.put("id", juego.getId());
                jJuego.put("nombre", juego.getNombre());
                jJuego.put("precio", juego.getPrecio());
                jJuego.put("anioPublicacion", juego.getAnioPublicacion());
                jJuego.put("empresMatriz", juego.getEmpresMatriz());
                jJuego.put("numJugadores", juego.getNumJugadores());
                jJuego.put("restriccionEdad", juego.getRestriccionEdad());
                jJuego.put("categoria", juego.getCategoria());
                
                // Si es un juego difícil, guardar también las instrucciones
                if (juego instanceof JuegoDificil) {
                    JuegoDificil juegoDif = (JuegoDificil) juego;
                    jJuego.put("instrucciones", juegoDif.getInstrucciones());
                }
                
                jJuegosPrestamo.put(jJuego);
            }
            
            // Escribir el JSONArray al archivo
            try (PrintWriter pw = new PrintWriter(juegosPrestamoArchivo)) {
                pw.print(jJuegosPrestamo.toString(4)); // Indentación de 4 espacios para legibilidad
            }
            System.out.println("Guardados " + micafe.juegosPrestamo.size() + " juegos de préstamo");
            
            // ==================== 2. GUARDAR JUEGOS DE VENTA ====================
            JSONArray jJuegosVenta = new JSONArray();
            
            for (Juego juego : micafe.juegosVenta) {
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
                }
                
                jJuegosVenta.put(jJuego);
            }
            
            try (PrintWriter pw = new PrintWriter(juegosVentaArchivo)) {
                pw.print(jJuegosVenta.toString(4));
            }
            System.out.println("Guardados " + micafe.juegosVenta.size() + " juegos de venta");
            
            // ==================== 3. GUARDAR BEBIDAS ====================
            JSONArray jBebidas = new JSONArray();
            
            for (Bebida bebida : micafe.menuBebidas) {
                JSONObject jBebida = new JSONObject();
                
                jBebida.put("id", bebida.getId());
                jBebida.put("nombre", bebida.getNombre());
                jBebida.put("precio", bebida.getPrecio());
                jBebida.put("temperatura", bebida.getTemperatura());
                jBebida.put("tieneAlcohol", bebida.isTieneAlcohol());
                
                jBebidas.put(jBebida);
            }
            
            try (PrintWriter pw = new PrintWriter(bebidasArchivo)) {
                pw.print(jBebidas.toString(4));
            }
            System.out.println("Guardadas " + micafe.menuBebidas.size() + " bebidas");
            
            // ==================== 4. GUARDAR PLATILLOS ====================
            JSONArray jPlatillos = new JSONArray();
            
            for (Platillo platillo : micafe.menuPlatillos) {
                JSONObject jPlatillo = new JSONObject();
                
                jPlatillo.put("id", platillo.getId());
                jPlatillo.put("nombre", platillo.getNombre());
                jPlatillo.put("precio", platillo.getPrecio());
                jPlatillo.put("alergenos", platillo.getAlergeneos());
                
                jPlatillos.put(jPlatillo);
            }
            
            try (PrintWriter pw = new PrintWriter(platillosArchivo)) {
                pw.print(jPlatillos.toString(4));
            }
            System.out.println("Guardados " + micafe.menuPlatillos.size() + " platillos");
            
        } catch (IOException e) {
            System.err.println("Error al guardar archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}