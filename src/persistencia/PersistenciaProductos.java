package persistencia; 


//utils
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

//exception
import org.json.JSONException;
import java.io.FileNotFoundException;
import exceptions.*;

//modelo 
import modelo.*;
import modelo.producto.*;


public class PersistenciaProductos extends PersistenciaCentral{
    
    public static  void descargarProductos(String juegosPrestamoArchivo, String juegosVentaArchivo, 
             String juegosDificilesArchivo, String bebidasArchivo,
             String platillosArchivo, Cafe miCafe) throws IOException, FileNotFoundException, JSONException,
            NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException, ProductosException {
        
    	
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
            Cafe miCafe) throws IOException, FileNotFoundException {
    	
    	salvarJuegos(juegosVentaArchivo,juegosPrestamoArchivo, juegosDificilesArchivo, miCafe);
    	salvarPlatillos(platillosArchivo, miCafe);
    	salvarBebidas(bebidasArchivo,miCafe);
    	
    }
    
    
    // descargar
    public static ArrayList<Juego> descargarJuegos(String juegoArchivo) throws IOException, FileNotFoundException, JSONException,
    NumeroJugadoresExcedidoException, RestriccionEdadInvalidaException, CategoriaInvalidaException {     
        JSONArray jJuegos = leerArchivoJSON(juegoArchivo);
        ArrayList<Juego> juegos = new ArrayList<>();
        
        for (int i = 0; i < jJuegos.length(); i++) {
            juegos.add(descargarJuegos(jJuegos.getJSONObject(i)));  
        }
        
        return juegos;
    }

    public static Juego descargarJuegos(JSONObject jJuego) throws IOException, FileNotFoundException, NumeroJugadoresExcedidoException, 
    RestriccionEdadInvalidaException, CategoriaInvalidaException {
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
        
        return nuevoJuego;
    }
    
    public static ArrayList<Platillo> descargarPlatillos(String platilloArchivo) throws  IOException,FileNotFoundException { 
    	JSONArray jPlatillos = leerArchivoJSON(platilloArchivo);
    	ArrayList<Platillo> platillos= new ArrayList<>();
    	
    	for (int i = 0; i < jPlatillos.length(); i++) {
        	platillos.add(descargarPlatillos( jPlatillos.getJSONObject(i)));  
        }
    	
 		return platillos;
    }

    public static Platillo descargarPlatillos(JSONObject jPlatillo) throws IOException, FileNotFoundException {
        ArrayList<String> alergenos = new ArrayList<>();
        
        // optJSONArray evita que el programa se estrelle si un platillo no tiene la llave "alergenos"
        JSONArray jAlergenos = jPlatillo.optJSONArray("alergenos");
        
        // Validación de seguridad: solo recorremos si la lista existe
        if (jAlergenos != null) {
            // CORRECCIÓN 1: Iterar sobre jAlergenos.length(), no sobre jPlatillo.length()
            for (int j = 0; j < jAlergenos.length(); j++) { 
                // CORRECCIÓN 2: Usar getString() porque el JSON tiene textos (ej. "Gluten"), no objetos anidados
                String alergeno = jAlergenos.getString(j); 
                alergenos.add(alergeno);
            }
        }
        
        // Instanciamos el platillo con la información extraída
        Platillo nuevoPlatillo = new Platillo(
                jPlatillo.getInt("id"),
                jPlatillo.getInt("precio"),
                jPlatillo.getString("nombre"),
                alergenos
        );
        
        return nuevoPlatillo;
    }
    
    public static ArrayList<Bebida> descargarBebidas(String bebidaArchivo) throws IOException, FileNotFoundException, JSONException, ProductosException {
    	JSONArray jBebidas = leerArchivoJSON(bebidaArchivo);
    	ArrayList<Bebida> bebidas = new ArrayList<>();
	        
	        for (int i = 0; i < jBebidas.length(); i++) {
	        	bebidas.add(descargarBebidas( jBebidas.getJSONObject(i)));  
	        }
    	
        return bebidas; 
    }
    
    public static Bebida descargarBebidas(JSONObject jBebida) throws IOException, FileNotFoundException, JSONException, ProductosException {           
            Bebida nuevaBebida = new Bebida(
            		jBebida.getInt("id"),
            		jBebida.getInt("precio"),
            		jBebida.getString("nombre"), 
            		jBebida.getString("temperatura"),
            		jBebida.getBoolean("esAlcoholica"));
            
  
        return nuevaBebida;
    }
    
    public static ArrayList<Producto> descargarProductos(JSONArray jProductos) throws IOException, FileNotFoundException, NumeroJugadoresExcedidoException,
    RestriccionEdadInvalidaException, CategoriaInvalidaException, JSONException, ProductosException {
        ArrayList<Producto> productos = new ArrayList<>();
        
        for (int i = 0; i < jProductos.length(); i++) {
            JSONObject jProducto = jProductos.getJSONObject(i);
            
            if (jProducto.has("temperatura")) {
                Bebida bebida = descargarBebidas(jProducto);
                productos.add(bebida);
            } 

            else if (jProducto.has("alergenos") && !jProducto.has("temperatura")) {
                Platillo platillo = descargarPlatillos(jProducto);
                productos.add(platillo);
            }

            else if (jProducto.has("anioPublicacion") && jProducto.has("categoria")) {
                Juego juego = descargarJuegos(jProducto);
                productos.add(juego);
            }
        }
        
        return productos;
    }
    
    //salvar  
    public static void salvarJuegos( String juegosPrestamoArchivo, String juegosVentaArchivo, 
        	String juegosDificilesArchivo, Cafe miCafe) throws  IOException,FileNotFoundException {
        	
     JSONArray juegosPrestamoArray = new JSONArray();
     JSONArray juegosVentaArray = new JSONArray();
     JSONArray juegosDificilesArray = new JSONArray();
        	
     for (Juego juego : miCafe.juegosPrestamo) {
        	JSONObject jJuego = AsalvarJuegos(juego);
        	if (juego instanceof JuegoDificil) {
    			juegosDificilesArray.put(jJuego);
    		} else {
        	juegosPrestamoArray.put(jJuego);}		
        } 

     for (Juego juego : miCafe.juegosVenta) {
    	 JSONObject jJuego = AsalvarJuegos(juego);
     	juegosVentaArray.put(jJuego);		
     } 

     guardarArchivoJSON(juegosPrestamoArchivo,juegosPrestamoArray);
     guardarArchivoJSON(juegosDificilesArchivo,juegosDificilesArray);
     guardarArchivoJSON(juegosVentaArchivo,juegosVentaArray);
    }
    
    public static JSONObject AsalvarJuegos(Juego juego)  throws  IOException,FileNotFoundException {
    	JSONObject jJuego = new JSONObject();
    	jJuego.put("id", juego.getId());
    	jJuego.put("nombre", juego.getNombre());
    	jJuego.put("precio", juego.getPrecio());
    	jJuego.put("anioPublicacion", juego.getAnioPublicacion());
    	jJuego.put("empresMatriz", juego.getEmpresMatriz());
    	jJuego.put("numJugadores", juego.getNumJugadores());
    	jJuego.put("restriccionEdad", juego.getRestriccionEdad());
    	jJuego.put("categoria", juego.getCategoria());
		jJuego.put("prestado", juego.estaDisponible());
    		
    	if (juego instanceof JuegoDificil) {
    			JuegoDificil juegoDif = (JuegoDificil) juego;
    			jJuego.put("instrucciones", juegoDif.getInstrucciones());
    		} 
    	
    	return jJuego;
    }
    
    public static void salvarPlatillos(String platillosArchivos, Cafe miCafe)  throws  IOException,FileNotFoundException {
    	JSONArray platillosArray = new JSONArray();
    	
    	for (Platillo platillo : miCafe.menuPlatillos) {
    		platillosArray.put(AsalvarPlatillos(platillo));
    	}
    	guardarArchivoJSON(platillosArchivos,platillosArray);
    }
    
    public static JSONObject AsalvarPlatillos(Platillo platillo)  throws  IOException,FileNotFoundException {  
    	
        JSONObject jPlatillo = new JSONObject();
        jPlatillo.put("id", platillo.getId());
        jPlatillo.put("nombre", platillo.getNombre());
        jPlatillo.put("precio", platillo.getPrecio());
        
        JSONArray alergenos = new JSONArray(); // Aun no se si funciona
	       for (String alergeno: platillo.getAlergeneos()) {
	    	   alergenos.put(alergeno);
	       }
	     
	        jPlatillo.put("alergenos", alergenos);

	       
        return jPlatillo;
    }
    
	public static void salvarBebidas(String bebidasArchivos, Cafe miCafe)  throws  IOException,FileNotFoundException {
		JSONArray bebidasArray = new JSONArray();
    	
    	for (Bebida bebida : miCafe.menuBebidas) {
        	bebidasArray.put(AsalvarBebidas(bebida));  
        	} 
    	
    	guardarArchivoJSON(bebidasArchivos,bebidasArray);
	}
	
	public static JSONObject AsalvarBebidas(Bebida bebida)  throws  IOException,FileNotFoundException {
		JSONObject jBebidda = new JSONObject();
    	jBebidda.put("id", bebida.getId());
    	jBebidda.put("nombre", bebida.getNombre());
    	jBebidda.put("precio", bebida.getPrecio());
    	jBebidda.put("temperatura", bebida.getTemperatura());
    	jBebidda.put("esAlcoholica", bebida.isTieneAlcohol());
    	return jBebidda;
	}
	
}