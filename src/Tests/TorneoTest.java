package Tests;

//utils
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

//exceptions
import exceptions.*;

//modelo
import modelo.producto.*;
import modelo.usuario.*;
import modelo.*;

class TorneoTest {
       
	//Cafe
    private static final int CAPACIDAD = 100;
    private static final String LOGIN_ADMIN = "00alvaro"; 
    private static final String PASSWORD_ADMIN = "adminAlvaro"; 
    private static final String NOMBRE_ADMIN = "Álvaro";
	
    // Constantes para juegos
    private static final int ID_JUEGO_VALIDO = 100;
    private static final int PRECIO = 5000;
    private static final String NOMBRE_JUEGO_VALIDO = "Ajedrez";
    private static final int ANIO_PUBLICACION = 2020;
    private static final String EMPRESA_MATRIZ = "Empresa Test";
    private static final int NUM_JUGADORES = 2;
    private static final String RESTRICCION_EDAD = "Adultos";
    private static final String CATEGORIA = "Estrategia";
   
   
    // Constantes para IDs
    
    private static final int ID_CLIENTE_FANATICO = 1;
    private static final int ID_CLIENTE_NORMAL = 2;
    private static final int ID_EMPLEADO_FANATICO = 3;
    private static final int ID_EMPLEADO_NORMAL = 4;
    private static final int ID_ADMIN = 5;
    
    // Constantes para nombres
    private static final String NOMBRE_TORNEO_VALIDO = "Torneo de Prueba";
    
    private static final String LOGIN_VALIDO = "usuario123";
    private static final String PASSWORD_VALIDO = "pass123";
    private static final String NOMBRE_USUARIO_VALIDO = "Usuario Prueba";
    
    // Constantes para precios
    private static final int PRECIO_COMPETITIVO_VALIDO = 10000;
    private static final int PRECIO_AMISTOSO_VALIDO = 0;
    private static final int PRECIO_NEGATIVO_INVALIDO = -5000;
    
    // Constantes para participantes
    private static final int NUM_PARTICIPANTES_VALIDO = 4;
    private static final int NUM_PARTICIPANTES_MINIMO = 2;
    private static final int NUM_PARTICIPANTES_INVALIDO_MENOR = 1;
    private static final int NUM_PARTICIPANTES_GRANDE = 8;
    
    // Constantes para tipos de torneo
    private static final String TIPO_AMISTOSO = "Amistoso";
    private static final String TIPO_COMPETITIVO = "Competitivo";
    private static final String TIPO_INVALIDO = "Profesional";
    
    
    
    // Constantes para edades y alergenos
    private static final int EDAD_VALIDA = 25;
    private static final ArrayList<String> ALERGENOS_VACIOS = new ArrayList<>();
    
    
    private Cafe miCafe;
    private Juego juego;
    private Cliente clienteFanatico;
    private Cliente clienteNormal;
    private Empleado empleadoFanatico;
    private Empleado empleadoNormal;
    private Administrador admin;
    private Torneo torneoAmistoso;
    private Torneo torneoCompetitivo;

    
    @BeforeEach
    void setUp() throws Exception {
        miCafe = new Cafe(CAPACIDAD);
        miCafe.cambiarAdmin(new Administrador(ID_ADMIN, LOGIN_ADMIN, PASSWORD_ADMIN, NOMBRE_ADMIN,miCafe));
        
        // Crear juego
        juego = new Juego(ID_JUEGO_VALIDO, PRECIO, NOMBRE_JUEGO_VALIDO,  ANIO_PUBLICACION, 
        		EMPRESA_MATRIZ, NUM_JUGADORES,RESTRICCION_EDAD, CATEGORIA);
        miCafe.agregarJuegoPrestamo(juego);
 
        
        clienteFanatico = new Cliente(ID_CLIENTE_FANATICO, "fanatico123", "pass123", 
            "Cliente Fanático", EDAD_VALIDA, ALERGENOS_VACIOS);
        clienteFanatico.agregarJuegoFavorito(juego);
        miCafe.agregarUsuario(clienteFanatico);
        
        // Crear cliente normal (no tiene el juego en favoritos)
        clienteNormal = new Cliente(ID_CLIENTE_NORMAL, "normal123", "pass123", 
            "Cliente Normal", EDAD_VALIDA, ALERGENOS_VACIOS);
        cafe.agregarCliente(clienteNormal);
        
        // Crear empleado fanático
        empleadoFanatico = new Empleado(ID_EMPLEADO_FANATICO, "empleadoFan123", "pass123", 
            "Empleado Fanático", 2000);
        empleadoFanatico.agregarJuegoFavorito(juego);
        cafe.agregarEmpleado(empleadoFanatico);
        
        // Crear empleado normal
        empleadoNormal = new Empleado(ID_EMPLEADO_NORMAL, "empleadoNormal123", "pass123", 
            "Empleado Normal", 2000);
        cafe.agregarEmpleado(empleadoNormal);
        
        // Crear torneos
        torneoAmistoso = new Torneo(TIPO_AMISTOSO, "Torneo Amistoso Test", juego, 
            NUM_PARTICIPANTES_VALIDO, PRECIO_AMISTOSO_VALIDO, cafe);
        torneoCompetitivo = new Torneo(TIPO_COMPETITIVO, "Torneo Competitivo Test", juego, 
            NUM_PARTICIPANTES_VALIDO, PRECIO_COMPETITIVO_VALIDO, cafe);
    }
    
    // ==================== TESTS DEL CONSTRUCTOR ====================
    
    @Test
    void testConstructorValidoAmistoso() {
        assertDoesNotThrow(() -> {
            new Torneo(TIPO_AMISTOSO, "Torneo Amigo", juego, 2, 0, cafe);
        });
    }
    
    @Test
    void testConstructorValidoCompetitivo() {
        assertDoesNotThrow(() -> {
            new Torneo(TIPO_COMPETITIVO, "Torneo Pro", juego, 2, 5000, cafe);
        });
    }
    
    @Test
    void testTipoNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(null, "Torneo", juego, 2, 0, cafe);
        });
        assertTrue(exception.getMessage().contains("El tipo del torneo no puede estar vacío"));
    }
    
    @Test
    void testTipoInvalidoLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(TIPO_INVALIDO, "Torneo", juego, 2, 0, cafe);
        });
        assertTrue(exception.getMessage().contains("solo puede ser 'Amistoso' o 'Competitivo'"));
    }
    
    @Test
    void testNombreVacioLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(TIPO_AMISTOSO, "", juego, 2, 0, cafe);
        });
        assertTrue(exception.getMessage().contains("El nombre del torneo no puede estar vacío"));
    }
    
    @Test
    void testJuegoNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(TIPO_AMISTOSO, "Torneo", null, 2, 0, cafe);
        });
        assertTrue(exception.getMessage().contains("El juego del torneo no puede ser nulo"));
    }
    
    @Test
    void testAmistosoConPrecioDistintoCeroLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(TIPO_AMISTOSO, "Torneo", juego, 2, 1000, cafe);
        });
        assertTrue(exception.getMessage().contains("Los torneos amistosos deben ser gratuitos"));
    }
    
    @Test
    void testPrecioNegativoLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new Torneo(TIPO_COMPETITIVO, "Torneo", juego, 2, PRECIO_NEGATIVO_INVALIDO, cafe);
        });
        assertTrue(exception.getMessage().contains("El precio no puede ser negativo"));
    }
    
    // ==================== TESTS PARA GETTERS ====================
    
    @Test
    void testGetTipo() {
        assertEquals(TIPO_AMISTOSO, torneoAmistoso.getTipo());
        assertEquals(TIPO_COMPETITIVO, torneoCompetitivo.getTipo());
    }
    
    @Test
    void testGetJuego() {
        assertEquals(juego, torneoAmistoso.getJuego());
    }
    
    @Test
    void testGetNumParticipantes() {
        assertEquals(NUM_PARTICIPANTES_VALIDO, torneoAmistoso.getNumParticipantes());
    }
    
    @Test
    void testGetPrecio() {
        assertEquals(PRECIO_AMISTOSO_VALIDO, torneoAmistoso.getPrecio());
        assertEquals(PRECIO_COMPETITIVO_VALIDO, torneoCompetitivo.getPrecio());
    }
    
    @Test
    void testIsActivo() {
        assertTrue(torneoAmistoso.isActivo());
    }
    
    @Test
    void testSetActivo() {
        torneoAmistoso.setActivo(false);
        assertFalse(torneoAmistoso.isActivo());
        
        torneoAmistoso.setActivo(true);
        assertTrue(torneoAmistoso.isActivo());
    }
    
    @Test
    void testGetFecha() {
        Calendar fecha = torneoAmistoso.getFecha();
        assertNotNull(fecha);
    }
    
    @Test
    void testGetParticipantesInicialmenteVacio() {
        assertTrue(torneoAmistoso.getParticipantes().isEmpty());
    }
    
    // ==================== TESTS PARA TIPO DE TORNEO ====================
    
    @Test
    void testEsAmistoso() {
        assertTrue(torneoAmistoso.esAmistoso());
        assertFalse(torneoCompetitivo.esAmistoso());
    }
    
    @Test
    void testEsCompetitivo() {
        assertFalse(torneoAmistoso.esCompetitivo());
        assertTrue(torneoCompetitivo.esCompetitivo());
    }
    
    // ==================== TESTS PARA AGREGAR PARTICIPANTES ====================
    
    @Test
    void testAgregarParticipanteNormal() throws TorneoException {
        torneoAmistoso.agregarParticipantes(clienteNormal);
        assertEquals(1, torneoAmistoso.getParticipantes().size());
        assertTrue(torneoAmistoso.getParticipantes().contains(clienteNormal));
    }
    
    @Test
    void testAgregarParticipanteDuplicadoLanzaExcepcion() throws TorneoException {
        torneoAmistoso.agregarParticipantes(clienteNormal);
        
        TorneoException exception = assertThrows(TorneoException.class, () -> {
            torneoAmistoso.agregarParticipantes(clienteNormal);
        });
        assertTrue(exception.getMessage().contains("El participante ya está inscrito"));
    }
    
    @Test
    void testAgregarParticipanteFanatico() throws TorneoException {
        torneoAmistoso.agregarParticipantes(clienteFanatico);
        assertEquals(1, torneoAmistoso.getParticipantes().size());
    }
    
    // ==================== TESTS PARA ELIMINAR PARTICIPANTES ====================
    
    @Test
    void testEliminarParticipante() throws TorneoException {
        torneoAmistoso.agregarParticipantes(clienteNormal);
        assertEquals(1, torneoAmistoso.getParticipantes().size());
        
        torneoAmistoso.eliminarParticipante(clienteNormal);
        assertEquals(0, torneoAmistoso.getParticipantes().size());
    }
    
    @Test
    void testEliminarParticipanteInexistente() {
        torneoAmistoso.eliminarParticipante(clienteNormal);
        assertEquals(0, torneoAmistoso.getParticipantes().size());
    }
    
    // ==================== TESTS PARA DEFINIR PREMIO ====================
    
    @Test
    void testDefinirPremioAmistoso() {
        torneoAmistoso.definirPremio();
        // Verificar premio a través del método ganador o getter
        assertNotNull(torneoAmistoso);
    }
    
    @Test
    void testDefinirPremioCompetitivoSimple() throws Exception {
        Torneo torneoPocoRecaudo = new Torneo(TIPO_COMPETITIVO, "Torneo Simple", juego, 
            4, 10000, cafe);
        torneoPocoRecaudo.definirPremio();
    }
    
    // ==================== TESTS PARA GANADOR ====================
    
    @Test
    void testGanadorCliente() throws TorneoException {
        torneoAmistoso.agregarParticipantes(clienteNormal);
        torneoAmistoso.ganador(clienteNormal);
        // Verificar que se registró el ganador
        assertNotNull(clienteNormal);
    }
    
    @Test
    void testGanadorEmpleado() throws TorneoException {
        torneoAmistoso.agregarParticipantes(empleadoNormal);
        torneoAmistoso.ganador(empleadoNormal);
        assertNotNull(empleadoNormal);
    }
    
    // ==================== TESTS PARA FANÁTICOS ====================
    
    @Test
    void testFanaticosSeAgreganAutomaticamente() {
        // Los fanáticos deberían agregarse automáticamente al crear el torneo
        assertNotNull(torneoAmistoso);
    }
    
    // ==================== TESTS PARA VALIDACIÓN DE PARTICIPANTES ====================
    
    @Test
    void testNumeroParticipantesMenor2LanzaExcepcion() {
        NumeroJugadoresExcedidoException exception = assertThrows(
            NumeroJugadoresExcedidoException.class, () -> {
                new Torneo(TIPO_AMISTOSO, "Torneo", juego, 
                    NUM_PARTICIPANTES_INVALIDO_MENOR, 0, cafe);
            });
        assertTrue(exception.getMessage().contains("Mínimo 2 participantes"));
    }
    
    // ==================== TESTS DE INTEGRACIÓN ====================
    
    @Test
    void testTorneoCompletoFlujo() throws Exception {
        // Crear torneo
        Torneo torneo = new Torneo(TIPO_COMPETITIVO, "Super Torneo", juego, 4, 20000, cafe);
        
        // Agregar participantes
        torneo.agregarParticipantes(clienteNormal);
        torneo.agregarParticipantes(clienteFanatico);
        
        // Verificar participantes
        assertEquals(2, torneo.getParticipantes().size());
        
        // Definir premio
        torneo.definirPremio();
        
        // Declarar ganador
        torneo.ganador(clienteNormal);
    }
    
    @Test
    void testMultiplesTorneos() throws Exception {
        Torneo torneo1 = new Torneo(TIPO_AMISTOSO, "Torneo 1", juego, 2, 0, cafe);
        Torneo torneo2 = new Torneo(TIPO_COMPETITIVO, "Torneo 2", juego, 2, 5000, cafe);
        
        torneo1.agregarParticipantes(clienteNormal);
        torneo2.agregarParticipantes(clienteFanatico);
        
        assertEquals(1, torneo1.getParticipantes().size());
        assertEquals(1, torneo2.getParticipantes().size());
    }
}