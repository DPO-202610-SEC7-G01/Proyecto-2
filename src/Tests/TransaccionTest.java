package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.UsuariosException;
import modelo.Cafe;
import modelo.Transaccion;
import modelo.producto.*;
import modelo.usuario.*;

class TransaccionIntegrationTest {

    
    private static final int CAPACIDAD = 100;
    private static final String LOGIN_ADMIN = "00alvaro";
    private static final String PASSWORD_ADMIN = "adminAlvaro";
    private static final String NOMBRE_ADMIN = "Álvaro";
    
    private static final int ID_TRANSACCION = 1;
    private static final int ID_CLIENTE = 10;
    private static final String LOGIN_CLIENTE = "cliente123";
    private static final String PASSWORD_CLIENTE = "pass123";
    private static final String NOMBRE_CLIENTE = "Cliente Test";
    private static final int EDAD_CLIENTE = 25;
    private static final ArrayList<String> ALERGENOS_VACIOS = new ArrayList<>();
    
    private static final int ID_EMPLEADO = 20;
    private static final String LOGIN_EMPLEADO = "empleado123";
    private static final String PASSWORD_EMPLEADO = "pass123";
    private static final String NOMBRE_EMPLEADO = "Empleado Test";
    
    private static final int ID_PRODUCTO = 1;
    private static final String NOMBRE_PRODUCTO = "Producto Test";
    private static final int PRECIO_PRODUCTO = 10000;
    
    private static final String TEMPERATURA_FRIA = "Frío";
       
    private Cafe miCafe;
    private Calendar fecha;
    private Cliente cliente;
    private Empleado empleado;
    private Producto producto;
    private Bebida bebida;
    private Platillo platillo;
    private Juego juego;
    private List<Producto> productos;
    private Transaccion transaccion;
    
    // 
    
    @BeforeEach
    void setUp() throws Exception {
        miCafe = new Cafe(CAPACIDAD);
        miCafe.cambiarAdmin(new Administrador(999, LOGIN_ADMIN, PASSWORD_ADMIN, NOMBRE_ADMIN, miCafe));
        
        fecha = Calendar.getInstance();
        
        cliente = new Cliente(ID_CLIENTE, LOGIN_CLIENTE, PASSWORD_CLIENTE,
                NOMBRE_CLIENTE, EDAD_CLIENTE, ALERGENOS_VACIOS);
        empleado = new Empleado(ID_EMPLEADO, LOGIN_EMPLEADO, PASSWORD_EMPLEADO, NOMBRE_EMPLEADO);
        
        miCafe.agregarUsuario(cliente);
        miCafe.agregarEmpleado(empleado);
        
        producto = new ProductoTestImpl(ID_PRODUCTO, PRECIO_PRODUCTO, NOMBRE_PRODUCTO);
        bebida = new Bebida(ID_PRODUCTO + 1, 5000, "Café", TEMPERATURA_FRIA, false);
        
        ArrayList<String> alergenos = new ArrayList<>();
        alergenos.add("Gluten");
        platillo = new Platillo(ID_PRODUCTO + 2, 15000, "Paella", alergenos);
        
        juego = new Juego(ID_PRODUCTO + 3, 50000, "Ajedrez", 2020, "Hasbro", 4, "-5", "Tablero");
        
        productos = new ArrayList<>();
        productos.add(producto);
        productos.add(bebida);
         transaccion = new Transaccion(ID_TRANSACCION, fecha, productos, cliente, false);
    }
    
    //toca usar esto para crear un producto x y usarlo en un futuro
    class ProductoTestImpl extends Producto {
        public ProductoTestImpl(int id, int precio, String nombre) throws Exception {
            super(id, precio, nombre);
        }
        
        @Override
        public double getTasaImpuesto() {
            return 0.19;
        }
    }
    
    // Integración
    
    @Test
    void testIntegracionTransaccionCompletaCliente() throws UsuariosException {
        assertNotNull(transaccion);
        assertEquals(2, transaccion.getProductos().size());
        assertEquals(cliente, transaccion.getCliente_final());
        int total = transaccion.calcularTotal();
        assertEquals(17300, total);
         assertTrue(cliente.getPuntosFidelidad() > 0);
        assertEquals(173, cliente.getPuntosFidelidad()); 
    }
    
    @Test
    void testIntegracionTransaccionCompletaEmpleado() throws UsuariosException {
        Transaccion transaccionEmpleado = new Transaccion(ID_TRANSACCION + 1, fecha, productos, empleado, false);
        
        int total = transaccionEmpleado.calcularTotal();
        assertEquals(13840, total);
        assertEquals(173, empleado.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionConAmigoEmpleado() throws UsuariosException {
        Transaccion transaccionAmigo = new Transaccion(ID_TRANSACCION + 2, fecha, productos, cliente, true);
        
        int total = transaccionAmigo.calcularTotal();
        assertEquals(15570, total);
        assertEquals(173, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionConPremio50() throws UsuariosException {
        cliente.agregarPremio("Bono de descuento 50%");
        
        int total = transaccion.calcularTotal();
        assertEquals(8650, total);
        assertEquals("", cliente.getPremio());
        assertEquals(173, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionMultiplesProductos() throws UsuariosException {
        List<Producto> productosMultiples = new ArrayList<>();
        productosMultiples.add(producto);    
        productosMultiples.add(bebida);      
        productosMultiples.add(platillo);    
        productosMultiples.add(juego);     
        
        Transaccion transaccionMultiple = new Transaccion(ID_TRANSACCION + 3, fecha, productosMultiples, cliente, false);
        
        int total = transaccionMultiple.calcularTotal();
        assertEquals(93000, total);
        assertEquals(930, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionYRegistroEnCafe() throws UsuariosException {
        int transaccionesIniciales = miCafe.getHistorialTransaccion() != null ? miCafe.getHistorialTransaccion().size() : 0;
        
        transaccion.calcularTotal();
        
        assertNotNull(miCafe.getHistorialTransaccion());
        assertTrue(miCafe.getHistorialTransaccion().size() > transaccionesIniciales);
        assertTrue(miCafe.getHistorialTransaccion().contains(transaccion));
    }
    
    @Test
    void testIntegracionTransaccionConAgregarProductoPosterior() throws UsuariosException {
        assertEquals(2, transaccion.getProductos().size());
        
        transaccion.agregarProducto(platillo);
        assertEquals(3, transaccion.getProductos().size());
        
        int total = transaccion.calcularTotal();
        assertEquals(33500, total);
        assertEquals(335, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionGenerarTransaccionDesdeMetodo() throws UsuariosException {
        ArrayList<Producto> nuevosProductos = new ArrayList<>();
        nuevosProductos.add(platillo);
        nuevosProductos.add(juego);
        
        Transaccion nuevaTransaccion = transaccion.generarTransaccion(nuevosProductos, 100, cliente);
        
        assertNotNull(nuevaTransaccion);
        assertEquals(100, nuevaTransaccion.getId());
        assertEquals(2, nuevaTransaccion.getProductos().size());
        assertEquals(cliente, nuevaTransaccion.getCliente_final());
        
        int total = nuevaTransaccion.calcularTotal();
        assertEquals(75700, total);
        assertEquals(757, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionProductosConDiferentesImpuestos() throws UsuariosException {
       
        List<Producto> productosMixtos = new ArrayList<>();
        productosMixtos.add(producto);    
        productosMixtos.add(bebida);     
        productosMixtos.add(platillo);    
        productosMixtos.add(juego);       
        
        Transaccion transaccionMixta = new Transaccion(ID_TRANSACCION + 4, fecha, productosMixtos, cliente, false);
        
        int total = transaccionMixta.calcularTotal();
        assertEquals(93000, total);
    }
    
    @Test
    void testIntegracionTransaccionConVariosTiposDeUsuario() throws UsuariosException {
        Transaccion transCliente = new Transaccion(ID_TRANSACCION + 5, fecha, productos, cliente, false);
        int totalCliente = transCliente.calcularTotal();
        assertEquals(17300, totalCliente);
        
        Transaccion transEmpleado = new Transaccion(ID_TRANSACCION + 6, fecha, productos, empleado, false);
        int totalEmpleado = transEmpleado.calcularTotal();
        assertEquals(13840, totalEmpleado);
        
        Transaccion transAmigo = new Transaccion(ID_TRANSACCION + 7, fecha, productos, cliente, true);
        int totalAmigo = transAmigo.calcularTotal();
        assertEquals(15570, totalAmigo);
        
        cliente.agregarPremio("Bono de descuento 50%");
        Transaccion transPremio = new Transaccion(ID_TRANSACCION + 8, fecha, productos, cliente, true);
        int totalPremio = transPremio.calcularTotal();
        assertEquals(8650, totalPremio);
    }
    
    @Test
    void testIntegracionTransaccionConCafeMultiplesTransacciones() throws UsuariosException {
        Transaccion t1 = new Transaccion(1, fecha, productos, cliente, false);
        Transaccion t2 = new Transaccion(2, fecha, productos, empleado, false);
        Transaccion t3 = new Transaccion(3, fecha, productos, cliente, true);
        
        t1.calcularTotal();
        t2.calcularTotal();
        t3.calcularTotal();
        
        assertTrue(miCafe.getHistorialTransaccion().contains(t1));
        assertTrue(miCafe.getHistorialTransaccion().contains(t2));
        assertTrue(miCafe.getHistorialTransaccion().contains(t3));
        
        assertEquals(173, cliente.getPuntosFidelidad()); 
    }
    
    @Test
    void testIntegracionFacturaSinProductos() throws UsuariosException {
        List<Producto> sinProductos = new ArrayList<>();
        Transaccion transaccionVacia = new Transaccion(ID_TRANSACCION + 9, fecha, sinProductos, cliente, false);
        
        int total = transaccionVacia.calcularTotal();
        assertEquals(0, total);
        
        assertEquals(0, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionFacturaSinClientesLanzaExcepcion() {
        Transaccion transaccionSinCliente = new Transaccion(ID_TRANSACCION + 10, fecha, productos, null, false);
        
        assertThrows(UsuariosException.class, () -> {
            transaccionSinCliente.calcularTotal();
        });
    }
    
    //
    
    @Test
    void testIntegracionTransaccionYHistorialCliente() throws UsuariosException {
        int puntosIniciales = cliente.getPuntosFidelidad();
        
        transaccion.calcularTotal();
        
        int puntosDespues = cliente.getPuntosFidelidad();
        assertTrue(puntosDespues > puntosIniciales);
        assertEquals(173, puntosDespues);
        List<Producto> masProductos = new ArrayList<>();
        masProductos.add(platillo);
        Transaccion t2 = new Transaccion(2, fecha, masProductos, cliente, false);
        t2.calcularTotal();
        assertEquals(173 + 162, cliente.getPuntosFidelidad());
    }
    
    @Test
    void testIntegracionTransaccionConDiferentesFechas() throws UsuariosException {
        Calendar fechaPasada = Calendar.getInstance();
        fechaPasada.set(2024, 0, 1);
        
        Calendar fechaFutura = Calendar.getInstance();
        fechaFutura.set(2024, 11, 31);
        
        Transaccion t1 = new Transaccion(1, fechaPasada, productos, cliente, false);
        Transaccion t2 = new Transaccion(2, fechaFutura, productos, cliente, false);
        
        t1.calcularTotal();
        t2.calcularTotal();
        
        assertEquals(fechaPasada, t1.getFecha());
        assertEquals(fechaFutura, t2.getFecha());
        
        assertTrue(miCafe.getHistorialTransaccion().contains(t1));
        assertTrue(miCafe.getHistorialTransaccion().contains(t2));
    }
}