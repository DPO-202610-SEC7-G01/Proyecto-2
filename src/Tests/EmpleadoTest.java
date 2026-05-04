package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import exceptions.CafeException;
import exceptions.ProductosException;
import exceptions.TorneoException;
import exceptions.UsuariosException;
import modelo.Cafe;
import modelo.Torneo;
import modelo.Turno;
import modelo.producto.Bebida;
import modelo.producto.Juego;
import modelo.producto.Producto;
import modelo.usuario.Administrador;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

class EmpleadoTest {
    

	//cafe
	private static final int CAPACIDAD = 100;
	private static final String LOGIN_ADMIN = "00alvaro"; 
	private static final String PASSWORD_ADMIN = "adminAlvaro"; 
	private static final String NOMBRE_ADMIN = "Álvaro";
	
	//empleado
	private static final int ID_VALIDO = 845;
    private static final String LOGIN_VALIDO = "alvaro845";
    private static final String PASSWORD_VALIDO = "Kat08";
    private static final String NOMBRE_VALIDO = "Álvaro";
    
    //cliente
    private static final int ID_CLIENTE = 10;
    private static final String LOGIN_CLIENTE = "Angela123";
    private static final String PASSWORD_CLIENTE = "pass123";
    private static final String NOMBRE_CLIENTE = "Ángela";
    private static final int EDAD_CLIENTE = 25;
    private static final ArrayList<String> ALERGENOS_VACIOS = new ArrayList<>();
    
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
    
    
    private Empleado empleadoValido;
    private Cafe miCafe;
    private Juego juego;
    private Cliente cliente;
    private Torneo torneo;
    
    @BeforeEach
    void setUp() throws Exception {
    	// Asumimos que esto existe antes  de hacer cosas       
        miCafe = new Cafe(CAPACIDAD);
        miCafe.cambiarAdmin(new Administrador(ID_VALIDO, LOGIN_ADMIN, PASSWORD_ADMIN, NOMBRE_ADMIN,miCafe));   
        
        juego = new Juego(ID_JUEGO, PRECIO_JUEGO, NOMBRE_JUEGO, ANIO_JUEGO, 
            EMPRESA_JUEGO, NUM_JUGADORES, RESTRICCION_EDAD, CATEGORIA);
        miCafe.agregarJuegoPrestamo(juego);
        
        torneo = new Torneo(TIPO, NOMBRE_TORNEO, juego, 4, PRECIO_TORNEO, miCafe);
        miCafe.agregarTorneos(torneo);

        cliente = new Cliente(ID_CLIENTE, LOGIN_CLIENTE, PASSWORD_CLIENTE, 
                NOMBRE_CLIENTE, EDAD_CLIENTE, ALERGENOS_VACIOS);

        empleadoValido = new Empleado(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, NOMBRE_VALIDO);
        
    }
    
    //constructor
    @Test
    void testConstructorValido() {
        assertDoesNotThrow(() -> {
            new Empleado(ID_VALIDO, LOGIN_VALIDO, PASSWORD_VALIDO, NOMBRE_VALIDO);
        });
    }
    
    // getters y setters
    
    @Test
    void testGetPuntosFidelidadInicial() {
        assertEquals(0, empleadoValido.getPuntosFidelidad());
    }
    
    @Test
    void testSumarPuntosFidelidadValido() throws UsuariosException {
        empleadoValido.sumarPuntosFidelidad(10);
        assertEquals(10, empleadoValido.getPuntosFidelidad());
        
        empleadoValido.sumarPuntosFidelidad(5);
        assertEquals(15, empleadoValido.getPuntosFidelidad());
    }
    
    @Test
    void testSumarPuntosFidelidadCeroLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            empleadoValido.sumarPuntosFidelidad(0);
        });
        assertTrue(exception.getMessage().contains("deben ser positivos"));
    }
    
    @Test
    void testSumarPuntosFidelidadNegativoLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            empleadoValido.sumarPuntosFidelidad(-5);
        });
        assertTrue(exception.getMessage().contains("deben ser positivos"));
    }
    
    @Test
    void testGetAmigosInicial() {
        assertTrue(empleadoValido.getAmigos().isEmpty());
    }
    
    @Test
    void testAgregarAmigo() {
        empleadoValido.agregarAmigo(cliente);
        
        assertEquals(1, empleadoValido.getAmigos().size());
        assertTrue(empleadoValido.getAmigos().contains(cliente));
        assertTrue(cliente.getAmigos());
    }
    
    @Test
    void testVerificarSiEsAmigo() throws UsuariosException {
        empleadoValido.agregarAmigo(cliente);
        
        assertTrue(empleadoValido.verificarSiEsAmigo(cliente));
        
        Cliente otroCliente = new Cliente(2, "otro", "pass", "Otro", 30, ALERGENOS_VACIOS);
        assertFalse(empleadoValido.verificarSiEsAmigo(otroCliente));
    }
    
    @Test
    void testGetJuegosFavoritosInicial() {
        assertTrue(empleadoValido.getJuegosFavoritos().isEmpty());
    }
    
    @Test
    void testAgregarJuegoFavorito() {
        empleadoValido.agregarJuegoFavorito(juego);
        
        assertEquals(1, empleadoValido.getJuegosFavoritos().size());
        assertTrue(empleadoValido.getJuegosFavoritos().contains(juego));
    }
    
    @Test
    void testAgregarMultiplesJuegosFavoritos() throws Exception {
        Juego juego2 = new Juego(2, 30000, "Damas", 2019, "Mattel", 2, "-5", "Tablero");
        
        empleadoValido.agregarJuegoFavorito(juego);
        empleadoValido.agregarJuegoFavorito(juego2);
        
        assertEquals(2, empleadoValido.getJuegosFavoritos().size());
    }
    
    // métodos
    
    @Test
    void testGetTurnosInicial() {
        assertTrue(empleadoValido.getTurnos().isEmpty());
    }
    
    @Test
    void testAgregarTurno() {
        Calendar fecha = Calendar.getInstance();
        Turno turno = new Turno(fecha, true);
        empleadoValido.agregarTurno(turno);
        
        assertEquals(1, empleadoValido.getTurnos().size());
        assertEquals(turno, empleadoValido.getTurnos().get(0));
    }
    
    // Nose pq gregorian calendar gusta tanto u-u 
    @Test
    void testGetTurnoPorFecha() {
        Calendar fecha1 = new GregorianCalendar(2024, 0, 15);
        Calendar fecha2 = new GregorianCalendar(2024, 0, 16);
        
        Turno turno1 = new Turno(fecha1, true);
        Turno turno2 = new Turno(fecha2, true);
        
        empleadoValido.agregarTurno(turno1);
        empleadoValido.agregarTurno(turno2);
        
        assertEquals(turno1, empleadoValido.getTurnoPorFecha(fecha1));
        assertEquals(turno2, empleadoValido.getTurnoPorFecha(fecha2));
        assertNull(empleadoValido.getTurnoPorFecha(new GregorianCalendar(2024, 0, 17)));
    }
    
    @Test
    void testCambiarFechaTurno() {
        Calendar fechaAntigua = new GregorianCalendar(2024, 0, 15);
        Calendar fechaNueva = new GregorianCalendar(2024, 0, 20);
        
        Turno turno = new Turno(fechaAntigua, true);
        empleadoValido.agregarTurno(turno);
        
        empleadoValido.cambiarFechaTurno(fechaAntigua, fechaNueva);
        
        assertEquals(fechaNueva, turno.getFecha());
    }
    
    @Test
    void testTrabajaEnFecha() {
        Calendar fechaTrabajo = new GregorianCalendar(2024, 0, 15);
        Calendar fechaNoTrabajo = new GregorianCalendar(2024, 0, 16);
        
        Turno turno = new Turno(fechaTrabajo, true);
        empleadoValido.agregarTurno(turno);
        
        assertTrue(empleadoValido.trabajaEnFecha(fechaTrabajo));
        assertFalse(empleadoValido.trabajaEnFecha(fechaNoTrabajo));
    }
    
    @Test
    void testGetDiasTrabajo() {
        Calendar fecha1 = new GregorianCalendar(2024, 0, 15);
        Calendar fecha2 = new GregorianCalendar(2024, 0, 16);
        
        Turno turno1 = new Turno(fecha1, true);
        Turno turno2 = new Turno(fecha2, true);
        
        empleadoValido.agregarTurno(turno1);
        empleadoValido.agregarTurno(turno2);
        
        ArrayList<Calendar> diasTrabajo = empleadoValido.getDiasTrabajo();
        assertEquals(2, diasTrabajo.size());
    }
    
    @Test
    void testGetListaFechas() {
        Calendar fecha1 = new GregorianCalendar(2024, 0, 15);
        Calendar fecha2 = new GregorianCalendar(2024, 0, 16);
        
        Turno turno1 = new Turno(fecha1, true);
        Turno turno2 = new Turno(fecha2, true);
        
        empleadoValido.agregarTurno(turno1);
        empleadoValido.agregarTurno(turno2);
        
        ArrayList<Calendar> fechas = empleadoValido.getListaFechas();
        assertEquals(2, fechas.size());
    }
    
    
    
    @Test
    void testInscribirseTorneoValido() throws TorneoException, CafeException, UsuariosException {
    	empleadoValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        
        assertEquals(1, empleadoValido.getTorneosInscritos().size());
        assertEquals(torneo, empleadoValido.getTorneosInscritos().get(0));
    }
    
    @Test
    void testInscribirseTorneoNoEncontradoLanzaExcepcion() {
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            empleadoValido.inscribirseTorneo("Juego Inexistente", miCafe);
        });
        assertTrue(exception.getMessage().contains("no encontrado"));
    }
    
    @Test
    void testInscribirseMismoTorneoDosVecesLanzaExcepcion() throws Exception {
        Torneo torneo = new Torneo("Competitivo", NOMBRE_TORNEO, juego, 4, PRECIO_TORNEO, miCafe);
        miCafe.agregarTorneos(torneo);
        
        empleadoValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
            empleadoValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        });
        assertTrue(exception.getMessage().contains("ya está inscrito"));
    }
    
    @Test
    void testInscribirseMasDe3TorneosLanzaExcepcion() throws Exception {
        Juego juego1 = new Juego(1, 50000, "Ajedrez", 2020, "Hasbro", 4, "-5", "Tablero");
        Juego juego2 = new Juego(2, 30000, "Damas", 2019, "Mattel", 2, "-5", "Tablero");
        Juego juego3 = new Juego(3, 40000, "Parchís", 2018, "Hasbro", 4, "-5", "Tablero");
        Juego juego4 = new Juego(4, 20000, "Backgammon", 2017, "Mattel", 2, "Adultos", "Tablero");
        
        miCafe.agregarJuegoPrestamo(juego1);
        miCafe.agregarJuegoPrestamo(juego2);
        miCafe.agregarJuegoPrestamo(juego3);
        miCafe.agregarJuegoPrestamo(juego4);
        
        Torneo torneo1 = new Torneo("Competitivo", "Torneo Ajedrez", juego1, 4, 10000, miCafe);
        Torneo torneo2 = new Torneo("Amistoso", "Torneo Damas", juego2, 2, 0, miCafe);
        Torneo torneo3 = new Torneo("Amistoso", "Torneo Parchís", juego3, 4, 0, miCafe);
        Torneo torneo4 = new Torneo("Competitivo", "Torneo Backgammon", juego4, 2, 5000, miCafe);
        
        miCafe.agregarTorneos(torneo1);
        miCafe.agregarTorneos(torneo2);
        miCafe.agregarTorneos(torneo3);
        miCafe.agregarTorneos(torneo4);
        
        empleadoValido.inscribirseTorneo("Ajedrez", miCafe);
        empleadoValido.inscribirseTorneo("Damas", miCafe);
        empleadoValido.inscribirseTorneo("Parchís", miCafe);
        
        assertEquals(3, empleadoValido.getTorneosInscritos().size());
        
        UsuariosException exception = assertThrows(UsuariosException.class, () -> {
        	empleadoValido.inscribirseTorneo("Backgammon", miCafe);
        });
        
        assertTrue(exception.getMessage().contains("excede el límite máximo"));
        assertTrue(exception.getMessage().contains("3"));
    }
    
    @Test
    void testDesinscribirseDeTodosLosTorneos() throws Exception {
        Torneo torneo = new Torneo("Competitivo", NOMBRE_TORNEO, juego, 4, PRECIO_TORNEO, miCafe);
        miCafe.agregarTorneos(torneo);
        
        empleadoValido.inscribirseTorneo(NOMBRE_JUEGO, miCafe);
        assertEquals(1, empleadoValido.getTorneosInscritos().size());
        
        empleadoValido.desinscribirseDeTodosLosTorneos();
        assertEquals(0, empleadoValido.getTorneosInscritos().size());
        assertEquals(0, torneo.getParticipantes().size());
    }
    
    //  HERENCIA
    
    @Test
    void testEmpleadoEsSubclaseDeUsuario() {
        assertTrue(empleadoValido instanceof Usuario);
    }
    
    @Test
    void testEmpleadoHeredaMetodosDeUsuario() {
        assertEquals(ID_VALIDO, empleadoValido.getId());
        assertEquals(LOGIN_VALIDO, empleadoValido.getLogin());
        assertEquals(PASSWORD_VALIDO, empleadoValido.getPassword());
        assertEquals(NOMBRE_VALIDO, empleadoValido.getNombre());
    }
    
    @Test
    void testEmpleadoPuedeCambiarPassword() throws UsuariosException {
        empleadoValido.setPassword("nuevoPassword123");
        assertEquals("nuevoPassword123", empleadoValido.getPassword());
    }
    
    // ==================== TESTS DE INTEGRACIÓN (IGNORADOS) ==================== Deepsek
    
    @Disabled("Prueba de integración - requiere implementación completa de sugerencias")
    @Test
    void testSugerencias() throws ProductosException {
        Producto producto = new Bebida(1, 5000, "Café", "Caliente", false);
        // empleadoValido.sugerencias(producto);
        // Verificar que se agregó la sugerencia
    }
    
    @Disabled("Prueba de integración - requiere implementación completa de aptoPrestamo")
    @Test
    void testAptoPrestamo() {
        Calendar fecha = Calendar.getInstance();
        // boolean apto = empleadoValido.aptoPrestamo(juego, fecha);
        // Verificar resultado
    }
    
    @Disabled("Prueba de integración - requiere Admin y cambio de turnos")
    @Test
    void testPedirCambioTurno() {
        Calendar fechaActual = Calendar.getInstance();
        Calendar nuevaFecha = Calendar.getInstance();
        nuevaFecha.add(Calendar.DAY_OF_MONTH, 1);
        
        // boolean resultado = empleadoValido.pedirCambioTurno(admin, fechaActual, nuevaFecha, otroEmpleado);
        // Verificar resultado
    }
    
    
   
}