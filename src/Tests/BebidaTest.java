package Tests;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import exceptions.ProductosException;
import modelo.producto.*;

class BebidaTest {
    
    
    private static final int ID_BEBIDA_VALIDO = 1;
    private static final String NOMBRE_BEBIDA_VALIDO = "Burunganda";
    private static final int PRECIO_BEBIDA_VALIDO = 7999;
    private static final String TEMPERATURA_FRIA = "Fría";
    private static final String TEMPERATURA_CALIENTE = "Caliente";
    private static final boolean CON_ALCOHOL = true;
    private static final boolean SIN_ALCOHOL = false;
    
    // Constantes para pruebas de temperatura inválida
    private static final String TEMPERATURA_INVALIDA = "Tibio";
    private static final String TEMPERATURA_NUMEROS = "123";
   
    
    private Bebida bebidaFriaSinAlcohol;
    private Bebida bebidaCalienteConAlcohol;
    private Bebida bebidaPorDefecto;
    
    
    @BeforeEach
    void setUp() throws ProductosException {
        bebidaFriaSinAlcohol = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, TEMPERATURA_FRIA, SIN_ALCOHOL);
        
        bebidaCalienteConAlcohol = new Bebida(ID_BEBIDA_VALIDO + 1, PRECIO_BEBIDA_VALIDO, 
            "Café Caliente", TEMPERATURA_CALIENTE, CON_ALCOHOL);
        
        bebidaPorDefecto = new Bebida(ID_BEBIDA_VALIDO + 2, PRECIO_BEBIDA_VALIDO, 
            "Bebida Default");
    }
    
    // Temperatura
    @Test
    void testConstructorBebidaFriaSinAlcohol() throws ProductosException {
        Bebida bebida = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, TEMPERATURA_FRIA, SIN_ALCOHOL);
        
        assertNotNull(bebida);
        assertEquals(ID_BEBIDA_VALIDO, bebida.getId());
        assertEquals(PRECIO_BEBIDA_VALIDO, bebida.getPrecio());
        assertEquals(NOMBRE_BEBIDA_VALIDO, bebida.getNombre());
        assertEquals(TEMPERATURA_FRIA, bebida.getTemperatura());
        assertFalse(bebida.isTieneAlcohol());
    }
    
    @Test
    void testConstructorBebidaCalienteConAlcohol() throws ProductosException {
        Bebida bebida = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, TEMPERATURA_CALIENTE, CON_ALCOHOL);
        
        assertEquals(TEMPERATURA_CALIENTE, bebida.getTemperatura());
        assertTrue(bebida.isTieneAlcohol());
    }
    
    @Test
    void testTemperaturaFriaCaseInsensitive() throws ProductosException {
        Bebida bebida = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, "fría", SIN_ALCOHOL);
        
        assertEquals("fría", bebida.getTemperatura());
    }
    
    @Test
    void testTemperaturaCalienteCaseInsensitive() throws ProductosException {
        Bebida bebida = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, "CALIENTE", SIN_ALCOHOL);
        
        assertEquals("CALIENTE", bebida.getTemperatura());
    }
    
    // Exceptions temp
    
    @Test
    void testTemperaturaNullLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                null, SIN_ALCOHOL);
        });
        
        assertTrue(exception.getMessage().contains("temperatura"));
        assertTrue(exception.getMessage().contains("La temperatura no puede estar vacía"));
        assertTrue(exception.getMessage().contains("ID=" + ID_BEBIDA_VALIDO));
    }
    
    @Test
    void testTemperaturaVaciaLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                "", SIN_ALCOHOL);
        });
        
        assertTrue(exception.getMessage().contains("La temperatura no puede estar vacía"));
    }
    
    @Test
    void testTemperaturaInvalidaLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                TEMPERATURA_INVALIDA, SIN_ALCOHOL);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("temperatura"));
        assertTrue(mensaje.contains("debe ser 'Frío' o 'Caliente'"));
        assertTrue(mensaje.contains(TEMPERATURA_INVALIDA));
        assertTrue(mensaje.contains("ID=" + ID_BEBIDA_VALIDO));
        assertTrue(mensaje.contains(NOMBRE_BEBIDA_VALIDO));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Tibio", "Templado", "Helado", "Hervido", "Congelado", "Quemado", "Ambiente"})
    void testTemperaturasInvalidasLanzanExcepcion(String temperaturaInvalida) {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                temperaturaInvalida, SIN_ALCOHOL);
        });
        
        assertTrue(exception.getMessage().contains("debe ser 'Frío' o 'Caliente'"));
        assertTrue(exception.getMessage().contains(temperaturaInvalida));
    }
    
    @Test
    void testTemperaturaConNumerosLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                TEMPERATURA_NUMEROS, SIN_ALCOHOL);
        });
        
        assertTrue(exception.getMessage().contains("debe ser 'Frío' o 'Caliente'"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  "})
    void testTemperaturasVaciasONullLanzanExcepcion(String temperaturaInvalida) {
        assertThrows(ProductosException.class, () -> {
            new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, NOMBRE_BEBIDA_VALIDO, 
                temperaturaInvalida, SIN_ALCOHOL);
        });
    }
    
    // Constructor
    
    @Test
    void testConConstantesProporcionadas1() throws ProductosException {
        Bebida bebida = new Bebida(1, 7999, "Burunganda", "Fría", true);
        
        assertEquals(1, bebida.getId());
        assertEquals(7999, bebida.getPrecio());
        assertEquals("Burunganda", bebida.getNombre());
        assertEquals("Fría", bebida.getTemperatura());
        assertTrue(bebida.isTieneAlcohol());
    }
    
    
    // Getters y Setters unicos
    
    @Test
    void testGetTemperatura() {
        assertEquals(TEMPERATURA_FRIA, bebidaFriaSinAlcohol.getTemperatura());
        assertEquals(TEMPERATURA_CALIENTE, bebidaCalienteConAlcohol.getTemperatura());
        assertEquals("Frío", bebidaPorDefecto.getTemperatura());
    }
    
    @Test
    void testSetTemperatura() {
        bebidaFriaSinAlcohol.setTemperatura(TEMPERATURA_CALIENTE);
        assertEquals(TEMPERATURA_CALIENTE, bebidaFriaSinAlcohol.getTemperatura());
        
        bebidaFriaSinAlcohol.setTemperatura(TEMPERATURA_FRIA);
        assertEquals(TEMPERATURA_FRIA, bebidaFriaSinAlcohol.getTemperatura());
    }
    
    @Test
    void testIsTieneAlcohol() {
        assertFalse(bebidaFriaSinAlcohol.isTieneAlcohol());
        assertTrue(bebidaCalienteConAlcohol.isTieneAlcohol());
        assertFalse(bebidaPorDefecto.isTieneAlcohol());
    }
    
    @Test
    void testSetTieneAlcohol() {
        bebidaFriaSinAlcohol.setTieneAlcohol(CON_ALCOHOL);
        assertTrue(bebidaFriaSinAlcohol.isTieneAlcohol());
        
        bebidaFriaSinAlcohol.setTieneAlcohol(SIN_ALCOHOL);
        assertFalse(bebidaFriaSinAlcohol.isTieneAlcohol());
    }
    
    @Test
    void testModificarBebidaCompletamente() throws ProductosException {
        Bebida bebida = new Bebida(ID_BEBIDA_VALIDO, PRECIO_BEBIDA_VALIDO, 
            NOMBRE_BEBIDA_VALIDO, TEMPERATURA_FRIA, SIN_ALCOHOL);
        
        // Cambiar todos los atributos
        bebida.setTemperatura(TEMPERATURA_CALIENTE);
        bebida.setTieneAlcohol(CON_ALCOHOL);
        
        // Verificar cambios
        assertEquals(TEMPERATURA_CALIENTE, bebida.getTemperatura());
        assertTrue(bebida.isTieneAlcohol());
    }
    
    // 
    
    @Test
    void testGetTasaImpuesto() {
        double tasaImpuesto = bebidaFriaSinAlcohol.getTasaImpuesto();
        assertEquals(0.08, tasaImpuesto);
    }
    
    //Herencia 
    
    @Test
    void testBebidaEsSubclaseDeProducto() {
        assertTrue(bebidaFriaSinAlcohol instanceof Producto);
    }
    
    @Test
    void testBebidaHeredaMetodosDeProducto() {
        assertEquals(ID_BEBIDA_VALIDO, bebidaFriaSinAlcohol.getId());
        assertEquals(PRECIO_BEBIDA_VALIDO, bebidaFriaSinAlcohol.getPrecio());
        assertEquals(NOMBRE_BEBIDA_VALIDO, bebidaFriaSinAlcohol.getNombre());
    }
    
}