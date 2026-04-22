package persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modelo.Cafe;
import modelo.Mesa;
import modelo.Reserva;
import modelo.Transaccion;
import modelo.producto.Producto;
import modelo.usuario.Cliente;
import modelo.usuario.Usuario;
import modelo.usuario.Empleado;

/**
 * Clase encargada de cargar y salvar la actividad diaria del café (Operaciones) en formato JSON.
 */
public class PersistenciaOperacionesJson implements IPersistenciaOperaciones {

    @Override
    public void cargarOperaciones(String archivo, Cafe cafe) throws IOException, Exception {
        try {
            String contenido = new String(Files.readAllBytes(new File(archivo).toPath()));
            JSONObject root = new JSONObject(contenido);

            // IMPORTANTE: El orden de carga es vital. 
            // Los clientes deben existir antes de cargar las reservas y transacciones.
            cargarClientes(cafe, root.getJSONArray("clientes"));
            cargarReservas(cafe, root.getJSONArray("reservas"));
            cargarTransacciones(cafe, root.getJSONArray("transacciones"));

        } catch (JSONException e) {
            throw new Exception("El formato del archivo JSON de operaciones es inválido: " + e.getMessage());
        }
    }

    @Override
    public void salvarOperaciones(String archivo, Cafe cafe) throws IOException {
        JSONObject root = new JSONObject();

        // 1. Salvar Clientes
        JSONArray jClientes = new JSONArray();
        for (Cliente cliente : cafe.getClientes()) {
            JSONObject jCliente = new JSONObject();
            jCliente.put("id", cliente.getId());
            jCliente.put("nombre", cliente.getNombre());
            jCliente.put("login", cliente.getLogin());
            jCliente.put("clave", cliente.getPassword());
            jCliente.put("edad", cliente.getEdad());
            jCliente.put("alergenos", cliente.getAlergenos()); 
            jClientes.put(jCliente);
        }
        root.put("clientes", jClientes);

        // 2. Salvar Reservas
        JSONArray jReservas = new JSONArray();
        for (Reserva reserva : cafe.getReservasPrevias()) {
            JSONObject jRes = new JSONObject();
            jRes.put("fechaMillis", reserva.getFecha().getTimeInMillis());
            jRes.put("numPersonas", reserva.getNumPersonas());
            
            if (reserva.getMesa() != null) {
                jRes.put("idMesa", reserva.getMesa().getId()); 
            }

            // Guardar IDs de los clientes de esta reserva
            JSONArray jIdClientes = new JSONArray();
            for (Cliente c : reserva.getClientes()) {
                jIdClientes.put(c.getId());
            }
            jRes.put("clientes", jIdClientes);
            
            jReservas.put(jRes);
        }
        root.put("reservas", jReservas);

        // 3. Salvar Transacciones
        JSONArray jTransacciones = new JSONArray();
        for (Transaccion trans : cafe.getHistorialTransaccion()) {
            JSONObject jTrans = new JSONObject();
            
            jTrans.put("id", trans.getId()); 
            jTrans.put("fechaMillis", trans.getFecha().getTimeInMillis());
            

            jTrans.put("amigoEmpleado", trans.isAmigoEmpleado()); 
            

            if (trans.getCliente_final() != null) {
                jTrans.put("idUsuario", trans.getCliente_final().getId()); 
            }

            // Guardar IDs de los productos consumidos
            JSONArray jIdProductos = new JSONArray();
            for (Producto p : trans.getProductos()) { 
                jIdProductos.put(p.getId());
            }
            jTrans.put("productos", jIdProductos);

            jTransacciones.put(jTrans);
        }
        root.put("transacciones", jTransacciones);

        // Escribir al archivo
        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.print(root.toString(4)); 
        }
    }

    // --- Métodos Auxiliares para Cargar ---

    private void cargarClientes(Cafe cafe, JSONArray jClientes) {
        for (int i = 0; i < jClientes.length(); i++) {
            JSONObject jCliente = jClientes.getJSONObject(i);
            int id = jCliente.getInt("id");
            String nombre = jCliente.getString("nombre");
            String login = jCliente.getString("login");
            String password = jCliente.getString("clave");
            int edad = jCliente.getInt("edad");
            String alergenos = jCliente.optString("alergenos", ""); 

            Cliente cliente = new Cliente(id, login, password, nombre, edad, alergenos);
            cafe.agregarUsuario(cliente);
        }
    }

    private void cargarReservas(Cafe cafe, JSONArray jReservas) throws Exception {
        for (int i = 0; i < jReservas.length(); i++) {
            JSONObject jRes = jReservas.getJSONObject(i);
            
            Calendar fecha = Calendar.getInstance();
            fecha.setTimeInMillis(jRes.getLong("fechaMillis"));
            int numPersonas = jRes.getInt("numPersonas");

            // Buscar la lista de clientes
            JSONArray jIdClientes = jRes.getJSONArray("clientes");
            List<Cliente> clientesReserva = new ArrayList<>();
            for (int j = 0; j < jIdClientes.length(); j++) {
                int idCliente = jIdClientes.getInt(j);
                Cliente c = buscarCliente(cafe, idCliente);
                if (c != null) clientesReserva.add(c);
            }

            Reserva reserva = new Reserva(clientesReserva, numPersonas, fecha);
            
            // Asignar la mesa por separado
            if (jRes.has("idMesa")) {
                int idMesa = jRes.getInt("idMesa");
                Mesa mesa = buscarMesa(cafe, idMesa);
                if (mesa != null) {
                    reserva.setMesa(mesa); 
                }
            }
            
            cafe.getReservasPrevias().add(reserva);
        }
    }

    private void cargarTransacciones(Cafe cafe, JSONArray jTransacciones) {
        for (int i = 0; i < jTransacciones.length(); i++) {
            JSONObject jTrans = jTransacciones.getJSONObject(i);
            
            int id = jTrans.getInt("id");
            boolean amigoEmpleado = jTrans.getBoolean("amigoEmpleado");
            
            Calendar fecha = Calendar.getInstance();
            fecha.setTimeInMillis(jTrans.getLong("fechaMillis"));
            
            // Buscar al usuario
            Usuario usuario = null;
            if (jTrans.has("idUsuario")) {
                usuario = buscarUsuario(cafe, jTrans.getInt("idUsuario"));
            }

            // Armar la lista de productos
            List<Producto> productosTransaccion = new ArrayList<>();
            JSONArray jIdProductos = jTrans.getJSONArray("productos");
            for (int j = 0; j < jIdProductos.length(); j++) {
                int idProd = jIdProductos.getInt(j);
                Producto p = buscarProducto(cafe, idProd);
                if (p != null) {
                    productosTransaccion.add(p);
                }
            }


            Transaccion transaccion = new Transaccion(id, fecha, productosTransaccion, usuario, amigoEmpleado);
            
            cafe.getHistorialTransaccion().add(transaccion);
        }
    }

    // --- Métodos Auxiliares de Búsqueda (Conectando Modelos) ---

    private Mesa buscarMesa(Cafe cafe, int idMesa) {
        for (Mesa m : cafe.getMesas()) {
            if (m.getId() == idMesa) return m;
        }
        return null;
    }

    private Cliente buscarCliente(Cafe cafe, int idCliente) {
        for (Cliente c : cafe.getClientes()) {
            if (c.getId() == idCliente) return c;
        }
        return null;
    }
    
    private Usuario buscarUsuario(Cafe cafe, int idUsuario) {
        // Primero buscamos si es un cliente
        Usuario u = buscarCliente(cafe, idUsuario);
        if (u != null) return u;
        
        // Si no es cliente, buscamos si es un empleado (Mesero, Cocinero)
        for (Empleado e : cafe.getEmpleados()) {
            if (e.getId() == idUsuario) return e;
        }
        
        // Finalmente buscamos si es el administrador
        if (cafe.getAdmin() != null && cafe.getAdmin().getId() == idUsuario) {
            return cafe.getAdmin();
        }
        
        return null;
    }

    private Producto buscarProducto(Cafe cafe, int idProducto) {
        for (Producto p : cafe.getMenuBebidas()) if (p.getId() == idProducto) return p;
        for (Producto p : cafe.getMenuPlatillos()) if (p.getId() == idProducto) return p;
        for (Producto p : cafe.getJuegosVenta()) if (p.getId() == idProducto) return p;
        for (Producto p : cafe.getJuegosPrestamo()) if (p.getId() == idProducto) return p;
        return null;
    }
}