package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
//exceptions
import exceptions.*;
//modelo
import modelo.producto.*;

class JuegoDificilTest {
    
    
    private static final int ID_VALIDO = 501;
    private static final int PRECIO_VALIDO = 120000;
    private static final String NOMBRE_VALIDO = "Gloomhaven";
    private static final int ANIO_VALIDO = 2017;
    private static final String EMPRESA_VALIDA = "Cephalofair Games";
    private static final int NUM_JUGADORES_VALIDO = 4;
    private static final String RESTRICCION_VALIDA = "Adultos";
    private static final String CATEGORIA_VALIDA = "Tablero";
    private static final String INSTRUCCIONES_VALIDAS = "Preparar el escenario colocando las losetas según el libro de escenarios. " +
        "Cada jugador elige un mazo de habilidades y gestiona su agotamiento por fatiga o daño.";
    
    // Constantes para diferentes tipos de instrucciones
    private static final String INSTRUCCIONES_CORTAS = "Instrucciones cortas";
    private static final String INSTRUCCIONES_LARGAS = "Instrucciones muy largas con muchos detalles " +
        "repetidos para probar el manejo de strings largos en el sistema...";
    private static final String INSTRUCCIONES_ESPECIALES = "Instrucciones con caracteres especiales: @#$%^&*()";
    
    // Constantes para diferentes configuraciones de juego
    private static final int ID_JUEGO_CARTAS = 502;
    private static final String NOMBRE_CARTAS = "Magic: The Gathering";
    private static final int ANIO_CARTAS = 1993;
    private static final String EMPRESA_CARTAS = "Wizards of the Coast";
    private static final int NUM_JUGADORES_CARTAS = 2;
    private static final String RESTRICCION_CARTAS = "+14";
    private static final String CATEGORIA_CARTAS = "Cartas";
    private static final String INSTRUCCIONES_CARTAS = "Cada jugador tiene un mazo. Por turnos pueden conjurar hechizos, lanzar criaturas y usar habilidades especiales hasta reducir los puntos de vida del oponente a 0.";
    
    private static final int ID_JUEGO_ACCION = 503;
    private static final String NOMBRE_ACCION = "Dobble";
    private static final int ANIO_ACCION = 2009;
    private static final String EMPRESA_ACCION = "Asmodee";
    private static final int NUM_JUGADORES_ACCION = 8;
    private static final String RESTRICCION_ACCION = "-5";
    private static final String CATEGORIA_ACCION = "Acción";
    private static final String INSTRUCCIONES_ACCION = "Los jugadores compiten para encontrar el símbolo coincidente entre dos cartas lo más rápido posible. El jugador que encuentre más coincidencias gana.";
 
    
    private JuegoDificil juegoDificilValido;
        
    @BeforeEach
    void setUp() throws ProductosException {
        juegoDificilValido = new JuegoDificil(
            ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
            NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA, INSTRUCCIONES_VALIDAS
        );
    }
    
    
    @Test
    void testConstructorJuegoDificilValido() throws ProductosException {
        JuegoDificil juego = new JuegoDificil(
            ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
            NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA, INSTRUCCIONES_VALIDAS
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
        assertEquals(INSTRUCCIONES_VALIDAS, juego.getInstrucciones());
        assertEquals("nuevo", juego.getEstado());
        assertFalse(juego.estaDisponible());
    }
    
    @Test
    void testConstructorJuegoDificilCartas() throws ProductosException {
        JuegoDificil juego = new JuegoDificil(
            ID_JUEGO_CARTAS, PRECIO_VALIDO, NOMBRE_CARTAS, ANIO_CARTAS, EMPRESA_CARTAS,
            NUM_JUGADORES_CARTAS, RESTRICCION_CARTAS, CATEGORIA_CARTAS, INSTRUCCIONES_CARTAS
        );
        
        assertEquals(ID_JUEGO_CARTAS, juego.getId());
        assertEquals(NOMBRE_CARTAS, juego.getNombre());
        assertEquals(ANIO_CARTAS, juego.getAnioPublicacion());
        assertEquals(EMPRESA_CARTAS, juego.getEmpresMatriz());
        assertEquals(NUM_JUGADORES_CARTAS, juego.getNumJugadores());
        assertEquals(RESTRICCION_CARTAS, juego.getRestriccionEdad());
        assertEquals(CATEGORIA_CARTAS, juego.getCategoria());
        assertEquals(INSTRUCCIONES_CARTAS, juego.getInstrucciones());
    }
    
    @Test
    void testConstructorJuegoDificilAccion() throws ProductosException {
        JuegoDificil juego = new JuegoDificil(
            ID_JUEGO_ACCION, PRECIO_VALIDO, NOMBRE_ACCION, ANIO_ACCION, EMPRESA_ACCION,
            NUM_JUGADORES_ACCION, RESTRICCION_ACCION, CATEGORIA_ACCION, INSTRUCCIONES_ACCION
        );
        
        assertEquals(ID_JUEGO_ACCION, juego.getId());
        assertEquals(NOMBRE_ACCION, juego.getNombre());
        assertEquals(ANIO_ACCION, juego.getAnioPublicacion());
        assertEquals(NUM_JUGADORES_ACCION, juego.getNumJugadores());
        assertEquals(RESTRICCION_ACCION, juego.getRestriccionEdad());
        assertEquals(CATEGORIA_ACCION, juego.getCategoria());
        assertEquals(INSTRUCCIONES_ACCION, juego.getInstrucciones());
    }
    
    @Test
    void testConstructorInstruccionesVaciasLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            new JuegoDificil(
                ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA, ""
            );
        });
    }
    
    @Test
    void testConstructorInstruccionesNullLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            new JuegoDificil(
                ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA, null
            );
        });
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  "})
    void testInstruccionesInvalidasLanzanExcepcion(String instruccionesInvalida) {
        assertThrows(ProductosException.class, () -> {
            new JuegoDificil(
                ID_VALIDO, PRECIO_VALIDO, NOMBRE_VALIDO, ANIO_VALIDO, EMPRESA_VALIDA,
                NUM_JUGADORES_VALIDO, RESTRICCION_VALIDA, CATEGORIA_VALIDA, instruccionesInvalida
            );
        });
    }
    
    // getters y setters unicos
    
    @Test
    void testGetInstrucciones() {
        assertEquals(INSTRUCCIONES_VALIDAS, juegoDificilValido.getInstrucciones());
    }
    
    @Test
    void testSetInstruccionesValidas() throws ProductosException {
        juegoDificilValido.setInstrucciones(INSTRUCCIONES_CORTAS);
        assertEquals(INSTRUCCIONES_CORTAS, juegoDificilValido.getInstrucciones());
        
        juegoDificilValido.setInstrucciones(INSTRUCCIONES_LARGAS);
        assertEquals(INSTRUCCIONES_LARGAS, juegoDificilValido.getInstrucciones());
        
        juegoDificilValido.setInstrucciones(INSTRUCCIONES_ESPECIALES);
        assertEquals(INSTRUCCIONES_ESPECIALES, juegoDificilValido.getInstrucciones());
    }
    
    @Test
    void testSetInstruccionesVaciasLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoDificilValido.setInstrucciones("");
        });
    }
    
    @Test
    void testSetInstruccionesNullLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoDificilValido.setInstrucciones(null);
        });
    }
    
    @Test
    void testSetInstruccionesConEspaciosLanzaExcepcion() {
        assertThrows(ProductosException.class, () -> {
            juegoDificilValido.setInstrucciones("   ");
        });
    }
    
    // mettodo
    
    @Test
    void testRequiereInstructor() {
        assertTrue(juegoDificilValido.requiereInstructor());
        
        JuegoDificil juegoConDiferentesConfiguraciones;
        try {
            juegoConDiferentesConfiguraciones = new JuegoDificil(
                ID_JUEGO_CARTAS, PRECIO_VALIDO, NOMBRE_CARTAS, ANIO_CARTAS, EMPRESA_CARTAS,
                NUM_JUGADORES_CARTAS, RESTRICCION_CARTAS, CATEGORIA_CARTAS, INSTRUCCIONES_CARTAS
            );
            assertTrue(juegoConDiferentesConfiguraciones.requiereInstructor());
            
            juegoConDiferentesConfiguraciones = new JuegoDificil(
                ID_JUEGO_ACCION, PRECIO_VALIDO, NOMBRE_ACCION, ANIO_ACCION, EMPRESA_ACCION,
                NUM_JUGADORES_ACCION, RESTRICCION_ACCION, CATEGORIA_ACCION, INSTRUCCIONES_ACCION
            );
            assertTrue(juegoConDiferentesConfiguraciones.requiereInstructor());
        } catch (ProductosException e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    // HERENCIA 
    
    @Test
    void testJuegoDificilEsSubclaseDeJuego() {
        assertTrue(juegoDificilValido instanceof Juego);
        assertTrue(juegoDificilValido instanceof Producto);
    }
    
    @Test
    void testJuegoDificilHeredaMetodosDeJuego() {
        assertEquals(ID_VALIDO, juegoDificilValido.getId());
        assertEquals(PRECIO_VALIDO, juegoDificilValido.getPrecio());
        assertEquals(NOMBRE_VALIDO, juegoDificilValido.getNombre());
        assertEquals(ANIO_VALIDO, juegoDificilValido.getAnioPublicacion());
        assertEquals(EMPRESA_VALIDA, juegoDificilValido.getEmpresMatriz());
        assertEquals(NUM_JUGADORES_VALIDO, juegoDificilValido.getNumJugadores());
        assertEquals(RESTRICCION_VALIDA, juegoDificilValido.getRestriccionEdad());
        assertEquals(CATEGORIA_VALIDA, juegoDificilValido.getCategoria());
    }
    
    @Test
    void testJuegoDificilHeredaMetodosDeProducto() {
        assertEquals("nuevo", juegoDificilValido.getEstado());
        assertFalse(juegoDificilValido.estaDisponible());
        
        juegoDificilValido.setEstado("usado");
        assertEquals("usado", juegoDificilValido.getEstado());
        
        juegoDificilValido.setPrestado(true);
        assertTrue(juegoDificilValido.estaDisponible());
    }
    
    @Test
    void testGetTasaImpuesto() {
        assertEquals(0.19, juegoDificilValido.getTasaImpuesto());
    }
    

    
    @Test
    void testModificarJuegoDificilCompletamente() throws ProductosException {
        juegoDificilValido.setPrecio(150000);
        juegoDificilValido.setNumJugadores(8);
        juegoDificilValido.setRestriccionEdad("+14");
        juegoDificilValido.setCategoria("Cartas");
        juegoDificilValido.setEstado("usado");
        juegoDificilValido.setPrestado(true);
        
        juegoDificilValido.setInstrucciones("Nuevas instrucciones actualizadas");

        assertEquals(150000, juegoDificilValido.getPrecio());
        assertEquals(8, juegoDificilValido.getNumJugadores());
        assertEquals("+14", juegoDificilValido.getRestriccionEdad());
        assertEquals("Cartas", juegoDificilValido.getCategoria());
        assertEquals("usado", juegoDificilValido.getEstado());
        assertTrue(juegoDificilValido.estaDisponible());
        assertEquals("Nuevas instrucciones actualizadas", juegoDificilValido.getInstrucciones());
    }
}