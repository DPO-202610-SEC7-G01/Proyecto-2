package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modelo.Cafe;
import modelo.Mesa;
import modelo.producto.Bebida;
import modelo.producto.Platillo;
import modelo.producto.Juego;
import modelo.producto.JuegoDificil;
import modelo.usuario.Empleado;
import modelo.usuario.Administrador;
import modelo.usuario.Mesero;
import modelo.usuario.Cocinero;

/**
 * Clase encargada de cargar y salvar la información base del café (Infraestructura) en formato JSON.
 */
public class PersistenciaCafeJson implements IPersistenciaCafe {

    @Override
    public void cargarCafe(String archivo, Cafe cafe) throws IOException, Exception {
        try {
            String contenido = new String(Files.readAllBytes(new File(archivo).toPath()));
            JSONObject root = new JSONObject(contenido);

            cargarMesas(cafe, root.getJSONArray("mesas"));
            cargarProductos(cafe, root.getJSONArray("productos"));
            cargarEmpleados(cafe, root.getJSONArray("empleados"));
            
            // Cargar al administrador si existe en el JSON
            if (root.has("administrador")) {
                cargarAdministrador(cafe, root.getJSONObject("administrador"));
            }

        } catch (JSONException e) {
            throw new Exception("El formato del archivo JSON es inválido: " + e.getMessage());
        }
    }

    @Override
    public void salvarCafe(String archivo, Cafe cafe) throws IOException {
        JSONObject root = new JSONObject();

        // 1. Salvar Mesas
        JSONArray jMesas = new JSONArray();
        for (Mesa mesa : cafe.getMesas()) { 
            JSONObject jMesa = new JSONObject();
            jMesa.put("idMesa", mesa.getId()); 
            jMesa.put("numSillas", mesa.getNumSillas());

            jMesa.put("ocupada", !mesa.isDisponible()); 
            jMesas.put(jMesa);
        }
        root.put("mesas", jMesas);

        // 2. Salvar Productos (Recorriendo las 4 listas de Cafe.java)
        JSONArray jProductos = new JSONArray();
        
        // 2.1 Platillos
        for (Platillo prod : cafe.getMenuPlatillos()) { 
            JSONObject jProd = new JSONObject();
            jProd.put("id", prod.getId());
            jProd.put("nombre", prod.getNombre());
            jProd.put("precio", prod.getPrecio());
            jProd.put("tipo", "Platillo");
            jProd.put("alergenos", prod.getAlergeneos());
            jProductos.put(jProd);
        }
        
        // 2.2 Bebidas
        for (Bebida prod : cafe.getMenuBebidas()) { 
            JSONObject jProd = new JSONObject();
            jProd.put("id", prod.getId());
            jProd.put("nombre", prod.getNombre());
            jProd.put("precio", prod.getPrecio());
            jProd.put("tipo", "Bebida");
            jProd.put("tieneAlcohol", prod.isTieneAlcohol());
            jProd.put("temperatura", prod.getTemperatura());
            jProductos.put(jProd);
        }

        // 2.3 Juegos para Préstamo
        for (Juego prod : cafe.getJuegosPrestamo()) {
            JSONObject jProd = new JSONObject();
            jProd.put("id", prod.getId());
            jProd.put("nombre", prod.getNombre());
            jProd.put("precio", prod.getPrecio());
            jProd.put("esPrestamo", true); // ¡Clave para saber dónde cargarlo!
            
            if (prod instanceof JuegoDificil) {
                jProd.put("tipo", "JuegoDificil");
                jProd.put("hayInstructor", ((JuegoDificil) prod).requiereInstructor());
            } else {
                jProd.put("tipo", "Juego");
            }
            salvarDatosJuegoBase(jProd, prod);
            jProductos.put(jProd);
        }

        // 2.4 Juegos para Venta
        for (Juego prod : cafe.getJuegosVenta()) {
            JSONObject jProd = new JSONObject();
            jProd.put("id", prod.getId());
            jProd.put("nombre", prod.getNombre());
            jProd.put("precio", prod.getPrecio());
            jProd.put("esPrestamo", false); 
            
            if (prod instanceof JuegoDificil) {
                jProd.put("tipo", "JuegoDificil");
                jProd.put("hayInstructor", ((JuegoDificil) prod).requiereInstructor());
            } else {
                jProd.put("tipo", "Juego");
            }
            salvarDatosJuegoBase(jProd, prod);
            jProductos.put(jProd);
        }
        
        root.put("productos", jProductos);

        // 3. Salvar Administrador
        Administrador admin = cafe.getAdmin(); 
        if (admin != null) {
            JSONObject jAdmin = new JSONObject();
            jAdmin.put("id", admin.getId());
            jAdmin.put("nombre", admin.getNombre());
            jAdmin.put("login", admin.getLogin());
            jAdmin.put("clave", admin.getPassword());
            root.put("administrador", jAdmin);
        }

        // 4. Salvar Empleados (Solo Meseros y Cocineros)
        JSONArray jEmpleados = new JSONArray();
        for (Empleado emp : cafe.getEmpleados()) { 
            JSONObject jEmp = new JSONObject();
            jEmp.put("id", emp.getId());
            jEmp.put("nombre", emp.getNombre());
            jEmp.put("login", emp.getLogin());
            jEmp.put("clave", emp.getPassword());

            if (emp instanceof Mesero) {
                jEmp.put("tipo", "Mesero");
            } else if (emp instanceof Cocinero) {
                jEmp.put("tipo", "Cocinero");
            }
            jEmpleados.put(jEmp);
        }
        root.put("empleados", jEmpleados);

        // Escribir al archivo
        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.print(root.toString(4)); // Indentación de 4 espacios
        }
    }

    // --- Métodos Auxiliares para Cargar ---

    private void cargarMesas(Cafe cafe, JSONArray jMesas) {
        for (int i = 0; i < jMesas.length(); i++) {
            JSONObject jMesa = jMesas.getJSONObject(i);
            Mesa mesa = new Mesa(jMesa.getInt("idMesa"), jMesa.getInt("numSillas"), jMesa.getBoolean("ocupada"));
            cafe.agregarMesa(mesa);
        }
    }

    private void cargarProductos(Cafe cafe, JSONArray jProductos) {
        for (int i = 0; i < jProductos.length(); i++) {
            JSONObject jProd = jProductos.getJSONObject(i);
            String tipo = jProd.getString("tipo");
            
            int id = jProd.getInt("id");
            String nombre = jProd.getString("nombre");
            int precio = jProd.getInt("precio");

            if (tipo.equals("Bebida")) {
                boolean tieneAlcohol = jProd.getBoolean("tieneAlcohol");
                String temperatura = jProd.getString("temperatura");
                Bebida nuevaBebida = new Bebida(id, precio, nombre, temperatura, tieneAlcohol);
                cafe.getMenuBebidas().add(nuevaBebida); // Se añade directamente a la lista de bebidas
            } 
            else if (tipo.equals("Platillo")) {
                String alergenos = jProd.getString("alergenos");
                Platillo nuevoPlatillo = new Platillo(id, precio, nombre, alergenos);
                cafe.getMenuPlatillos().add(nuevoPlatillo); // Se añade directamente a la lista de platillos
            } 
            else if (tipo.equals("Juego") || tipo.equals("JuegoDificil")) {
                boolean esPrestamo = jProd.getBoolean("esPrestamo");
                
                // Extraer los datos comunes que piden ambos constructores
                int anioPublicacion = jProd.getInt("anioPublicacion");
                String empresMatriz = jProd.getString("empresMatriz");
                int numJugadores = jProd.getInt("numJugadores");
                String restriccionEdad = jProd.getString("restriccionEdad");
                String categoria = jProd.getString("categoria");
                
                Juego nuevoJuego = null;

                if (tipo.equals("JuegoDificil")) {
                    String instrucciones = jProd.getString("instrucciones");
                    
                    nuevoJuego = new JuegoDificil(id, precio, nombre, anioPublicacion, empresMatriz, numJugadores, restriccionEdad, categoria, instrucciones);
                } else {
                    // Constructor Juego: id, precio, nombre, anioPublicacion, empresMatriz, numJugadores, restriccionEdad, categoria
                    nuevoJuego = new Juego(id, precio, nombre, anioPublicacion, empresMatriz, numJugadores, restriccionEdad, categoria);
                }

                // Dependiendo del JSON, lo metemos en Venta o en Préstamo
                if (esPrestamo) {
                    cafe.agregarJuegoPrestamo(nuevoJuego);
                } else {
                    cafe.agregarJuegoVenta(nuevoJuego);
                }
            }
                }
            }
        
    

    private void cargarAdministrador(Cafe cafe, JSONObject jAdmin) {
        // 1. Extraemos los datos del JSON
        int id = jAdmin.getInt("id");
        String login = jAdmin.getString("login");
        String password = jAdmin.getString("clave"); // Usamos "clave" del JSON para el password
        String nombre = jAdmin.getString("nombre");
        
        // 2. Creamos el Administrador usando el objeto 'cafe' que ya tenemos en memoria
        Administrador admin = new Administrador(id, login, password, nombre, cafe);
        
        // 3. Lo vinculamos al café
        cafe.cambiarAdmin(admin); 
    }
    private void cargarEmpleados(Cafe cafe, JSONArray jEmpleados) {
        for (int i = 0; i < jEmpleados.length(); i++) {
            JSONObject jEmp = jEmpleados.getJSONObject(i);
            String tipo = jEmp.getString("tipo");

            int id = jEmp.getInt("id");
            String nombre = jEmp.getString("nombre");
            String login = jEmp.getString("login");
            String clave = jEmp.getString("clave");

            Empleado nuevoEmpleado = null;

            if (tipo.equals("Mesero")) {
                nuevoEmpleado = new Mesero(id, nombre, login, clave);
            } else if (tipo.equals("Cocinero")) {
                nuevoEmpleado = new Cocinero(id, nombre, login, clave);
            }

            if (nuevoEmpleado != null) {
                cafe.agregarEmpleado(nuevoEmpleado);
            }
        }
    }

    // --- Método Auxiliar para Evitar Repetir Código ---
    private void salvarDatosJuegoBase(JSONObject jProd, Juego juego) {
        jProd.put("anioPublicacion", juego.getAnioPublicacion());
        jProd.put("empresMatriz", juego.getEmpresMatriz());
        jProd.put("numJugadores", juego.getNumJugadores());
        jProd.put("restriccionEdad", juego.getRestriccionEdad());
        jProd.put("categoria", juego.getCategoria());
    }
}