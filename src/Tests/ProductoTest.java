package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import exceptions.ProductosException;
import modelo.producto.Producto;

class ProductoTest {
        
    private static final int ID_VALIDO = 1;
    private static final int PRECIO_VALIDO = 10000;
    private static final String NOMBRE_VALIDO = "ProductoTest";
    
    private static final int ID_INVALIDO_CERO = 0;
    private static final int ID_INVALIDO_NEGATIVO = -5;
    private static final int PRECIO_INVALIDO_CERO = 0;
    private static final int PRECIO_INVALIDO_NEGATIVO = -1000;
    private static final String NOMBRE_CON_NUMEROS = "Producto123";

    //Toca poner esto pq es una clase abstracta
    class ProductoConcreto extends Producto {
        public ProductoConcreto(int id, int precio, String nombre) throws ProductosException {
            super(id, precio, nombre);
        }
        
        @Override
        public double getTasaImpuesto() {
            return 0.19; // 19% de impuesto
        }
    }
        
    private Producto productoValido;
        
    @BeforeEach
    void setUp() throws ProductosException {
        productoValido = new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO);
    }
    
    //Constructor 
    @Test
    void testConstructorValido() {
        assertDoesNotThrow(() -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO);
        });
    }
    
    @Test
    void testConstructorConIdCeroLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_INVALIDO_CERO, PRECIO_VALIDO, NOMBRE_VALIDO);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("id"));
        assertTrue(mensaje.contains("El ID del producto debe ser positivo"));
        assertTrue(mensaje.contains(String.valueOf(ID_INVALIDO_CERO)));
        assertTrue(mensaje.contains("Producto nulo") || mensaje.contains("ID="));
    }
    
    @Test
    void testConstructorConIdNegativoLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_INVALIDO_NEGATIVO, PRECIO_VALIDO, NOMBRE_VALIDO);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("id"));
        assertTrue(mensaje.contains("El ID del producto debe ser positivo"));
        assertTrue(mensaje.contains(String.valueOf(ID_INVALIDO_NEGATIVO)));
    }
    
    @Test
    void testConstructorConPrecioCeroLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_INVALIDO_CERO, NOMBRE_VALIDO);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("precio"));
        assertTrue(mensaje.contains("El precio del producto debe ser positivo"));
        assertTrue(mensaje.contains(String.valueOf(PRECIO_INVALIDO_CERO)));
    }
    
    @Test
    void testConstructorConPrecioNegativoLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_INVALIDO_NEGATIVO, NOMBRE_VALIDO);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("precio"));
        assertTrue(mensaje.contains("El precio del producto debe ser positivo"));
        assertTrue(mensaje.contains(String.valueOf(PRECIO_INVALIDO_NEGATIVO)));
    }
    
    @Test
    void testConstructorConNombreNullLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, null);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("nombre"));
        assertTrue(mensaje.contains("El nombre del producto no puede estar vacío"));
    }
    
    @Test
    void testConstructorConNombreVacioLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, "");
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("nombre"));
        assertTrue(mensaje.contains("El nombre del producto no puede estar vacío"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  ", " \n\t "})
    void testConstructorConNombresInvalidosLanzanExcepcion(String nombreInvalido) {
        assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, nombreInvalido);
        });
    }
    
    @Test
    void testConstructorConNombreConNumerosLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, NOMBRE_CON_NUMEROS);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("nombre"));
        assertTrue(mensaje.contains("El nombre del producto no puede contener números"));
        assertTrue(mensaje.contains(NOMBRE_CON_NUMEROS));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Producto123", "Test123", "123Producto", "123", "Producto 123"})
    void testConstructorConNombresConNumerosLanzanExcepcion(String nombreConNumeros) {
        assertThrows(ProductosException.class, () -> {
            new ProductoConcreto(ID_VALIDO, PRECIO_VALIDO, nombreConNumeros);
        });
    }
    
    //getters y setters propios
    
    @Test
    void testGetId() {
        assertEquals(ID_VALIDO, productoValido.getId());
    }
    
    @Test
    void testGetPrecio() {
        assertEquals(PRECIO_VALIDO, productoValido.getPrecio());
    }
    
    @Test
    void testGetNombre() {
        assertEquals(NOMBRE_VALIDO, productoValido.getNombre());
    }
   
    
    @Test
    void testSetIdValido() throws ProductosException {
        productoValido.setId(10);
        assertEquals(10, productoValido.getId());
    }
    
    @Test
    void testSetIdCeroLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setId(0);
        });
        
        assertTrue(exception.getMessage().contains("El ID del producto debe ser positivo"));
    }
    
    @Test
    void testSetIdNegativoLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setId(-10);
        });
        
        assertTrue(exception.getMessage().contains("El ID del producto debe ser positivo"));
    }
    
    @Test
    void testSetPrecioValido() throws ProductosException {
        productoValido.setPrecio(20000);
        assertEquals(20000, productoValido.getPrecio());
    }
    
    @Test
    void testSetPrecioCeroLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setPrecio(0);
        });
        
        assertTrue(exception.getMessage().contains("El precio del producto debe ser positivo"));
    }
    
    @Test
    void testSetPrecioNegativoLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setPrecio(-5000);
        });
        
        assertTrue(exception.getMessage().contains("El precio del producto debe ser positivo"));
    }
    
    @Test
    void testSetNombreValido() throws ProductosException {
        productoValido.setNombre("NuevoNombre");
        assertEquals("NuevoNombre", productoValido.getNombre());
    }
    
    @Test
    void testSetNombreConEspaciosPermitidos() throws ProductosException {
        productoValido.setNombre("Nombre Con Espacios");
        assertEquals("Nombre Con Espacios", productoValido.getNombre());
    }
    
    @Test
    void testSetNombreNullLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setNombre(null);
        });
        
        assertTrue(exception.getMessage().contains("El nombre del producto no puede estar vacío"));
    }
    
    @Test
    void testSetNombreVacioLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setNombre("");
        });
        
        assertTrue(exception.getMessage().contains("El nombre del producto no puede estar vacío"));
    }
    
    @Test
    void testSetNombreConNumerosLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            productoValido.setNombre(NOMBRE_CON_NUMEROS);
        });
        
        assertTrue(exception.getMessage().contains("El nombre del producto no puede contener números"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Test123", "123Test", "Test 123", "123"})
    void testSetNombresConNumerosLanzanExcepcion(String nombreConNumeros) {
        assertThrows(ProductosException.class, () -> {
            productoValido.setNombre(nombreConNumeros);
        });

    }
   
    
    //Métodos
    
    @Test
    void testGetTasaImpuesto() {
        double tasaImpuesto = productoValido.getTasaImpuesto();
        assertEquals(0.19, tasaImpuesto);
    }
    
    
    @Test
    void testCalcularPrecioFinal() {
        double precioFinal = productoValido.calcularPrecioFinal();
        // 10000 * (1 + 0.19) = 11900
        assertEquals(11900.0, precioFinal, 0.01);
    }
    
    @Test
    void testCalcularPrecioFinalConPrecioDiferente() throws ProductosException {
        Producto producto = new ProductoConcreto(2, 5000, "ProductoBarato");
        double precioFinal = producto.calcularPrecioFinal();
        assertEquals(5950.0, precioFinal, 0.01);
    }
    
    @Test
    void testCalcularPrecioFinalConPrecioGrande() throws ProductosException {
        Producto producto = new ProductoConcreto(3, 1000000, "ProductoCaro");
        double precioFinal = producto.calcularPrecioFinal();
        // 1000000 * (1 + 0.19) = 1190000
        assertEquals(1190000.0, precioFinal, 0.01);
    }
    

    
    @Test
    void testValoresLimites() throws ProductosException {
        // Valores límite válidos
        Producto productoMinimo = new ProductoConcreto(1, 1, "Minimo");
        assertEquals(1, productoMinimo.getId());
        assertEquals(1, productoMinimo.getPrecio());
        
        Producto productoMaximo = new ProductoConcreto(Integer.MAX_VALUE, Integer.MAX_VALUE, "Maximo");
        assertEquals(Integer.MAX_VALUE, productoMaximo.getId());
        assertEquals(Integer.MAX_VALUE, productoMaximo.getPrecio());
    }
    
    
    
   
}