package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import exceptions.ProductosException;
import modelo.producto.*;

class JuegoTest {
    

    private static final int ID_VALIDO = 501;
    private static final int PRECIO_VALIDO = 150000;
    private static final String NOMBRE_VALIDO = "Catan";
    private static final int ANIO_VALIDO = 1995;
    private static final String EMPRESA_VALIDA = "Devir";
    private static final int NUM_JUGADORES_VALIDO = 4;
    private static final String RESTRICCION_VALIDA = "-5";
    private static final String CATEGORIA_VALIDA = "Tablero";
    
    private static final int ID_JUEGO_ACCION = 701;
    private static final String NOMBRE_ACCION = "Dobble";
    private static final int ANIO_ACCION = 2009;
    private static final String EMPRESA_ACCION = "Asmodee";
    private static final int NUM_JUGADORES_ACCION = 8;
    private static final String RESTRICCION_ACCION = "Adultos";
    private static final String CATEGORIA_ACCION = "Acción";
    
    private Juego juegoValido;
    
    
    @BeforeEach
    void setUp() throws ProductosException {
        juegoValido = new Juego(
            ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
            NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA
        );
    }
    
    //constructor
    
    @Test
    void testConstructorJuegoValido() throws ProductosException {
        Juego juego = new Juego(
            ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
            NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA
        );
        
        assertNotNull(juego);
        assertEquals(ID_VALIDO, juego.getId());
        assertEquals(PRECIO_VALIDO, juego.getPrecio());
        assertEquals(NOMBRE_VALIDO, juego.getNombre());
        assertEquals(ANIO_VALIDO, juego.getAnioPublicacion());
        assertEquals(EMPRESA_VALIDA, juego.getEmpresMatriz());
        assertEquals(NUM_JUGADORES_VALIDO, juego.getNumJugadores());
        assertEquals(RESTRICCION_VALIDA, juego.getRestriccionEdad());
        assertEquals(CATEGORIA_VALIDA, juego.getCategoria());
        assertEquals("nuevo", juego.getEstado());
        assertFalse(juego.estaDisponible());
    }
    
    // exceptions del constructor
    
    @Test
    void testAnioPublicacionInvalidoLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, -1995, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA);
        });
        
        assertTrue(exception.getMessage().contains("año de publicación debe ser positivo"));
    }
    
    @Test
    void testNumJugadoresMenor1LanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                0, RESTRICCION_VALIDA, CATEGORIA_VALIDA);
        });
        
        assertTrue(exception.getMessage().contains("Número de jugadores inválido"));
    }
    
    @Test
    void testNumJugadoresMayor40LanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                50, RESTRICCION_VALIDA, CATEGORIA_VALIDA);
        });
        
        assertTrue(exception.getMessage().contains("Número de jugadores inválido"));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -5, 41, 45, 50, 100})
    void testNumJugadoresInvalidosLanzanExcepcion(int numJugadoresInvalido) {
        assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                numJugadoresInvalido, RESTRICCION_VALIDA, CATEGORIA_VALIDA);
        });
    }
    
    @Test
    void testRestriccionEdadInvalidaLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, "+18", CATEGORIA_VALIDA);
        });
        
        assertTrue(exception.getMessage().contains("Restricción de edad inválida"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"+18", "+16", "Mayores", "Niños", "Todos", "+21"})
    void testRestriccionesEdadInvalidasLanzanExcepcion(String restriccionInvalida) {
        assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, restriccionInvalida, CATEGORIA_VALIDA);
        });
    }
    
    @Test
    void testCategoriaInvalidaLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, "Videojuego");
        });
        
        assertTrue(exception.getMessage().contains("Categoría inválida"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Videojuego", "Deporte", "Estrategia", "Simulación"})
    void testCategoriasInvalidasLanzanExcepcion(String categoriaInvalida) {
        assertThrows(ProductosException.class, () -> {
            new Juego(ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, categoriaInvalida);
        });
    }
    
    // getters y setters unicos
    
    @Test
    void testGetAnioPublicacion() {
        assertEquals(ANIO_VALIDO, juegoValido.getAnioPublicacion());
    }
    
    @Test
    void testSetAnioPublicacionValido() throws ProductosException {
        juegoValido.setAnioPublicacion(2024);
        assertEquals(2024, juegoValido.getAnioPublicacion());
    }
    
    @Test
    void testSetAnioPublicacionInvalidoLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoValido.setAnioPublicacion(-2024);
        });
    }
    
    @Test
    void testGetEmpresMatriz() {
        assertEquals(EMPRESA_VALIDA, juegoValido.getEmpresMatriz());
    }
    
    @Test
    void testSetEmpresMatriz() {
        juegoValido.setEmpresMatriz("Nueva Empresa");
        assertEquals("Nueva Empresa", juegoValido.getEmpresMatriz());
    }
    
    @Test
    void testGetNumJugadores() {
        assertEquals(NUM_JUGADORES_VALIDO, juegoValido.getNumJugadores());
    }
    
    @Test
    void testSetNumJugadoresValido() throws ProductosException {
        juegoValido.setNumJugadores(8);
        assertEquals(8, juegoValido.getNumJugadores());
        
        juegoValido.setNumJugadores(1);
        assertEquals(1, juegoValido.getNumJugadores());
        
        juegoValido.setNumJugadores(40);
        assertEquals(40, juegoValido.getNumJugadores());
    }
    
    @Test
    void testSetNumJugadoresInvalidoLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoValido.setNumJugadores(50);
        });
        
        assertThrows(ProductosException.class, () -> {
            juegoValido.setNumJugadores(0);
        });
    }
    
    @Test
    void testGetRestriccionEdad() {
        assertEquals(RESTRICCION_VALIDA, juegoValido.getRestriccionEdad());
    }
    
    @Test
    void testSetRestriccionEdadValida() throws ProductosException {
        juegoValido.setRestriccionEdad("Adultos");
        assertEquals("Adultos", juegoValido.getRestriccionEdad());
        
        juegoValido.setRestriccionEdad("-5");
        assertEquals("-5", juegoValido.getRestriccionEdad());
    }
    
    @Test
    void testSetRestriccionEdadInvalidaLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoValido.setRestriccionEdad("+18");
        });
    }
    
    @Test
    void testGetCategoria() {
        assertEquals(CATEGORIA_VALIDA, juegoValido.getCategoria());
    }
    
    @Test
    void testSetCategoriaValida() throws ProductosException {
        juegoValido.setCategoria("Cartas");
        assertEquals("Cartas", juegoValido.getCategoria());
        
        juegoValido.setCategoria("Acción");
        assertEquals("Acción", juegoValido.getCategoria());
    }
    
    @Test
    void testSetCategoriaInvalidaLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoValido.setCategoria("Deporte");
        });
    }
    
    @Test
    void testGetEstado() {
        assertEquals("nuevo", juegoValido.getEstado());
    }
    
    @Test
    void testSetEstado() {
        juegoValido.setEstado("usado");
        assertEquals("usado", juegoValido.getEstado());
        
        juegoValido.setEstado("dañado");
        assertEquals("dañado", juegoValido.getEstado());
    }
    
    @Test
    void testGetPrestado() {
        assertFalse(juegoValido.estaDisponible());
    }
    
    @Test
    void testSetPrestado() {
        juegoValido.setPrestado(true);
        assertTrue(juegoValido.estaDisponible());
        
        juegoValido.setPrestado(false);
        assertFalse(juegoValido.estaDisponible());
    }
    
    // métodos
    
    @Test
    void testEsCategoriaAccion() throws ProductosException {
        assertFalse(juegoValido.esCategoriaAccion());
        
        Juego juegoAccion = new Juego(
            ID_JUEGO_ACCION, PRECIO_VALIDO, NOMBRE_ACCION, ANIO_ACCION, EMPRESA_ACCION,
            NUM_JUGADORES_ACCION, RESTRICCION_ACCION, CATEGORIA_ACCION
        );
        assertTrue(juegoAccion.esCategoriaAccion());
    }
    
    @Test
    void testEsAptoParaEdad() throws ProductosException {
        // Juego con restricción "-5" (mayor o igual a 5 años)
        assertTrue(juegoValido.esAptoParaEdad(5));
        assertTrue(juegoValido.esAptoParaEdad(10));
        assertTrue(juegoValido.esAptoParaEdad(25));
        assertFalse(juegoValido.esAptoParaEdad(4));
        assertFalse(juegoValido.esAptoParaEdad(3));
        
        // Juego con restricción "Adultos"
        Juego juegoAdultos = new Juego(
            ID_JUEGO_ACCION, PRECIO_VALIDO, NOMBRE_ACCION, ANIO_ACCION, EMPRESA_ACCION,
            NUM_JUGADORES_ACCION, "Adultos", CATEGORIA_ACCION
        );
        assertFalse(juegoAdultos.esAptoParaEdad(17));
        assertTrue(juegoAdultos.esAptoParaEdad(25));
        assertTrue(juegoAdultos.esAptoParaEdad(18));
        
    }
    
    @Test
    void testExtraerEdadMinima() {
        assertEquals(5, juegoValido.extraerEdadMinima("-5"));
        assertEquals(18, juegoValido.extraerEdadMinima("Adultos"));
        assertEquals(0, juegoValido.extraerEdadMinima("Sin restricción"));
    }
    
    @Test
    void testRequiereInstructor() {
        assertFalse(juegoValido.requiereInstructor());
    }
    
    // Herencia
    
    @Test
    void testJuegoEsSubclaseDeProducto() {
        assertTrue(juegoValido instanceof Producto);
    }
    
    @Test
    void testJuegoHeredaMetodosDeProducto() {
        assertEquals(ID_VALIDO, juegoValido.getId());
        assertEquals(PRECIO_VALIDO, juegoValido.getPrecio());
        assertEquals(NOMBRE_VALIDO, juegoValido.getNombre());
    }
    
    @Test
    void testGetTasaImpuesto() {
        double tasaImpuesto = juegoValido.getTasaImpuesto();
        assertEquals(0.19, tasaImpuesto);
    }
    
}