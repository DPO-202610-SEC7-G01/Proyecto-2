package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.InvalidCredentialsException;
import exceptions.ProductosException;
import exceptions.UsuariosException;
import modelo.usuario.Cocinero;
import modelo.producto.Platillo;
import modelo.producto.Bebida;

class CocineroTest {
    
	// Constantes para pruebas correctas
    private static final int ID_VALIDO = 845;
    private static final String LOGIN_VALIDO = "alvaro845";
    private static final String PASSWORD_VALIDO = "Kat08";
    private static final String NOMBRE_VALIDO = "Álvaro";
	
    private static final int ID_PLATILLO_VALIDO = 001;
    private static final String NOMBRE_PLATILLO_VALIDO = "Sarandonga Enviablá";
    private static final int PRECIO_PLATILLO_VALIDO = 15000;
    private static final ArrayList<String> ALERGENOS_VALIDOS = new ArrayList<>(
        Arrays.asList("Camarones", "Nueces")
    );
    
    private static final int ID_BEBIDA_VALIDO = 1;
    private static final String NOMBRE_BEBIDA_VALIDO = "Burunganda";
    private static final int PRECIO_BEBIDA_VALIDO = 7999;
    private static final String TEMPERATURA_FRIA = "Fría";
    private static final boolean ALCOHOL= true;
  
	
    private Cocinero cocinero;
    private Platillo platilloValido;
    private Bebida bebidaValida;
    
    
    @BeforeEach
    void setUp() throws InvalidCredentialsException, ProductosException, UsuariosException {
        cocinero = new Cocinero(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, NOMBRE_VALIDO);
        platilloValido = new Platillo(ID_PLATILLO_VALIDO,PRECIO_PLATILLO_VALIDO,NOMBRE_PLATILLO_VALIDO,ALERGENOS_VALIDOS);
        bebidaValida = new Bebida(ID_BEBIDA_VALIDO,PRECIO_BEBIDA_VALIDO,NOMBRE_BEBIDA_VALIDO,TEMPERATURA_FRIA,ALCOHOL);
    }
    
    //Platillo
    @Test
    void testAprenderPlatilloValido() {
        cocinero.aprenderPlatillo(platilloValido);
        assertEquals(1, cocinero.getPlatillosConocidos().size());
        assertEquals(platilloValido, cocinero.getPlatillosConocidos().get(0));
    }
    
    @Test
    void testAprenderPlatilloNullLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cocinero.aprenderPlatillo(null);
        });
        assertEquals("El platillo no puede ser nulo", exception.getMessage());
    }
    
    @Test
    void testAprenderMultiplesPlatillos() throws ProductosException {
        Platillo platillo2 = new Platillo(2, 8999, "Tacos", new ArrayList<>(Arrays.asList("Tortilla", "Carne")));
        Platillo platillo3 = new Platillo(3, 6500, "Ensalada", new ArrayList<>(Arrays.asList("Lechuga", "Tomate")));
        
        cocinero.aprenderPlatillo(platilloValido);
        cocinero.aprenderPlatillo(platillo2);
        cocinero.aprenderPlatillo(platillo3);
        
        assertEquals(3, cocinero.getPlatillosConocidos().size());
        assertTrue(cocinero.getPlatillosConocidos().contains(platilloValido));
        assertTrue(cocinero.getPlatillosConocidos().contains(platillo2));
        assertTrue(cocinero.getPlatillosConocidos().contains(platillo3));
    }
    
    //Bebida
    
    @Test
    void testAprenderBebidaValida() {
        cocinero.aprenderBebida(bebidaValida);
        assertEquals(1, cocinero.getBebidasConocidas().size());
        assertEquals(bebidaValida, cocinero.getBebidasConocidas().get(0));
    }
    
    @Test
    void testAprenderBebidaNullLanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cocinero.aprenderBebida(null);
        });
        assertEquals("La bebida no puede ser nula", exception.getMessage());
    }
    
    @Test
    void testAprenderMultiplesBebidas() throws ProductosException {
        Bebida bebida2 = new Bebida(2, 2500, "Café", "Caliente", false);
        Bebida bebida3 = new Bebida(3, 3000, "Jugo", "Fría", false);
        
        cocinero.aprenderBebida(bebidaValida);
        cocinero.aprenderBebida(bebida2);
        cocinero.aprenderBebida(bebida3);
        
        assertEquals(3, cocinero.getBebidasConocidas().size());
        assertTrue(cocinero.getBebidasConocidas().contains(bebidaValida));
        assertTrue(cocinero.getBebidasConocidas().contains(bebida2));
        assertTrue(cocinero.getBebidasConocidas().contains(bebida3));
    }
    
    // Herencia
    
    @Test
    void testCocineroEsInstanciaDeUsuario() {
        assertTrue(cocinero instanceof modelo.usuario.Usuario);
    }
    
  
}