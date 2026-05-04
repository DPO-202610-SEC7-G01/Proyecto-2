package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

//Exceptions
import exceptions.ProductosException;

//Modelo
import modelo.producto.*;

class PlatilloTest {
       
    private static final int ID_PLATILLO_VALIDO = 001;
    private static final String NOMBRE_PLATILLO_VALIDO = "Sarandonga Enviablá";
    private static final int PRECIO_PLATILLO_VALIDO = 15000;
    
    private static final ArrayList<String> ALERGENOS_VALIDOS = new ArrayList<>(
        Arrays.asList("Camarones", "Nueces")
    );
    
    // inválidos
    private static final String ALERGENO_CON_NUMEROS = "Nueces123";
  
    private Platillo platilloValido;
    

    
    @BeforeEach
    void setUp() throws ProductosException {
        platilloValido = new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
            NOMBRE_PLATILLO_VALIDO, ALERGENOS_VALIDOS);
    }
    
    //Constructor
    
    @Test
    void testConstructorPlatilloValido() throws ProductosException {
        Platillo platillo = new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
            NOMBRE_PLATILLO_VALIDO, ALERGENOS_VALIDOS);
        
        assertNotNull(platillo);
        assertEquals(ID_PLATILLO_VALIDO, platillo.getId());
        assertEquals(PRECIO_PLATILLO_VALIDO, platillo.getPrecio());
        assertEquals(NOMBRE_PLATILLO_VALIDO, platillo.getNombre());
        assertEquals(2, platillo.getAlergeneos().size());
        assertTrue(platillo.getAlergeneos().contains("Camarones"));
        assertTrue(platillo.getAlergeneos().contains("Nueces"));

    }
    
    @Test
    void testConstructorListaAlergenosNullLanzaExcepcion() {
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, null);
        });
        
        assertTrue(exception.getMessage().contains("alergenos"));
        assertTrue(exception.getMessage().contains("La lista de alergenos no puede ser nula"));
        assertTrue(exception.getMessage().contains("ID=" + ID_PLATILLO_VALIDO));
        assertTrue(exception.getMessage().contains(NOMBRE_PLATILLO_VALIDO));
    }
    
    @Test
    void testConstructorAlergenoNullLanzaExcepcion() {
        ArrayList<String> alergenosConNull = new ArrayList<>();
        alergenosConNull.add("Camarones");
        alergenosConNull.add(null);
        
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, alergenosConNull);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("alergenos"));
        assertTrue(mensaje.contains("no puede ser nulo"));
        assertTrue(mensaje.contains("ID=" + ID_PLATILLO_VALIDO));
        assertTrue(mensaje.contains(NOMBRE_PLATILLO_VALIDO));
    }
    
    @Test
    void testConstructorAlergenoVacioLanzaExcepcion() {
        ArrayList<String> alergenosConVacio = new ArrayList<>();
        alergenosConVacio.add("Camarones");
        alergenosConVacio.add("");
        alergenosConVacio.add("Gluten");
        
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, alergenosConVacio);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("alergenos"));
        assertTrue(mensaje.contains("no puede estar vacío"));
        assertTrue(mensaje.contains("ID=" + ID_PLATILLO_VALIDO));
    }
    
    @Test
    void testConstructorAlergenoConEspaciosLanzaExcepcion() {
        ArrayList<String> alergenosConEspacios = new ArrayList<>();
        alergenosConEspacios.add("Camarones");
        alergenosConEspacios.add("   ");
        alergenosConEspacios.add("Gluten");
        
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, alergenosConEspacios);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("alergenos"));
        assertTrue(mensaje.contains("no puede estar vacío"));
    }
    
    @Test
    void testConstructorAlergenoConNumerosLanzaExcepcion() {
        ArrayList<String> alergenosConNumeros = new ArrayList<>();
        alergenosConNumeros.add("Camarones");
        alergenosConNumeros.add(ALERGENO_CON_NUMEROS);
        alergenosConNumeros.add("Gluten");
        
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, alergenosConNumeros);
        });
        
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("alergenos"));
        assertTrue(mensaje.contains("no puede contener números"));
        assertTrue(mensaje.contains(ALERGENO_CON_NUMEROS));
        assertTrue(mensaje.contains("ID=" + ID_PLATILLO_VALIDO));
        assertTrue(mensaje.contains(NOMBRE_PLATILLO_VALIDO));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Nuez123", "Camarón2", "Gluten3", "4Leche", "Huevo0"})
    void testAlergenosConNumerosLanzanExcepcion(String alergenoConNumero) {
        ArrayList<String> alergenos = new ArrayList<>();
        alergenos.add("Camarones");
        alergenos.add(alergenoConNumero);
        
        ProductosException exception = assertThrows(ProductosException.class, () -> {
            new Platillo(ID_PLATILLO_VALIDO, PRECIO_PLATILLO_VALIDO, 
                NOMBRE_PLATILLO_VALIDO, alergenos);
        });
        
        assertTrue(exception.getMessage().contains("no puede contener números"));
        assertTrue(exception.getMessage().contains(alergenoConNumero));
    }
    
    // getters y setters unicos
    
    @Test
    void testGetAlergeneos() {
        ArrayList<String> alergenos = platilloValido.getAlergeneos();
        assertEquals(2, alergenos.size());
        assertTrue(alergenos.contains("Camarones"));
        assertTrue(alergenos.contains("Nueces"));
    }
    
      
    
    @Test
    void testGetTasaImpuesto() {
        double tasaImpuesto = platilloValido.getTasaImpuesto();
        assertEquals(0.08, tasaImpuesto);
    }
    
    // HERENCIA 
    
    @Test
    void testPlatilloEsSubclaseDeProducto() {
        assertTrue(platilloValido instanceof Producto);
    }
    
    @Test
    void testPlatilloHeredaMetodosDeProducto() {
        assertEquals(ID_PLATILLO_VALIDO, platilloValido.getId());
        assertEquals(PRECIO_PLATILLO_VALIDO, platilloValido.getPrecio());
        assertEquals(NOMBRE_PLATILLO_VALIDO, platilloValido.getNombre());
    }
    
    //metodos
    
    @Test
    void testConstructorListaAlergenosVacia() throws ProductosException {
        ArrayList<String> alergenosVacios = new ArrayList<>();
        
        Platillo platillo = new Platillo(2, 20000, "Tacos", alergenosVacios);
        
        assertNotNull(platillo);
        assertTrue(platillo.getAlergeneos().isEmpty());
    }
    

    @Test
    void testConConstantesProporcionadas() throws ProductosException {
        ArrayList<String> alergenos = new ArrayList<>(Arrays.asList("Camarones", "Nueces"));
        Platillo platillo = new Platillo(1, 15000, "Paella", alergenos);
        
        assertEquals(1, platillo.getId());
        assertEquals(15000, platillo.getPrecio());
        assertEquals("Paella", platillo.getNombre());
        assertEquals(2, platillo.getAlergeneos().size());
        assertTrue(platillo.getAlergeneos().contains("Camarones"));
        assertTrue(platillo.getAlergeneos().contains("Nueces"));
    }
}