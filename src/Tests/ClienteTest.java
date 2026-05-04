package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//exceptions
import exceptions.*;

//modelo
import modelo.*;
import modelo.producto.*;
import modelo.usuario.*;
class ClienteTest {

	//cafe
	private static final int CAPACIDAD = 100;
    private static final String LOGIN_ADMIN = "00alvaro"; 
    private static final String PASSWORD_ADMIN = "adminAlvaro"; 
    private static final String NOMBRE_ADMIN = "Álvaro";
	
    //cliente
	private static final int ID_VALIDO = 845;
    private static final String LOGIN_VALIDO = "alvaro845";
    private static final String PASSWORD_VALIDO = "Kat08";
    private static final String NOMBRE_VALIDO = "Álvaro";
    private static final int EDAD_VALIDA = 25;
    private static final ArrayList<String> ALERGENOS_VALIDOS = new ArrayList<>(
        Arrays.asList("Camarones", "Nueces", "Gluten")
    );
    
    // Constantes para datos inválidos
    private static final int EDAD_INVALIDA_CERO = 0;
    private static final int EDAD_INVALIDA_NEGATIVA = -5;
    private static final String ALERGENO_CON_NUMERO = "Nueces123";
    
    //juego
    private static final int ID_JUEGO = 501;
    private static final int PRECIO_JUEGO = 150000;
    private static final String NOMBRE_JUEGO = "Catan";
    private static final int ANIO_JUEGO = 1995;
    private static final String EMPRESA_JUEGO = "Devir";
    private static final int NUM_JUGADORES = 4;
    private static final String RESTRICCION_EDAD = "-5";
    private static final String CATEGORIA = "Tablero";
    
    //toreno
    private static final String NOMBRE_TORNEO = "Torneo de Catan";
    private static final int PRECIO_TORNEO = 10000;
    private static final String TIPO = "Competitivo";
    
    private Cliente clienteValido;
    private Cafe miCafe;
    private Juego juego;
    private Torneo torneo;
    
    
    @BeforeEach
    void setUp() throws UsuariosException, Exception {
       // Asumimos que esto existe antes  de hacer cosas       
        miCafe = new Cafe(CAPACIDAD);
        miCafe.cambiarAdmin(new Administrador(ID_VALIDO, LOGIN_ADMIN, PASSWORD_ADMIN, NOMBRE_ADMIN,miCafe));      
        juego = new Juego(ID_JUEGO, PRECIO_JUEGO, NOMBRE_JUEGO, ANIO_JUEGO, 
            EMPRESA_JUEGO, NUM_JUGADORES, RESTRICCION_EDAD, CATEGORIA);
        
        miCafe.agregarJuegoPrestamo(juego);
        
        torneo = new Torneo(TIPO, NOMBRE_TORNEO, juego, 4, PRECIO_TORNEO);
        miCafe.agregarTorneos(torneo);

        clienteValido = new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, ALERGENOS_VALIDOS);
    }
    
    //Constructor
    @Test
    void testConstructorValido() {
        assertDoesNotThrow(() -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, ALERGENOS_VALIDOS);
        });
    }
    
    @Test
    void testConstructorConEdadCeroLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_INVALIDA_CERO, ALERGENOS_VALIDOS);
        });
        
        assertTrue(exception.getMessage().contains("edad"));
        assertTrue(exception.getMessage().contains("La edad debe ser un número positivo"));
    }
    
    @Test
    void testConstructorConEdadNegativaLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_INVALIDA_NEGATIVA, ALERGENOS_VALIDOS);
        });
        
        assertTrue(exception.getMessage().contains("La edad debe ser un número positivo"));
    }
    
    
    @Test
    void testConstructorConAlergenosConNumerosLanzaExcepcion() {
        ArrayList<String> alergenosInvalidos = new ArrayList<>(Arrays.asList("Camarones", ALERGENO_CON_NUMERO));
        
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, alergenosInvalidos);
        });
        
        assertTrue(exception.getMessage().contains("no puede contener números"));
    }
    
    @Test
    void testConstructorConAlergenoNullLanzaExcepcion() {
        ArrayList<String> alergenosConNull = new ArrayList<>();
        alergenosConNull.add("Camarones");
        alergenosConNull.add(null);
        
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, alergenosConNull);
        });
        
        assertTrue(exception.getMessage().contains("alergenos"));
        assertTrue(exception.getMessage().contains("no puede ser nulo"));
    }
    
    @Test
    void testConstructorConAlergenoVacioLanzaExcepcion() {
        ArrayList<String> alergenosConVacio = new ArrayList<>();
        alergenosConVacio.add("Camarones");
        alergenosConVacio.add("");
        
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, alergenosConVacio);
        });
        
        assertTrue(exception.getMessage().contains("alergenos"));
        assertTrue(exception.getMessage().contains("no puede ser nulo o estar vacío"));
    }
    
    @Test
    void testConstructorConAlergenosNull() {
        assertDoesNotThrow(() -> {
            Cliente cliente = new Cliente(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, 
                NOMBRE_VALIDO, EDAD_VALIDA, null);
            assertNotNull(cliente.getAlergenos());
            assertTrue(cliente.getAlergenos().isEmpty());
        });
    }
    
    // gettters y Setters Específcios
    
    @Test
    void testGetEdad() {
        assertEquals(EDAD_VALIDA, clienteValido.getEdad());
    }
    
    @Test
    void testGetPuntosFidelidad() {
        assertEquals(0, clienteValido.getPuntosFidelidad());
    }
    
    @Test
    void testGetAlergenos() {
        ArrayList<String> alergenos = clienteValido.getAlergenos();
        assertEquals(3, alergenos.size());
        assertTrue(alergenos.contains("Camarones"));
        assertTrue(alergenos.contains("Nueces"));
        assertTrue(alergenos.contains("Gluten"));
    }
    
    @Test
    void testGetJuegosFavoritos() {
        assertNotNull(clienteValido.getJuegosFavoritos());
        assertTrue(clienteValido.getJuegosFavoritos().isEmpty());
    }
    
    @Test
    void testGetTorneosInscritos() {
        assertNotNull(clienteValido.getTorneosInscritos());
        assertTrue(clienteValido.getTorneosInscritos().isEmpty());
    }
    
    @Test
    void testGetAmigos() {
        assertFalse(clienteValido.getAmigos());
    }
    
    @Test
    void testGetPremio() {
        assertEquals("", clienteValido.getPremio());
    }
    
    @Test
    void testAgregarJuegoFavorito() {
        clienteValido.agregarJuegoFavorito(juego);
        assertEquals(1, clienteValido.getJuegosFavoritos().size());
        assertEquals(juego, clienteValido.getJuegosFavoritos().get(0));
    }
    
    @Test
    void testAgregarPremio() {
        String premio = "Placa de oro";
        clienteValido.agregarPremio(premio);
        assertEquals(premio, clienteValido.getPremio());
    }
    
    @Test
    void testSetAmigos() {
        assertFalse(clienteValido.getAmigos());
        clienteValido.setAmigos(true);
        assertTrue(clienteValido.getAmigos());
    }
    @Test
    void testNuevoAmigo() {
        assertFalse(clienteValido.getAmigos());
        clienteValido.nuevoAmigo();
        assertTrue(clienteValido.getAmigos());
    }
    
    //métodos
    @Test
    void testSumarPuntosFidelidadValido() throws UsuariosException {
        clienteValido.sumarPuntosFidelidad(10);
        assertEquals(10, clienteValido.getPuntosFidelidad());
        
        clienteValido.sumarPuntosFidelidad(5);
        assertEquals(15, clienteValido.getPuntosFidelidad());
    }
    
    @Test
    void testSumarPuntosFidelidadCeroLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            clienteValido.sumarPuntosFidelidad(0);
        });
        
        assertTrue(exception.getMessage().contains("puntosFidelidad"));
        assertTrue(exception.getMessage().contains("deben ser positivos"));
    }
    
    
    @Test
    void testSumarPuntosFidelidadNegativoLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            clienteValido.sumarPuntosFidelidad(-5);
        });
        
        assertTrue(exception.getMessage().contains("puntosFidelidad"));
        assertTrue(exception.getMessage().contains("deben ser positivos"));
    }
    
    @Test
    void testInscribirseTorneoValido() throws TorneoException {
        clienteValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        
        assertEquals(1, clienteValido.getTorneosInscritos().size());
        assertEquals(torneo, clienteValido.getTorneosInscritos().get(0));
    }
    
    @Test
    void testInscribirseTorneoNoEncontradoLanzaExcepcion() {
        TorneoException exception = assertThrows(TorneoException.class, () -> {
            clienteValido.inscribirseTorneo("Juego Inexistente", miCafe);
        });
        
        assertTrue(exception.getMessage().contains("no encontrado"));
    }
    
    @Test
    void testInscribirseMismoTorneoDosVecesLanzaExcepcion() throws TorneoException {
        clienteValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        
        TorneoException exception = assertThrows(TorneoException.class, () -> {
            clienteValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        });
        
        assertTrue(exception.getMessage().contains("ya está inscrito"));
    }
    
    @Test
    void testDesinscribirseDeTodosLosTorneos() throws TorneoException {
        clienteValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        assertEquals(1, clienteValido.getTorneosInscritos().size());
        assertEquals(1, torneo.getParticipantes().size());
        
        clienteValido.desinscribirseDeTodosLosTorneos();
        
        assertEquals(0, clienteValido.getTorneosInscritos().size());
        assertEquals(0, torneo.getParticipantes().size());
    }
    
    @Test
    void testDesinscribirseSinTorneosInscritos() {
        assertEquals(0, clienteValido.getTorneosInscritos().size());
        clienteValido.desinscribirseDeTodosLosTorneos();
        assertEquals(0, clienteValido.getTorneosInscritos().size());
    }
    
    //Herencia
    @Test
    void testClienteEsSubclaseDeUsuario() {
        assertTrue(clienteValido instanceof Usuario);
    }
    
    @Test
    void testClienteHeredaMetodosDeUsuario() {
        assertEquals(ID_VALIDO, clienteValido.getId());
        assertEquals(LOGIN_VALIDO, clienteValido.getLogin());
        assertEquals(PASSWORD_VALIDO, clienteValido.getPassword());
        assertEquals(NOMBRE_VALIDO, clienteValido.getNombre());
    }
    
    @Test
    void testAgregarMultiplesJuegosFavoritos() throws ProductosException {
        Juego juego2 = new Juego(2, 30000, "Damas", 2019, "Mattel", 2, "-5", "Tablero");
        
        clienteValido.agregarJuegoFavorito(juego);
        clienteValido.agregarJuegoFavorito(juego2);
        
        assertEquals(2, clienteValido.getJuegosFavoritos().size());
        assertTrue(clienteValido.getJuegosFavoritos().contains(juego));
        assertTrue(clienteValido.getJuegosFavoritos().contains(juego2));
    }
    
    
	

}
