package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.usuario.Turno;

class TurnoTest {

    
    private static final int ANIO_VALIDO = 2024;
    private static final int MES_VALIDO = 5;  // Mayo (0-index: 4)
    private static final int DIA_VALIDO = 15;
    
    private static final int ANIO_OTRO = 2024;
    private static final int MES_OTRO = 5;
    private static final int DIA_OTRO = 20;
    
    private static final int ANIO_DIFERENTE = 2025;
    
    private Calendar fechaBase;
    private Calendar fechaDiferente;
    private Calendar fechaMismoDia;
    private Turno turnoActivo;
    private Turno turnoInactivo;
    
    
    @BeforeEach
    void setUp() {
        fechaBase = new GregorianCalendar(ANIO_VALIDO, MES_VALIDO - 1, DIA_VALIDO);
        fechaDiferente = new GregorianCalendar(ANIO_OTRO, MES_OTRO - 1, DIA_OTRO);
        fechaMismoDia = new GregorianCalendar(ANIO_VALIDO, MES_VALIDO - 1, DIA_VALIDO);
        turnoActivo = new Turno(fechaBase, true);
        turnoInactivo = new Turno(fechaBase, false);
    }
    
    //
    
    @Test
    void testConstructorConDosParametros() {
        Turno turno = new Turno(fechaBase, true);
        
        assertNotNull(turno);
        assertEquals(fechaBase, turno.getFecha());
        assertTrue(turno.isActivo());
    }
    
    @Test
    void testConstructorConUnParametro() {
        Turno turno = new Turno(fechaBase);
        
        assertNotNull(turno);
        assertEquals(fechaBase, turno.getFecha());
        assertTrue(turno.isActivo(), "El turno creado con un parámetro debe estar activo por defecto");
    }
    
    @Test
    void testConstructorTurnoInactivo() {
        Turno turno = new Turno(fechaBase, false);
        
        assertNotNull(turno);
        assertEquals(fechaBase, turno.getFecha());
        assertFalse(turno.isActivo());
    }
    
    // getters y setters
    
    @Test
    void testGetFecha() {
        assertEquals(fechaBase, turnoActivo.getFecha());
    }
    
    @Test
    void testSetFecha() {
        Calendar nuevaFecha = new GregorianCalendar(2024, 11, 25);
        turnoActivo.setFecha(nuevaFecha);
        
        assertEquals(nuevaFecha, turnoActivo.getFecha());
    }
    
    @Test
    void testIsActivo() {
        assertTrue(turnoActivo.isActivo());
        assertFalse(turnoInactivo.isActivo());
    }
    
    @Test
    void testSetActivo() {
        turnoActivo.setActivo(false);
        assertFalse(turnoActivo.isActivo());
        
        turnoActivo.setActivo(true);
        assertTrue(turnoActivo.isActivo());
    }
    
    //método
    
    @Test
    void testEsMismaFechaConMismaFecha() {
        assertTrue(turnoActivo.esMismaFecha(fechaMismoDia));
    }
    
    @Test
    void testEsMismaFechaConFechaDiferente() {
        assertFalse(turnoActivo.esMismaFecha(fechaDiferente));
    }
    
    @Test
    void testEsMismaFechaConMismoDiaDiferenteAnio() {
        Calendar fechaOtroAnio = new GregorianCalendar(ANIO_DIFERENTE, MES_VALIDO - 1, DIA_VALIDO);
        assertFalse(turnoActivo.esMismaFecha(fechaOtroAnio));
    }
    
    @Test
    void testEsMismaFechaConMismoAnioDiferenteMes() {
        Calendar fechaOtroMes = new GregorianCalendar(ANIO_VALIDO, 10, DIA_VALIDO); 
        assertFalse(turnoActivo.esMismaFecha(fechaOtroMes));
    }
    
    @Test
    void testEsMismaFechaConMismoAnioMismoMesDiferenteDia() {
        Calendar fechaOtroDia = new GregorianCalendar(ANIO_VALIDO, MES_VALIDO - 1, DIA_VALIDO + 5);
        assertFalse(turnoActivo.esMismaFecha(fechaOtroDia));
    }
    
    @Test
    void testEsMismaFechaConBisiesto() {
        Calendar fechaBisiesto = new GregorianCalendar(2024, 1, 29);
        Turno turnoBisiesto = new Turno(fechaBisiesto);
        
        Calendar mismaFechaBisiesto = new GregorianCalendar(2024, 1, 29);
        assertTrue(turnoBisiesto.esMismaFecha(mismaFechaBisiesto));
    }
    
    @Test
    void testEsMismaFechaConBordeDeAnio() {
        Calendar fechaFinAnio = new GregorianCalendar(2024, 11, 31);
        Turno turnoFinAnio = new Turno(fechaFinAnio);
        
        Calendar fechaInicioAnio = new GregorianCalendar(2025, 0, 1);
        assertFalse(turnoFinAnio.esMismaFecha(fechaInicioAnio));
    }
    

    
    @Test
    void testToStringConTurnoActivo() {
        String resultado = turnoActivo.toString();
        
        assertTrue(resultado.contains(String.valueOf(ANIO_VALIDO)));
        assertTrue(resultado.contains(String.valueOf(MES_VALIDO)));
        assertTrue(resultado.contains(String.valueOf(DIA_VALIDO)));
        assertTrue(resultado.contains("Trabaja"));
        assertFalse(resultado.contains("Descansa"));
    }
    
    @Test
    void testToStringConTurnoInactivo() {
        String resultado = turnoInactivo.toString();
        
        assertTrue(resultado.contains(String.valueOf(ANIO_VALIDO)));
        assertTrue(resultado.contains(String.valueOf(MES_VALIDO)));
        assertTrue(resultado.contains(String.valueOf(DIA_VALIDO)));
        assertTrue(resultado.contains("Descansa"));
        assertFalse(resultado.contains("Trabaja"));
    }
    
    //
    
    @Test
    void testEqualsMismoObjeto() {
        assertTrue(turnoActivo.equals(turnoActivo));
    }
    
    @Test
    void testEqualsMismaFecha() {
        Turno otroTurnoMismaFecha = new Turno(fechaMismoDia);
        assertTrue(turnoActivo.equals(otroTurnoMismaFecha));
    }
    
    @Test
    void testEqualsFechaDiferente() {
        Turno otroTurno = new Turno(fechaDiferente);
        assertFalse(turnoActivo.equals(otroTurno));
    }
    
    @Test
    void testEqualsConNull() {
        assertFalse(turnoActivo.equals(null));
    }
    
    @Test
    void testEqualsConObjetoDiferenteClase() {
        assertFalse(turnoActivo.equals("No es un turno"));
    }
    
    @Test
    void testEqualsMismaFechaDiferenteActivo() {
        Turno turnoActivo1 = new Turno(fechaBase, true);
        Turno turnoActivo2 = new Turno(fechaBase, false);
        
        // equals solo compara fecha, no el estado activo
        assertTrue(turnoActivo1.equals(turnoActivo2));
    }
    
    //
    
    @Test
    void testHashCodeMismaFecha() {
        Turno turno1 = new Turno(fechaBase);
        Turno turno2 = new Turno(fechaMismoDia);
        
        assertEquals(turno1.hashCode(), turno2.hashCode());
    }
    
    @Test
    void testHashCodeFechaDiferente() {
        Turno turno1 = new Turno(fechaBase);
        Turno turno2 = new Turno(fechaDiferente);
        
        assertNotEquals(turno1.hashCode(), turno2.hashCode());
    }
    
    @Test
    void testHashCodeConsistente() {
        int hashCode1 = turnoActivo.hashCode();
        int hashCode2 = turnoActivo.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    // 
    
    @Test
    void testTurnosFechaLimite() {
        Calendar fechaLimite = new GregorianCalendar(2024, 11, 31, 23, 59);
        Calendar fechaInicioAnio = new GregorianCalendar(2025, 0, 1, 0, 0);
        
        Turno turnoLimite = new Turno(fechaLimite);
        
        assertFalse(turnoLimite.esMismaFecha(fechaInicioAnio));
        assertNotEquals(turnoLimite.hashCode(), new Turno(fechaInicioAnio).hashCode());
    }
}