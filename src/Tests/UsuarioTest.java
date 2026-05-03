package Tests;

//utils
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


//esceptions
import exceptions.InvalidCredentialsException;

//mundo
import modelo.usuario.Usuario;

class UsuarioTest {
    
    // Constantes para pruebas correctas
    private static final int ID_CORRECTO = 845;
    private static final String LOGIN_CORRECTO = "alvaro845";
    private static final String PASSWORD_CORRECTO = "Kat08";
    private static final String NOMBRE_CORRECTO = "Álvaro";
    
    // Constantes para pruebas incorrectas
    private static final String LOGIN_INCORRECTO = "a 5"; 

    
    //Constantes
    private Usuario usuarioCorrecto;
    
    
    //Toca poner esto pq es una clase abstracta
    class UsuarioImpl extends Usuario {
        public UsuarioImpl(int id, String login, String password, String nombre) throws InvalidCredentialsException {
            super(id, login, password, nombre);
        }
    }
    
    @BeforeEach
    void setUp() throws InvalidCredentialsException {
        usuarioCorrecto = new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, PASSWORD_CORRECTO, NOMBRE_CORRECTO);
    }
    
    //getters
    @Test
    void testGetId() {
        assertEquals(ID_CORRECTO, usuarioCorrecto.getId());
    }
    
    @Test
    void testGetLogin() {
        assertEquals(LOGIN_CORRECTO, usuarioCorrecto.getLogin());
        assertNotNull(usuarioCorrecto.getLogin());
        assertFalse(usuarioCorrecto.getLogin().isBlank());
    }
    
    @Test
    void testGetPassword() {
        assertEquals(PASSWORD_CORRECTO, usuarioCorrecto.getPassword());
        assertNotNull(usuarioCorrecto.getPassword());
        assertFalse(usuarioCorrecto.getPassword().isBlank());
    }
    
    @Test
    void testGetNombre() {
        assertEquals(NOMBRE_CORRECTO, usuarioCorrecto.getNombre());
        assertNotNull(usuarioCorrecto.getNombre());
        assertFalse(usuarioCorrecto.getNombre().isBlank());
    }
    
    
    @Test
    void testConstructorValido() {
        assertDoesNotThrow(() -> {
            new UsuarioImpl(1, "juan123", "pass123", "Juan Pérez");
        });
    }
    
    //login
    @Test
    void testLoginVacioLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, "", PASSWORD_CORRECTO, NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("El login no puede estar vacío"));
    }
    
    @Test
    void testLoginNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, null, PASSWORD_CORRECTO, NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("El login no puede estar vacío"));
    }
    
    @Test
    void testLoginConEspaciosLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_INCORRECTO, PASSWORD_CORRECTO, NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("solo puede contener letras y números"));
    }
    
    @Test
    void testLoginConCaracteresEspecialesLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, "alvaro@845", PASSWORD_CORRECTO, NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("solo puede contener letras y números"));
    }
    
    //password
    @Test
    void testPasswordVacioLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, "", NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("La constraseña no puede estar vacía"));
    }
    
    @Test
    void testPasswordNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, null, NOMBRE_CORRECTO);
        });
        assertTrue(exception.getMessage().contains("La constraseña no puede estar vacía"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  ", " \n\t ", "\t\n "})
    void testPasswordsInvalidosLanzanExcepcion(String passwordInvalido) {
        assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, passwordInvalido, NOMBRE_CORRECTO);
        });
    }
    
    //nombre
    @Test
    void testNombreVacioLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, PASSWORD_CORRECTO, "");
        });
        assertTrue(exception.getMessage().contains("El nombre no puede estar vacío"));
    }
    
    @Test
    void testNombreNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, PASSWORD_CORRECTO, null);
        });
        assertTrue(exception.getMessage().contains("El nombre no puede estar vacío"));
    }
    
    @Test
    void testNombreConNumerosLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, PASSWORD_CORRECTO, "Álvaro845");
        });
        assertTrue(exception.getMessage().contains("El nombre no puede contener números"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  ", " \n\t ", "\t\n "})
    void testNombresInvalidosLanzanExcepcion(String nombreInvalido) {
        assertThrows(InvalidCredentialsException.class, () -> {
            new UsuarioImpl(ID_CORRECTO, LOGIN_CORRECTO, PASSWORD_CORRECTO, nombreInvalido);
        });
    }
    
    //contraseña
    @Test
    void testSetPasswordValido() {
        String nuevaPassword = "NuevaPass123";
        assertDoesNotThrow(() -> {
            usuarioCorrecto.setPassword(nuevaPassword);
        });
        assertEquals(nuevaPassword, usuarioCorrecto.getPassword());
    }
    
    @Test
    void testSetPasswordConEspacios() throws InvalidCredentialsException {
        usuarioCorrecto.setPassword("  NuevaPass123  ");
        assertEquals("NuevaPass123", usuarioCorrecto.getPassword());
    }
    
    @Test
    void testSetPasswordVacioLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            usuarioCorrecto.setPassword("");
        });
        assertTrue(exception.getMessage().contains("La nueva contraseña no puede estar vacía"));
    }
    
    @Test
    void testSetPasswordNullLanzaExcepcion() {
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            usuarioCorrecto.setPassword(null);
        });
        assertTrue(exception.getMessage().contains("La nueva contraseña no puede estar vacía"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t  "})
    void testSetPasswordInvalidosLanzanExcepcion(String passwordInvalido) {
        assertThrows(InvalidCredentialsException.class, () -> {
            usuarioCorrecto.setPassword(passwordInvalido);
        });
    }
    
    @Test
    void testActualizarPasswordMultiplesVeces() throws InvalidCredentialsException {
        usuarioCorrecto.setPassword("pass1");
        assertEquals("pass1", usuarioCorrecto.getPassword());
        
        usuarioCorrecto.setPassword("pass2");
        assertEquals("pass2", usuarioCorrecto.getPassword());
        
        usuarioCorrecto.setPassword("pass3");
        assertEquals("pass3", usuarioCorrecto.getPassword());
    }
}