package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JuegoTest {
	private static final int ID_JUEGO_VALIDO = 100;
    private static final int PRECIO = 5000;
    private static final String NOMBRE_JUEGO_VALIDO = "Ajedrez";
    private static final int ANIO_PUBLICACION = 2020;
    private static final String EMPRESA_MATRIZ = "Empresa Test";
    private static final int NUM_JUGADORES = 2;
    private static final String RESTRICCION_EDAD = "Adultos";
    private static final String CATEGORIA = "Estrategia";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
